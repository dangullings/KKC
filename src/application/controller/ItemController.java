package application.controller;

import application.model.Item;
import application.util.ItemDAOImpl;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ItemController implements Initializable{

    @FXML private TextField txtName;
    @FXML private TextField txtQuantity;
    @FXML private TextField txtCost;
    @FXML private TextField txtSale;
    @FXML private Button btnAddItem;
    @FXML private Button btnDeleteItem;
    @FXML private Button btnPopulate;

    @FXML
    TableView<Item> itemTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ItemDAOImpl idi = new ItemDAOImpl();
        idi.createItemTable();

        ObservableList<Item> items = idi.selectAllObservable();

        TableColumn<Item, String> colName = new TableColumn<>("Item Name");
        colName.setMinWidth(100);
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Item, Integer> colquantity = new TableColumn<>("quantity");
        colquantity.setMinWidth(100);
        colquantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Item, Integer> colcost = new TableColumn<>("cost");
        colcost.setMinWidth(100);
        colcost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        TableColumn<Item, Integer> colsale = new TableColumn<>("sale");
        colsale.setMinWidth(100);
        colsale.setCellValueFactory(new PropertyValueFactory<>("sale"));

        itemTable.setItems(items);
        itemTable.getColumns().addAll(colName, colquantity, colcost, colsale);
        itemTable.setEditable(true);
    }

    public void pressAdd(ActionEvent event){
        ItemDAOImpl idi = new ItemDAOImpl();
        idi.createItemTable();

        Item item = new Item(txtName.getText(), Integer.parseInt(txtQuantity.getText()), Integer.parseInt(txtCost.getText()), Integer.parseInt(txtSale.getText()));
        idi.insert(item);
        itemTable.getItems().add(item);

        txtName.clear();
        txtQuantity.clear();
        txtCost.clear();
        txtSale.clear();
    }

    public void pressDelete(ActionEvent event){
        ObservableList<Item> itemSelected, items;
        items = itemTable.getItems();
        itemSelected = itemTable.getSelectionModel().getSelectedItems();

        ItemDAOImpl idi = new ItemDAOImpl();
        idi.delete(itemSelected.get(0).getName());

        itemSelected.forEach(items::remove);
    }
}
