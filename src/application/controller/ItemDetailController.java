package application.controller;

import application.model.Inventory;
import application.model.Item;
import application.model.LineItem;
import application.model.Order;
import application.util.DAO.InventoryDAOImpl;
import application.util.DAO.LineItemDAOImpl;
import application.util.DAO.OrderDAOImpl;
import application.util.DAO.TestDAOImpl;
import application.util.GraphicTools;
import application.util.StageLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

public class ItemDetailController {

    @FXML TableView<Order> itemTransactionTable;
    @FXML Label lblName;
    @FXML Label lblDescription;
    @FXML Label lblProduceCost;
    @FXML Label lblSaleCost;
    @FXML Label lblSold;
    @FXML Label lblProduced;
    @FXML Label lblNet;
    @FXML Button btnOk;

    private boolean completeOrdersOnly;
    private OrderDAOImpl orderDAO = new OrderDAOImpl();
    private Item item;

    public void initTable(ObservableList<Order> orders) {
        InventoryDAOImpl inventoryDAO = new InventoryDAOImpl();
        Inventory inventory = inventoryDAO.selectById(item.getId());

        TableColumn<Order, Integer> colNumber = new TableColumn<>("id");
        colNumber.setMinWidth(50);
        colNumber.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Order, LocalDate> colDate = new TableColumn<>("Date");
        colDate.setMinWidth(90);
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Order, String> colStudentInfo = new TableColumn<>("Buyer");
        colStudentInfo.setMinWidth(120);

        TableColumn<Order, String> colFirstName = new TableColumn<>("First Name");
        colFirstName.setMinWidth(120);
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Order, String> colLastName = new TableColumn<>("Last Name");
        colLastName.setMinWidth(120);
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Order, String> colSalePrice = new TableColumn<>("Sale Price");
        colSalePrice.setMinWidth(85);
        colSalePrice.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        TableColumn<Order, String> colDesc = new TableColumn<>("Note");
        colDesc.setMinWidth(254);
        colDesc.setCellValueFactory(new PropertyValueFactory<>("note"));

        colStudentInfo.getColumns().addAll(colFirstName, colLastName);

        itemTransactionTable.setItems(orders);
        itemTransactionTable.getColumns().addAll(colNumber, colDate, colStudentInfo, colSalePrice, colDesc);
        itemTransactionTable.setPlaceholder(new Label("no orders created"));

        itemTransactionTable.setRowFactory( tv -> {
            TableRow<Order> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Order rowData = row.getItem();
                    loadOrderDetail(rowData.getId());
                }
            });
            return row ;
        });

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

    public void initData(Item item) {
        this.item = new Item();
        this.item = item;

        LineItemDAOImpl lineItemDAO = new LineItemDAOImpl();
        OrderDAOImpl orderDAO = new OrderDAOImpl();
        ObservableList<Order> orders = FXCollections.observableArrayList();

        List<LineItem> lineItems = lineItemDAO.selectAllObservableByItemId(item.getId());

        for(LineItem lineItem : lineItems){
            orders.add(orderDAO.selectById(lineItem.getOrderId()));
        }

        initTable(orders);
    }

    private void loadOrderDetail(int orderId){
        OrderDAOImpl orderDAO = new OrderDAOImpl();
        Order order = orderDAO.selectById(orderId);

        OrderDetailController controller = StageLoader.loadStage("view/OrderDetail.fxml", "Order Detail").getController();
        controller.initData(order);
    }

    public void pressOk(){
        GraphicTools.removeGraphicEffectOnRootView();
        Stage stage = (Stage) btnOk.getScene().getWindow();
        stage.close();
        StudentController.getInstance().updateStudentTable();
    }
}