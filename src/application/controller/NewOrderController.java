package application.controller;

import application.model.*;
import application.util.AlertUser;
import application.util.DAO.*;
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
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class NewOrderController implements Initializable {

    private NumberFormat formatter = NumberFormat.getCurrencyInstance();

    private OrderDAOImpl orderDAO = new OrderDAOImpl();
    private LineItemDAOImpl lineItemDAO = new LineItemDAOImpl();
    private InventoryDAOImpl inventoryDAO = new InventoryDAOImpl();
    private StudentDAOImpl studentDAO = new StudentDAOImpl();
    private ItemDAOImpl itemDAO = new ItemDAOImpl();

    private ArrayList<Student> students;
    private ArrayList<Item> items;
    private ObservableList<LineItem> lineItems;
    private ArrayList<LineItem> lineItemsBeforeEdit;

    private Order order;
    private Student selectedStudent = new Student();
    private Item selectedItem = new Item();
    private LineItem selectedLineItem = new LineItem();

    private boolean isStudentOrder;
    private boolean isItemOrder;
    private boolean isNewOrder;

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
    private Button btnSave;
    @FXML
    private Button btnSaveComplete;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnAddItem;
    @FXML
    private Button btnRemoveItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();
    }

    public void initData() {
        order = new Order();
        students = new ArrayList<>();
        items = new ArrayList<>();

        setNewOrder(true);
        setItemOrder(true);
        setStudentOrder(true);

        lineItems = lineItemDAO.selectAllObservableLineItems();

        initUIData();
        initLineItemTable();
        addUIListeners();
    }

    private void initUIData(){
        Callback<ListView<Student>, ListCell<Student>> factory = lv -> new ListCell<Student>() {
            @Override
            protected void updateItem(Student item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getFirstName() + " " + item.getLastName());
            }
        };

        studentCombobox.setCellFactory(factory);
        studentCombobox.setButtonCell(factory.call(null));
        studentCombobox.setItems(studentDAO.selectAllActiveObservable());

        Callback<ListView<Item>, ListCell<Item>> factoryItem = lv -> new ListCell<Item>() {
            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName() + " " + item.getDescription());
            }
        };

        itemCombobox.setCellFactory(factoryItem);
        itemCombobox.setButtonCell(factoryItem.call(null));
        itemCombobox.setItems(itemDAO.selectAllObservable());

        datePicker.setValue(LocalDate.now());
    }

    private void addUIListeners(){
        txtQuantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                //txtQuantity.setText("");
            }
        });
    }

    private void initLineItemTable(){
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
    }

    private void loadOrderData(Order t){
        lineItemsBeforeEdit = new ArrayList<>();
        order = new Order();
        order = t;

        Student student = studentDAO.selectById(order.getStudentId());

        lineItems = lineItemDAO.selectAllObservableByOrderId(order.getId());
        lineItemsBeforeEdit.addAll(lineItems);

        if (order.getFirstName().equals("Business")){
            studentCombobox.setDisable(true);
            radioStudent.setSelected(false);
            radioBusiness.setSelected(true);
            setStudentOrder(false);
        }else{
            studentCombobox.setValue(student);
            studentCombobox.setDisable(false);
            radioStudent.setSelected(true);
            radioBusiness.setSelected(false);
            setStudentOrder(true);
            selectedStudent = student;
        }

        if (!lineItems.isEmpty()){
            radioBusiness.setDisable(true);
            radioStudent.setDisable(true);
            studentCombobox.setDisable(true);
        }

        lineItemsTable.setItems(lineItems);

        datePicker.setValue(order.getDate());
        txtNote.setText(order.getNote());

        setNewOrder(false);
    }

    public void initData(Order order) {
        this.order = new Order();
        this.order = order;

        loadOrderData(order);
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

    public void pressSaveOrder(){
        saveOrder();
    }

    public void pressSaveComplete(){
        saveOrder();
        completeOrder();
    }

    private void saveOrder(){
        setOrderDataFromUI();

        if (isStudentOrder){
            order.setStudentId(selectedStudent.getId());
            order.setStudentInfo();
        }else{
            order.setFirstName("Business");
            order.setLastName("Business");
        }

        if (isNewOrder) {
            orderDAO.insert(order);
        }else{
            orderDAO.update(order, order.getId());
        }

        for (LineItem lineItem : lineItems){
            lineItem.setOrderId(order.getId());

            if (isNewOrder()){
                lineItemDAO.insert(lineItem);
            }else{
                if (lineItemsBeforeEdit.contains(lineItem)){
                    lineItemDAO.update(lineItem, lineItem.getId());
                }else{
                    lineItemDAO.insert(lineItem);
                }
            }
        }

        if (!isNewOrder()) {
            for (LineItem lineItem_b : lineItemsBeforeEdit) {
                if (!lineItems.contains(lineItem_b)) {
                    lineItemDAO.delete(lineItem_b.getId());
                }
            }
        }

        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();

        OrderController.getInstance().ordersTableInsert(order);
        OrderController.getInstance().updateOrderTable();

        if (InventoryController.getInstance() != null) {
            InventoryController.getInstance().updateInventoryTable();
        }
    }

    private void completeOrder(){
        order.setComplete(true);
        orderDAO.update(order, order.getId());

        for (LineItem lineItem : lineItems){
            if (lineItem.getItemId() != 0) {
                Inventory inventory = inventoryDAO.selectById(lineItem.getItemId());
                inventory.sellItem(lineItem.getQuantity(), lineItem.getPrice());
                inventoryDAO.update(inventory, inventory.getItemId());
            }
        }

        OrderController.getInstance().ordersTableInsert(order);
        OrderController.getInstance().updateOrderTable();
    }

    @FXML
    private void pressCancel(){
        Optional<ButtonType> action = AlertUser.alertUser("Confirmation Dialog", "Exit? (all changed data will be lost)", Alert.AlertType.CONFIRMATION);

        if (action.get() == ButtonType.OK){
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
    }

    private void setOrderDataFromUI(){
        order.setDate(datePicker.getValue());
        order.setSalePrice(lineItems);
        order.setNote(txtNote.getText());
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
        txtOther.clear();
        txtOther.setDisable(true);
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
        txtOther.setDisable(false);
        txtPrice.setDisable(false);
        setItemOrder(false);
    }

    public boolean isStudentOrder() {
        return isStudentOrder;
    }

    private void setStudentOrder(boolean studentOrder) {
        isStudentOrder = studentOrder;
    }

    public boolean isItemOrder() {
        return isItemOrder;
    }

    private void setItemOrder(boolean itemOrder) {
        isItemOrder = itemOrder;
    }

    public boolean isNewOrder() {
        return isNewOrder;
    }

    private void setNewOrder(boolean newOrder) {
        isNewOrder = newOrder;
    }
}
