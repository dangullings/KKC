package application.controller;

import application.model.Inventory;
import application.model.Item;
import application.util.DAO.InventoryDAOImpl;
import application.util.DAO.ItemDAOImpl;
import application.util.GraphicTools;
import application.util.StageLoader;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
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

    private void initTable(ObservableList<Inventory> inventories){
        TableColumn<Inventory.ItemView, String> colItem = new TableColumn<>("Item Name");
        colItem.setMinWidth(100);
        colItem.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Inventory.ItemView, Integer> colSold = new TableColumn<>("Amount Sold");
        colSold.setMinWidth(100);
        colSold.setCellValueFactory(new PropertyValueFactory<>("sold"));

        TableColumn<Inventory.ItemView, Integer> colQty = new TableColumn<>("Stock Qty");
        colQty.setMinWidth(100);
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Inventory.ItemView, String> colDesc = new TableColumn<>("Item Description");
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));

        for (Inventory inventory : inventories){
            Item item = itemDAO.selectById(inventory.getItemId());
            inventory.setItemViews(item, inventory);
            inventoryTable.getItems().add(inventory.getItemView());
        }

        inventoryTable.getColumns().addAll(colItem, colSold, colQty, colDesc);
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
