package application.controller;

import application.model.LineItem;
import application.model.Order;
import application.util.AlertUser;
import application.util.DAO.LineItemDAOImpl;
import application.util.DAO.OrderDAOImpl;
import application.util.GraphicTools;
import application.util.StageLoader;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class OrderController implements Initializable {

    private static OrderController instance;

    public OrderController(){
        instance = this;
    }

    public static OrderController getInstance(){
        return instance;
    }

    private OrderDAOImpl orderDAO = new OrderDAOImpl();
    private LineItemDAOImpl lineItemDAO = new LineItemDAOImpl();

    private boolean completeOrdersOnly;

    @FXML
    Button btnNewOrder;
    @FXML
    Button btnEditOrder;
    @FXML
    Button btnRemoveOrder;
    @FXML
    Button btnOrderView;

    @FXML
    Label lblTableHeader;

    @FXML
    TableView<Order> ordersTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable(orderDAO.selectAllObservableOrders(completeOrdersOnly));
    }

    private void initTable(ObservableList<Order> orders){
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

        ordersTable.setItems(orders);
        ordersTable.getColumns().addAll(colNumber, colDate, colStudentInfo, colSalePrice, colDesc);
    }

    @FXML
    public void pressNewOrder(){
        GraphicTools.setGraphicEffectOnRootView();

        StageLoader.loadStage("view/NewOrder.fxml", "New Order");
    }

    @FXML
    public void pressDetail(){
        Order orderSelected;
        orderSelected = ordersTable.getSelectionModel().getSelectedItem();

        if (orderSelected == null) {
            return;
        }

        GraphicTools.setGraphicEffectOnRootView();

        OrderDetailController controller = StageLoader.loadStage("view/OrderDetail.fxml", "Order Detail").getController();
        controller.initData(orderSelected);
    }

    @FXML
    public void pressEditOrder(){
        Order orderSelected;
        orderSelected = ordersTable.getSelectionModel().getSelectedItem();

        if (orderSelected == null) {
            return;
        }

        GraphicTools.setGraphicEffectOnRootView();

        NewOrderController controller = StageLoader.loadStage("view/NewOrder.fxml", "Edit Order").getController();
        controller.initData(orderSelected);
    }

    @FXML
    public void pressRemoveOrder(){
        Order orderSelected;
        orderSelected = ordersTable.getSelectionModel().getSelectedItem();

        if (orderSelected == null) {
            return;
        }

        Optional<ButtonType> action = AlertUser.alertUser("Confirmation", "Remove Order? (Order will be deleted, and all data will be lost)", Alert.AlertType.CONFIRMATION);

        if (action.get() == ButtonType.OK){
            List<LineItem> lineItems;
            lineItems = lineItemDAO.selectAllLineItemsByOrderId(orderSelected.getId());

            for (LineItem lineItem : lineItems){
                lineItemDAO.delete(lineItem.getId());
            }

            orderDAO.deleteById(orderSelected.getId());
            ordersTable.getItems().remove(orderSelected);
        }
    }

    public void pressOrderView(){
        completeOrdersOnly = !completeOrdersOnly;

        if (completeOrdersOnly){
            btnOrderView.setText("View Orders");
            lblTableHeader.setText("Completed Transactions");
            btnNewOrder.setDisable(true);
            btnEditOrder.setDisable(true);
            btnRemoveOrder.setDisable(true);
        }else{
            btnOrderView.setText("View Transactions");
            lblTableHeader.setText("Pending Orders");
            btnNewOrder.setDisable(false);
            btnEditOrder.setDisable(false);
            btnRemoveOrder.setDisable(false);
        }

        updateOrderTable();
    }

    public void ordersTableInsert(Order item){
        ordersTable.getItems().add(item);
    }

    public void updateOrderTable(){
        ordersTable.getItems().clear();
        ordersTable.setItems(orderDAO.selectAllObservableOrders(completeOrdersOnly));
    }
}
