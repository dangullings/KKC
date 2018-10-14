package application.controller;

import application.model.Inventory;
import application.model.Item;
import application.model.LineItem;
import application.util.DAO.InventoryDAOImpl;
import application.util.DAO.LineItemDAOImpl;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class ItemDetailController implements Initializable {

    @FXML TableView<LineItem> itemTransactionTable;
    @FXML Label lblName;
    @FXML Label lblDescription;
    @FXML Label lblProduceCost;
    @FXML Label lblSaleCost;
    @FXML Label lblSold;
    @FXML Label lblProduced;
    @FXML Label lblNet;
    @FXML Button btnOk;

    private Item item;

    public void initData(Item item) {
        this.item = new Item();
        this.item = item;

        InventoryDAOImpl inventoryDAO = new InventoryDAOImpl();
        LineItemDAOImpl lineItemDAO = new LineItemDAOImpl();

        Inventory inventory = inventoryDAO.selectById(item.getId());
        ObservableList<LineItem> lineItems = lineItemDAO.selectAllObservableByItemIdComplete(item.getId(), true);

        TableColumn<LineItem, String> colTranId = new TableColumn<>("Order Id");
        colTranId.setMinWidth(100);
        colTranId.setCellValueFactory(new PropertyValueFactory<>("transactionId"));

        TableColumn<LineItem, String> colQty = new TableColumn<>("Quantity Sold");
        colQty.setMinWidth(100);
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<LineItem, String> colPrice = new TableColumn<>("Sale Price");
        colPrice.setMinWidth(100);
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        itemTransactionTable.setItems(lineItems);
        itemTransactionTable.getColumns().addAll(colTranId, colQty, colPrice);
        colTranId.setSortType(TableColumn.SortType.DESCENDING);
        itemTransactionTable.getSortOrder().setAll(colTranId);

        lblName.setText(item.getName());
        lblDescription.setText("Description: " + item.getDescription());
        lblProduceCost.setText("Produce Cost: $" + item.getProduceCost());
        lblSaleCost.setText("Sale Cost: $" + item.getSaleCost());

        int producedAmt = 0;
        BigDecimal producedCost = BigDecimal.ZERO;
        int soldAmt = 0;
        BigDecimal saleCost = BigDecimal.ZERO;

        producedAmt = inventory.getProduced();
        producedCost = producedCost.add(new BigDecimal(inventory.getProducedCost().toString()));

        soldAmt = inventory.getSold();
        saleCost = saleCost.add(new BigDecimal(inventory.getSalesCost().toString()));

        //MathContext mc = new MathContext(2);

        int netAmt = producedAmt - soldAmt;
        BigDecimal netCost = saleCost.subtract(producedCost);

        lblProduced.setText(producedAmt + " ($" + producedCost + ")");
        lblSold.setText(soldAmt + " ($" + saleCost + ")");
        lblNet.setText(netAmt + " ($" + netCost + ")");
    }

    public void initialize(URL location, ResourceBundle resources) {

    }

    public void pressOk(){
        Stage stage = (Stage) btnOk.getScene().getWindow();
        stage.close();
        StudentController.getInstance().updateStudentTable();
    }
}