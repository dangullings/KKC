package application.controller;

import application.Main;
import application.model.LineItem;
import application.model.Student;
import application.model.Transaction;
import application.util.LineItemDAOImpl;
import application.util.StudentDAOImpl;
import application.util.TransactionDAOImpl;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {

    private static TransactionController instance;

    public TransactionController(){
        instance = this;
    }

    public static TransactionController getInstance(){
        return instance;
    }

    private TransactionDAOImpl transactionDAO = new TransactionDAOImpl();
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
    TableView<Transaction> transactionsTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        transactionDAO.createTransactionTable();

        ObservableList<Transaction> transactions = transactionDAO.selectAllObservableTransactions(completeOrdersOnly);

        TableColumn<Transaction, Integer> colNumber = new TableColumn<>("id");
        colNumber.setMinWidth(50);
        colNumber.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Transaction, LocalDate> colDate = new TableColumn<>("Date");
        colDate.setMinWidth(120);
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Transaction, String> colStudentInfo = new TableColumn<>("Buyer");
        colStudentInfo.setMinWidth(120);

        TableColumn<Transaction, String> colFirstName = new TableColumn<>("First Name");
        colFirstName.setMinWidth(120);
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Transaction, String> colLastName = new TableColumn<>("Last Name");
        colLastName.setMinWidth(120);
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Transaction, String> colSalePrice = new TableColumn<>("Sale Price");
        colSalePrice.setMinWidth(120);
        colSalePrice.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        TableColumn<Transaction, String> colDesc = new TableColumn<>("Note");
        colDesc.setMinWidth(250);
        colDesc.setCellValueFactory(new PropertyValueFactory<>("note"));

        colStudentInfo.getColumns().addAll(colFirstName, colLastName);

        transactionsTable.setItems(transactions);
        transactionsTable.getColumns().addAll(colNumber, colDate, colStudentInfo, colSalePrice, colDesc);
        transactionsTable.setEditable(true);
    }

    @FXML
    public void pressNewOrder(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/NewTransaction.fxml"));
        try {
            Parent root1 = (Parent) loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("New Transaction");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void pressEditOrder(){
        Transaction transactionSelected;
        transactionSelected = transactionsTable.getSelectionModel().getSelectedItem();

        if (transactionSelected == null) {
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/NewTransaction.fxml"));
        try {
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Edit Order");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene((Pane) loader.load()));
            NewTransactionController controller = loader.<NewTransactionController>getController();
            controller.initData(transactionSelected);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void pressRemoveOrder(){
        Transaction transactionSelected;
        transactionSelected = transactionsTable.getSelectionModel().getSelectedItem();

        if (transactionSelected == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Remove Order? (Order will be deleted, and all data will be lost)");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK){
            List<LineItem> lineItems;
            lineItems = lineItemDAO.selectAllLineItemsByTransactionId(transactionSelected.getId());

            for (LineItem lineItem : lineItems){
                lineItemDAO.delete(lineItem.getId());
            }

            transactionDAO.deleteById(transactionSelected.getId());
            transactionsTable.getItems().remove(transactionSelected);
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

        updateTransactionTable();
    }

    public void transactionsTableInsert(Transaction item){
        transactionsTable.getItems().add(item);
    }

    public void updateTransactionTable(){
        transactionsTable.getItems().clear();
        ObservableList<Transaction> transactions;

        transactions = transactionDAO.selectAllObservableTransactions(completeOrdersOnly);

        transactionsTable.setItems(transactions);
    }
}
