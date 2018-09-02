package application.controller;

import application.model.Inventory;
import application.model.Item;
import application.util.InventoryDAOImpl;
import application.util.ItemDAOImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class NewItemController implements Initializable {

    private NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
    private ItemDAOImpl itemDAO = new ItemDAOImpl();
    private InventoryDAOImpl inventoryDAO = new InventoryDAOImpl();

    private Item item;
    private Inventory inventory;
    private boolean isNewItem;

    private int newQuantity;
    private int oldQuantity;

    @FXML
    Button btnSave;
    @FXML
    Button btnCancel;
    @FXML
    Button btnDecQty;
    @FXML
    Button btnIncQty;

    @FXML
    TextField txtName;
    @FXML
    TextField txtProduceCost;
    @FXML
    TextField txtSaleCost;
    @FXML
    TextField txtDescription;
    @FXML
    Label lblQuantity;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setNewItem(true);

        newQuantity = 0;
        oldQuantity = 0;

        itemDAO.createItemTable();

        txtProduceCost.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                if (newValue.length() != oldValue.length()) {

                }
            }
        });

        txtSaleCost.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                if (newValue.length() != oldValue.length()) {
                    //System.out.println(formatter.format(new BigDecimal(newValue)));
                }
            }
        });
    }

    public static String currencyFormat(BigDecimal n) {
        return NumberFormat.getCurrencyInstance().format(n);
    }

    public static String currencyFormat(String n) {
        return NumberFormat.getCurrencyInstance().format(n);
    }

    public void pressSave(){
        BigDecimal produceCost = new BigDecimal(txtProduceCost.getText());
        BigDecimal saleCost = new BigDecimal(txtSaleCost.getText());

        formatter.format(produceCost);
        formatter.format(saleCost);

        if (isNewItem){
            Item item = new Item(txtName.getText(), produceCost, saleCost, txtDescription.getText());
            this.item = item;
            itemDAO.insert(item);

            Inventory inventory = new Inventory();
            inventory.addItem(item, newQuantity);
            inventoryDAO.insert(inventory);
            this.inventory = inventory;
        }else{
            item.setName(txtName.getText());
            item.setProduceCost(produceCost);
            item.setSaleCost(saleCost);
            item.setDescription(txtDescription.getText());

            itemDAO.update(item, item.getId());

            inventory.updateItem(item, oldQuantity, newQuantity);

            inventoryDAO.update(inventory, inventory.getItemId());
        }

        if (InventoryController.getInstance() != null) {
            inventory.setItemViews(item, inventory);
            InventoryController.getInstance().inventoryTableInsert(inventory.getItemView());
            InventoryController.getInstance().updateInventoryTable();
        }

        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    public void pressCancel(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Exit item creation? (all data will be lost)");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK){
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
    }

    public void pressDecreaseQuantity(){
        lblQuantity.setText(String.valueOf(--newQuantity));
    }

    public void pressIncreaseQuantity(){
        lblQuantity.setText(String.valueOf(++newQuantity));
    }

    private void loadItemData(Item item){
        InventoryDAOImpl inventoryDAO = new InventoryDAOImpl();
        this.inventory = inventoryDAO.selectById(item.getId());

        txtName.setText(item.getName());
        txtProduceCost.setText(item.getProduceCost().toString());
        txtSaleCost.setText(item.getSaleCost().toString());
        txtDescription.setText(item.getDescription());
        lblQuantity.setText("" + inventory.getQuantity());

        oldQuantity = inventory.getQuantity();
        newQuantity = inventory.getQuantity();

        setNewItem(false);
    }

    public void initData(Item item) {
        this.item = new Item();
        this.item = item;

        loadItemData(item);
    }

    public boolean isNewItem() {
        return isNewItem;
    }

    private void setNewItem(boolean newItem) {
        isNewItem = newItem;
    }
}