package application.controller;

import application.model.*;
import application.util.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class NewTransactionController implements Initializable {

    private NumberFormat formatter = NumberFormat.getCurrencyInstance();

    private LineItemDAOImpl lineItemDAO = new LineItemDAOImpl();
    private InventoryDAOImpl inventoryDAO = new InventoryDAOImpl();

    private ArrayList<Student> students;
    private ArrayList<Item> items;
    private ObservableList<LineItem> lineItems;

    private Transaction transaction;
    private Student selectedStudent = new Student();
    private Item selectedItem = new Item();
    private LineItem selectedLineItem = new LineItem();

    private boolean isStudentOrder;
    private boolean isItemOrder;

    @FXML
    ComboBox<Student> studentCombobox;
    @FXML
    ComboBox<Item> itemCombobox;
    @FXML
    TableView<LineItem> lineItemsTable;
    @FXML
    DatePicker datePicker;

    @FXML
    TextField txtQuantity;
    @FXML
    TextField txtPrice;
    @FXML
    TextField txtOther;
    @FXML
    TextField txtNote;

    @FXML
    RadioButton radioStudent;
    @FXML
    RadioButton radioBusiness;
    @FXML
    RadioButton radioInventory;
    @FXML
    RadioButton radioOther;

    @FXML
    Label lblStudentSelected;
    @FXML
    Label lblItemSelected;
    @FXML
    Label lblDate;

    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnAddItem;
    @FXML
    private Button btnRemoveItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeData();
    }

    public void initializeData() {
        transaction = new Transaction();
        lineItemDAO.createLineItemTable();

        students = new ArrayList<>();
        items = new ArrayList<>();

        setItemOrder(true);
        setStudentOrder(true);

        datePicker.setValue(LocalDate.now());

        StudentDAOImpl sdi = new StudentDAOImpl();
        ObservableList<Student> students = sdi.selectAllActiveObservable();

        ItemDAOImpl idi = new ItemDAOImpl();
        ObservableList<Item> items = idi.selectAllObservable();

        LineItemDAOImpl lidi = new LineItemDAOImpl();
        lineItems = lidi.selectAllObservableLineItems();

        Callback<ListView<Student>, ListCell<Student>> factory = lv -> new ListCell<Student>() {
            @Override
            protected void updateItem(Student item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getFirstName() + " " + item.getLastName());
            }
        };

        studentCombobox.setCellFactory(factory);
        studentCombobox.setButtonCell(factory.call(null));
        studentCombobox.setItems(students);

        Callback<ListView<Item>, ListCell<Item>> factoryItem = lv -> new ListCell<Item>() {
            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName() + " " + item.getDescription());
            }
        };

        itemCombobox.setCellFactory(factoryItem);
        itemCombobox.setButtonCell(factoryItem.call(null));
        itemCombobox.setItems(items);

        TableColumn<LineItem, String> colLineItemName = new TableColumn<>("Item Name");
        colLineItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));

        TableColumn<LineItem, Integer> colQuantity = new TableColumn<>("Quantity");
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<LineItem, BigDecimal> colPrice = new TableColumn<>("Sale Price");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        lineItemsTable.setItems(lineItems);
        lineItemsTable.getItems().clear();
        lineItemsTable.getColumns().addAll(colLineItemName, colQuantity, colPrice);

        lineItemsTable.setRowFactory( tv -> {
            TableRow<LineItem> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    selectedLineItem = row.getItem();
                }
            });
            return row ;
        });


        txtQuantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                //txtQuantity.setText("");
            }
        });
    }

    public void pressAddLineItem(){
        LineItem lineItem = new LineItem();

        lineItem.setQuantity(Integer.parseInt(txtQuantity.getText()));

        if (isItemOrder) {
            lineItem.setItemId(selectedItem.getId());
            lineItem.setItemName(selectedItem.getName());
            lineItem.setPrice(selectedItem.getSaleCost().multiply(BigDecimal.valueOf(lineItem.getQuantity())));
        }else{
            BigDecimal price = new BigDecimal(txtPrice.getText());
            formatter.format(price);
            lineItem.setItemName(txtOther.getText());
            lineItem.setPrice(price.multiply(BigDecimal.valueOf(lineItem.getQuantity())));
        }

        lineItems.add(lineItem);

        radioBusiness.setDisable(true);
        radioStudent.setDisable(true);
        studentCombobox.setDisable(true);
    }

    public void pressRemoveLineItem(){
        lineItems.remove(selectedLineItem);

        if (lineItems.isEmpty()){
            radioBusiness.setDisable(false);
            radioStudent.setDisable(false);
            studentCombobox.setDisable(false);
        }
    }

    public void pressSaveTransaction(){
        TransactionDAOImpl transactionDAO = new TransactionDAOImpl();
        LineItemDAOImpl lineItemsDAO = new LineItemDAOImpl();

        transaction.setDate(LocalDate.now());
        transaction.setSalePrice(lineItems);
        transaction.setNote(txtNote.getText());

        if (isStudentOrder){
            transaction.setStudentId(selectedStudent.getId());
            transaction.setStudentInfo();
        }else{
            transaction.setFirstName("Business");
            transaction.setLastName("Business");
        }

        transactionDAO.insert(transaction);

        for (LineItem lineItem : lineItems){
            lineItem.setTransactionId(transaction.getId());
            lineItemsDAO.insert(lineItem);

            if (isItemOrder) {
                Inventory inventory = inventoryDAO.selectById(lineItem.getItemId());
                inventory.sellItem(lineItem.getQuantity(), lineItem.getPrice());
                inventoryDAO.update(inventory, inventory.getItemId());
            }
        }

        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();

        TransactionController.getInstance().transactionsTableInsert(transaction);
        TransactionController.getInstance().updateTransactionTable();

        if (InventoryController.getInstance() != null) {
            InventoryController.getInstance().updateInventoryTable();
        }
    }

    public void chooseStudent(){
        selectedStudent = studentCombobox.getValue();
    }

    public void chooseItem(){
        selectedItem = itemCombobox.getValue();
        txtPrice.setText(selectedItem.getSaleCost().toString());
    }

    public void selectRadioBusiness(){
        radioStudent.setSelected(false);
        studentCombobox.setDisable(true);
        setStudentOrder(false);
    }

    public void selectRadioStudent(){
        radioBusiness.setSelected(false);
        studentCombobox.setDisable(false);
        setStudentOrder(true);
    }

    public void selectRadioInventory(){
        radioOther.setSelected(false);
        itemCombobox.setDisable(false);
        txtOther.setDisable(true);
        if ((selectedItem != null) && (selectedItem.getProduceCost() != null)) {
            txtPrice.setText(selectedItem.getSaleCost().toString());
        }
        txtPrice.setDisable(true);
        setItemOrder(true);
    }

    public void selectRadioOther(){
        radioInventory.setSelected(false);
        itemCombobox.setDisable(true);
        txtOther.setDisable(false);
        txtPrice.setDisable(false);
        setItemOrder(false);
    }

    public boolean isStudentOrder() {
        return isStudentOrder;
    }

    public void setStudentOrder(boolean studentOrder) {
        isStudentOrder = studentOrder;
    }

    public boolean isItemOrder() {
        return isItemOrder;
    }

    public void setItemOrder(boolean itemOrder) {
        isItemOrder = itemOrder;
    }
}
