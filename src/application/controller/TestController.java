package application.controller;

import application.Main;
import application.model.Student;
import application.model.Test;
import application.util.StudentDAOImpl;
import application.util.TestDAOImpl;
import application.util.Test_StudentDAOImpl;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.util.List;
import java.util.Optional;
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
    @FXML private Button btnTestDetail;
    @FXML private Button btnEditTest;
    @FXML private Button btnRemoveTest;

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

        TableColumn<Test, String> colNumStudents = new TableColumn<>("Number of Students");
        colNumStudents.setMinWidth(120);
        colNumStudents.setCellValueFactory(new PropertyValueFactory<>("numStudents"));

        testTable.setItems(tests);
        testTable.getColumns().addAll(colType, colDate, colLocation, colNumStudents);
    }

    @FXML
    public void pressNewTest(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/NewTest.fxml"));
        try {
            Parent root1 = (Parent) loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("New Test");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void pressTestDetail(){
        Test testSelected;
        testSelected = testTable.getSelectionModel().getSelectedItem();

        if (testSelected == null) {
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/TestDetail.fxml"));
        try {
            Parent root1 = (Parent) loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Test Detail");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root1));
            TestDetailController controller = loader.<TestDetailController>getController();
            controller.initData(testSelected);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pressEditTest(){
        loadTest();
    }

    private void loadTest(){
        Test testSelected;
        testSelected = testTable.getSelectionModel().getSelectedItem();

        if (testSelected == null) {
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/NewTest.fxml"));
        try {
            //Parent root1 = (Parent) loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Edit Test");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene((Pane) loader.load()));
            NewTestController controller = loader.<NewTestController>getController();
            controller.initData(testSelected);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pressRemoveTest(){
        Test testSelected;
        testSelected = testTable.getSelectionModel().getSelectedItem();

        if (testSelected == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Remove Test? (Test will be deleted, and all data will be lost)");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK){
            Test_StudentDAOImpl tsdi = new Test_StudentDAOImpl();
            tsdi.deleteByTestId(testSelected.getId());

            List<Student> studentsInTest = tsdi.selectAllStudentsByTestId(testSelected.getId());

            StudentDAOImpl sdi = new StudentDAOImpl();

            for (Student student : studentsInTest){
                student.decreaseRank();
                sdi.update(student, student.getId());
            }

            testTable.getItems().remove(testSelected);

            TestDAOImpl tdi = new TestDAOImpl();
            tdi.delete(testSelected.getId());
        }
    }

    private boolean isRegexMatch(String pattern, String value) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(value);
        return matcher.matches();
    }

    public void updateTestTable(){
        TestDAOImpl tdi = new TestDAOImpl();
        testTable.getItems().clear();
        ObservableList<Test> tests = tdi.selectAllObservable();
        testTable.setItems(tests);
    }

    public void testTableInsert(Test test){
        testTable.getItems().add(test);
    }
}
