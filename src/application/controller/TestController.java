package application.controller;

import application.Main;
import application.RANK;
import application.model.Student;
import application.model.Test;
import application.util.StudentDAOImpl;
import application.util.TestDAOImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestController implements Initializable {

    private static TestController instance;

    public TestController(){
        instance = this;
    }

    public static TestController getInstance(){
        return instance;
    }

    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtYear;
    @FXML private TextField txtMonth;
    @FXML private TextField txtDay;
    @FXML private TextField txtEmail;
    @FXML private TextField txtNumber;

    @FXML private Button btnNewTest;
    @FXML private Button btnDelete;

    @FXML
    TableView<Test> testTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TestDAOImpl tdi = new TestDAOImpl();
        tdi.createTestTable();

        ObservableList<Test> tests = tdi.selectAllObservable();

        TableColumn<Test, String> colType = new TableColumn<>("Type");
        colType.setMinWidth(120);
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Test, LocalDate> colDate = new TableColumn<>("Date");
        colDate.setMinWidth(120);
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Test, String> colLocation = new TableColumn<>("Location");
        colLocation.setMinWidth(120);
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));

        testTable.setItems(tests);
        testTable.getColumns().addAll(colType, colDate, colLocation);
        testTable.setEditable(true);
    }

    @FXML
    public void pressNewTest(ActionEvent event){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/NewTest.fxml"));
        try {
            Parent root1 = (Parent) loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("New Test");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testTableInsert(Test test){
        testTable.getItems().add(test);
    }

    public void pressDelete(ActionEvent event){

    }

    private boolean isRegexMatch(String pattern, String value) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(value);
        return matcher.matches();
    }
}
