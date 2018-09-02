package application.controller;

import application.model.*;
import application.util.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NewTransactionController implements Initializable {

    private LineItemDAOImpl lineItemDAO = new LineItemDAOImpl();
    private InventoryDAOImpl inventoryDAO = new InventoryDAOImpl();

    private ArrayList<Student> students;
    private ArrayList<Item> items;
    private ArrayList<LineItem> lineItems;

    private Transaction transaction;
    private Student selectedStudent = new Student();
    private Item selectedItem = new Item();
    private LineItem selectedLineItem = new LineItem();

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
        lineItems = new ArrayList<>();

        datePicker.setValue(LocalDate.now());
        lblDate.setText(datePicker.getValue().toString());

        StudentDAOImpl sdi = new StudentDAOImpl();
        ObservableList<Student> students = sdi.selectAllActiveObservable();

        ItemDAOImpl idi = new ItemDAOImpl();
        ObservableList<Item> items = idi.selectAllObservable();

        LineItemDAOImpl lidi = new LineItemDAOImpl();
        ObservableList<LineItem> lineItems = lidi.selectAllObservableLineItems();

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

        TableColumn<LineItem, Integer> colPrice = new TableColumn<>("Sale Price");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        lineItemsTable.setItems(lineItems);
        lineItemsTable.getItems().clear();
        lineItemsTable.getColumns().addAll(colLineItemName, colQuantity, colPrice);

        //studentsTable.setRowFactory( tv -> {
        //    TableRow<Student> row = new TableRow<>();
        //    row.setOnMouseClicked(event -> {
        //        if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
        //            selectedStudent = row.getItem();
        //            lblStudentSelected.setText(selectedStudent.getFirstName() + " " + selectedStudent.getLastName());
        //        }
        //    });
        //    return row ;
        //});

        txtQuantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

            }
        });
    }

    public void pressAddLineItem(){
        LineItem lineItem = new LineItem();

        lineItem.setItemId(selectedItem.getId());
        lineItem.setItemName(selectedItem.getName());
        lineItem.setQuantity(Integer.parseInt(txtQuantity.getText()));
        lineItem.setPrice(selectedItem.getSaleCost().multiply(BigDecimal.valueOf(lineItem.getQuantity())));

        lineItems.add(lineItem);
        lineItemsTable.getItems().add(lineItem);
    }

    public void pressRemoveLineItem(){
        lineItems.remove(selectedLineItem);
        lineItemsTable.getItems().remove(selectedLineItem);
    }

    public void pressSaveTransaction(){
        TransactionDAOImpl transactionDAO = new TransactionDAOImpl();
        LineItemDAOImpl lineItemsDAO = new LineItemDAOImpl();

        transaction.setStudentId(selectedStudent.getId());
        transaction.setStudentInfo();
        transaction.setDate(LocalDate.now());
        transaction.setSalePrice(lineItems);

        transactionDAO.insert(transaction);

        for (LineItem lineItem : lineItems){
            lineItem.setTransactionId(transaction.getId());
            lineItemsDAO.insert(lineItem);

            Inventory inventory = inventoryDAO.selectById(lineItem.getItemId());
            inventory.sellItem(lineItem.getQuantity(), lineItem.getPrice());
            inventoryDAO.update(inventory, inventory.getItemId());
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
        lblStudentSelected.setText(studentCombobox.getValue().getFirstName() + " " + studentCombobox.getValue().getLastName());
    }

    public void chooseItem(){
        selectedItem = itemCombobox.getValue();
        lblItemSelected.setText(itemCombobox.getValue().getName() + " " + itemCombobox.getValue().getDescription());
    }
}
