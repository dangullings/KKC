package application.controller;

import application.model.LineItem;
import application.model.Order;
import application.model.Student;
import application.util.DAO.LineItemDAOImpl;
import application.util.GraphicTools;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class OrderDetailController implements Initializable {

    @FXML TableView<LineItem> lineItemsTable;
    @FXML Label lblOrder;
    @FXML Label lblBuyer;
    @FXML Label lblNote;
    @FXML Label lblSalePrice;
    @FXML Label lblCostValue;
    @FXML Label lblSaleValue;
    @FXML Label lblCost;
    @FXML Label lblNet;
    @FXML Button btnOk;

    private Order order;

    public void initData(Order order) {
        this.order = new Order();
        this.order = order;

        LineItemDAOImpl lineItemDAO = new LineItemDAOImpl();

        ObservableList<LineItem> lineItems = lineItemDAO.selectAllObservableByOrderId(order.getId());

        TableColumn<LineItem, String> colLineItemName = new TableColumn<>("Item Name");
        colLineItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));

        TableColumn<LineItem, Integer> colQuantity = new TableColumn<>("Quantity");
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<LineItem, BigDecimal> colPrice = new TableColumn<>("Sale Price");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        lineItemsTable.setItems(lineItems);
        lineItemsTable.getColumns().addAll(colLineItemName, colQuantity, colPrice);

        lblOrder.setText("Order #"+order.getId());
        lblBuyer.setText("Buyer: "+order.getFirstName() + " " + order.getLastName());
        lblNote.setText("Note: " + order.getNote());
        lblSalePrice.setText("Sale Price:");
        lblCost.setText(("Cost: "));
        lblCostValue.setText("$");
        lblSaleValue.setText("$"+order.getSalePrice());
        lblNet.setText("$");
    }

    public void initialize(URL location, ResourceBundle resources) {

    }

    public void pressOk(){
        GraphicTools.removeGraphicEffectOnRootView();
        Stage stage = (Stage) btnOk.getScene().getWindow();
        stage.close();
    }
}