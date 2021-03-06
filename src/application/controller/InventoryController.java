package application.controller;

import application.model.Inventory;
import application.model.Item;
import application.model.LineItem;
import application.model.Order;
import application.util.AlertUser;
import application.util.DAO.InventoryDAOImpl;
import application.util.DAO.ItemDAOImpl;
import application.util.GraphicTools;
import application.util.StageLoader;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static application.util.StageLoader.loadStage;

public class InventoryController implements Initializable{

    private static InventoryController instance;

    public InventoryController(){
        instance = this;
    }

    public static InventoryController getInstance(){
        return instance;
    }

    private InventoryDAOImpl inventoryDAO = new InventoryDAOImpl();
    private ItemDAOImpl itemDAO = new ItemDAOImpl();

    @FXML private Button btnNewItem;
    @FXML private Button btnEditItem;
    @FXML private Button btnItemDetail;
    @FXML private Button btnRemoveItem;

    @FXML
    TableView<Inventory.ItemView> inventoryTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable(inventoryDAO.selectAllObservable());
    }

    @FXML
    public void pressNewItem(){
        GraphicTools.setGraphicEffectOnRootView();

        StageLoader.loadStage("view/NewItem.fxml", "New Item");
    }

    @FXML
    public void pressEditItem(){
        Inventory.ItemView inventorySelected;
        inventorySelected = inventoryTable.getSelectionModel().getSelectedItem();

        if (inventorySelected == null) {
            return;
        }

        GraphicTools.setGraphicEffectOnRootView();

        NewItemController controller = loadStage("view/NewItem.fxml", "Edit Item").getController();
        controller.initData(itemDAO.selectById(inventorySelected.getId()));
    }

    @FXML
    public void pressItemDetail(){
        Inventory.ItemView inventorySelected;
        inventorySelected = inventoryTable.getSelectionModel().getSelectedItem();

        if (inventorySelected == null) {
            return;
        }

        GraphicTools.setGraphicEffectOnRootView();

        ItemDetailController controller = loadStage("view/ItemDetail.fxml", "Item Detail").getController();
        controller.initData(itemDAO.selectById(inventorySelected.getId()));
    }

    @FXML
    public void pressRemoveItem(){
        System.out.println("remove item");
        Inventory.ItemView itemSelected;
        itemSelected = inventoryTable.getSelectionModel().getSelectedItem();

        if (itemSelected == null) {
            return;
        }

        Optional<ButtonType> action = AlertUser.alertUser("Confirmation", "Remove Item? (Item will be deleted, and all data will be lost)", Alert.AlertType.CONFIRMATION);

        if (action.get() == ButtonType.OK){
            inventoryDAO.deleteByItemId(itemSelected.getId());
            itemDAO.deleteById(itemSelected.getId());
            inventoryTable.getItems().remove(itemSelected);
        }
    }

    private void initTable(ObservableList<Inventory> inventories){
        TableColumn<Inventory.ItemView, String> colItem = new TableColumn<>("Item Name");
        colItem.setMinWidth(129);
        colItem.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Inventory.ItemView, Integer> colSold = new TableColumn<>("Amount Sold");
        colSold.setMinWidth(80);
        colSold.setCellValueFactory(new PropertyValueFactory<>("sold"));

        TableColumn<Inventory.ItemView, Integer> colQty = new TableColumn<>("Stock Qty");
        colQty.setMinWidth(80);
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Inventory.ItemView, String> colDesc = new TableColumn<>("Item Description");
        colDesc.setMinWidth(450);
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));

        for (Inventory inventory : inventories){
            Item item = itemDAO.selectById(inventory.getItemId());
            inventory.setItemViews(item, inventory);
            inventoryTable.getItems().add(inventory.getItemView());
        }

        inventoryTable.getColumns().addAll(colItem, colSold, colQty, colDesc);
        inventoryTable.setPlaceholder(new Label("no inventory created"));

        inventoryTable.setRowFactory( tv -> {
            TableRow<Inventory.ItemView> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    pressItemDetail();
                }
            });
            return row ;
        });
    }

    public void inventoryTableInsert(Inventory.ItemView inventory){
        inventoryTable.getItems().add(inventory);
    }

    public void updateInventoryTable(){
        InventoryDAOImpl inventoryDAO = new InventoryDAOImpl();
        inventoryTable.getItems().clear();
        ObservableList<Inventory> inventories;

        inventories = inventoryDAO.selectAllObservable();

        for (Inventory inventory : inventories){
            Item item = itemDAO.selectById(inventory.getItemId());
            inventory.setItemViews(item, inventory);
            inventoryTable.getItems().add(inventory.getItemView());
        }
    }
}
