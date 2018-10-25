package application.controller;

import application.model.Student;
import application.model.Test;
import application.util.DAO.StudentDAOImpl;
import application.util.DAO.TestDAOImpl;
import application.util.DAO.Test_StudentDAOImpl;
import application.util.GraphicTools;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static application.util.AlertUser.alertUser;
import static application.util.StageLoader.loadStage;

public class TestController implements Initializable {

    private static TestController instance;

    public TestController(){
        instance = this;
    }

    public static TestController getInstance(){
        return instance;
    }

    private TestDAOImpl testDAO = new TestDAOImpl();

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
        ObservableList<Test> tests = testDAO.selectAllObservable();
        initTestTable(tests);
    }

    private void initTestTable(ObservableList<Test> tests){
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
        GraphicTools.setGraphicEffectOnRootView();

        loadStage("view/NewTest.fxml", "New Test");
    }

    @FXML
    public void pressTestDetail(){
        Test testSelected;
        testSelected = testTable.getSelectionModel().getSelectedItem();

        if (testSelected == null) {
            return;
        }

        GraphicTools.setGraphicEffectOnRootView();
        loadTestDetail(testSelected);
    }

    public void pressEditTest(){
        loadTest();
    }

    private void loadTestDetail(Test testSelected){
        TestDetailController controller = loadStage("view/TestDetail.fxml", "Test Detail").getController();
        controller.initData(testSelected);
    }

    private void loadTest(){
        Test testSelected;
        testSelected = testTable.getSelectionModel().getSelectedItem();

        if (testSelected == null) {
            return;
        }

        GraphicTools.setGraphicEffectOnRootView();

        NewTestController controller = loadStage("view/NewTest.fxml", "Edit Test").getController();
        controller.initData(testSelected);
    }

    public void pressRemoveTest(){
        Test testSelected;
        testSelected = testTable.getSelectionModel().getSelectedItem();

        if (testSelected == null) {
            return;
        }

        Optional<ButtonType> action = alertUser("Confirmation", "Remove Test? (Test will be deleted, and all data will be lost)", Alert.AlertType.CONFIRMATION);

        if (action.get() == ButtonType.OK){
            removeTest(testSelected);
        }
    }

    private void removeTest(Test testSelected){
        Test_StudentDAOImpl test_studentDAO = new Test_StudentDAOImpl();
        test_studentDAO.deleteByTestId(testSelected.getId());

        List<Student> studentsInTest = test_studentDAO.selectAllStudentsByTestId(testSelected.getId());

        StudentDAOImpl studentDAO = new StudentDAOImpl();

        for (Student student : studentsInTest){
            student.decreaseRank();
            studentDAO.update(student, student.getId());
        }

        testTable.getItems().remove(testSelected);

        TestDAOImpl tdi = new TestDAOImpl();
        tdi.delete(testSelected.getId());
    }

    private boolean isRegexMatch(String pattern, String value) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(value);
        return matcher.matches();
    }

    public void updateTestTable(){
        testTable.getItems().clear();
        ObservableList<Test> tests = testDAO.selectAllObservable();
        testTable.setItems(tests);
    }

    public void testTableInsert(Test test){
        testTable.getItems().add(test);
    }
}
