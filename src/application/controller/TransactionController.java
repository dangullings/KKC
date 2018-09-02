package application.controller;

import application.Main;
import application.model.Student;
import application.model.Transaction;
import application.util.TransactionDAOImpl;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {

    private static TransactionController instance;

    public TransactionController(){
        instance = this;
    }

    public static TransactionController getInstance(){
        return instance;
    }

    boolean isStudentTran;

    @FXML
    RadioButton radioStudent;

    @FXML
    RadioButton radioBusiness;

    @FXML
    Button btnNewTransaction;

    @FXML
    TableView<Transaction> transactionsTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TransactionDAOImpl tdi = new TransactionDAOImpl();
        tdi.createTransactionTable();

        ObservableList<Transaction> transactions = tdi.selectAllObservableTransactions();

        TableColumn<Transaction, Integer> colNumber = new TableColumn<>("id");
        colNumber.setMinWidth(120);
        colNumber.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Transaction, LocalDate> colDate = new TableColumn<>("Date");
        colDate.setMinWidth(120);
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Transaction, String> colStudentInfo = new TableColumn<>("Student");
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
        colDesc.setMinWidth(120);
        colDesc.setCellValueFactory(new PropertyValueFactory<>("note"));

        colStudentInfo.getColumns().addAll(colFirstName, colLastName);

        transactionsTable.setItems(transactions);
        transactionsTable.getColumns().addAll(colNumber, colDate, colStudentInfo, colSalePrice, colDesc);
        transactionsTable.setEditable(true);
    }

    @FXML
    public void pressNewTransaction(){
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

    public void transactionsTableInsert(Transaction item){
        transactionsTable.getItems().add(item);
    }

    public void updateTransactionTable(){
        TransactionDAOImpl tdi = new TransactionDAOImpl();
        transactionsTable.getItems().clear();
        ObservableList<Transaction> transactions;

        transactions = tdi.selectAllObservableTransactions();
        transactionsTable.setItems(transactions);
    }
}
