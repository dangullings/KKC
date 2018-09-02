package application.controller;

import application.Main;
import application.model.Inventory;
import application.model.Item;
import application.util.InventoryDAOImpl;
import application.util.ItemDAOImpl;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InventoryController implements Initializable{

    private static InventoryController instance;

    public InventoryController(){
        instance = this;
    }

    public static InventoryController getInstance(){
        return instance;
    }

    private ItemDAOImpl itemDAO = new ItemDAOImpl();

    @FXML private Button btnNewItem;
    @FXML private Button btnEditItem;
    @FXML private Button btnItemDetail;

    @FXML
    TableView<Inventory.ItemView> inventoryTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ItemDAOImpl itemDAO = new ItemDAOImpl();
        itemDAO.createItemTable();
        InventoryDAOImpl inventoryDAO = new InventoryDAOImpl();
        inventoryDAO.createInventoryTable();

        ObservableList<Inventory> inventories = inventoryDAO.selectAllObservable();

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

        //inventoryTable.setItems(inventories);
        inventoryTable.getColumns().addAll(colItem, colSold, colQty, colDesc);
    }

    @FXML
    public void pressNewItem(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/NewItem.fxml"));
        try {
            Parent root1 = (Parent) loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("New Item");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void pressEditItem(){
        Inventory.ItemView inventorySelected;
        inventorySelected = inventoryTable.getSelectionModel().getSelectedItem();

        if (inventorySelected == null) {
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/NewItem.fxml"));
        try {
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Edit Item");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene((Pane) loader.load()));
            NewItemController controller = loader.<NewItemController>getController();
            controller.initData(itemDAO.selectById(inventorySelected.getId()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void pressItemDetail(){
        Inventory.ItemView inventorySelected;
        inventorySelected = inventoryTable.getSelectionModel().getSelectedItem();

        if (inventorySelected == null) {
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/ItemDetail.fxml"));
        try {
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Item Detail");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene((Pane) loader.load()));
            ItemDetailController controller = loader.<ItemDetailController>getController();
            controller.initData(itemDAO.selectById(inventorySelected.getId()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void inventoryTableInsert(Inventory.ItemView inventory){
        inventoryTable.getItems().add(inventory);
    }

    public void updateInventoryTable(){
        InventoryDAOImpl idi = new InventoryDAOImpl();
        inventoryTable.getItems().clear();
        ObservableList<Inventory> inventories;

        inventories = idi.selectAllObservable();

        for (Inventory inventory : inventories){
            Item item = itemDAO.selectById(inventory.getItemId());
            inventory.setItemViews(item, inventory);
            inventoryTable.getItems().add(inventory.getItemView());
        }
    }
}
