package application.controller;

import application.model.Student;
import application.util.DAO.AttendanceDAOImpl;
import application.util.GraphicTools;
import application.util.StageLoader;
import application.util.DAO.StudentDAOImpl;
import application.util.DAO.Test_StudentDAOImpl;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

import static application.util.AlertUser.alertUser;

public class StudentController implements Initializable{

    private static StudentController instance;

    public StudentController(){
        instance = this;
    }

    public static StudentController getInstance(){
        return instance;
    }

    private StudentDAOImpl studentDAO = new StudentDAOImpl();

    @FXML AnchorPane anchorPane;
    @FXML private Button btnActiveView;
    @FXML TextField filterInput;
    @FXML TableView<Student> studentTable;
    @FXML Label lblStudents;

    private boolean activeStudentsOnly = true;
    private ObservableList<Student> students;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (activeStudentsOnly){
            students = studentDAO.selectAllActiveObservable();
        }else{
            students = studentDAO.selectAllInactiveObservable();
        }

        initStudentTable(students);

        if (activeStudentsOnly){
            btnActiveView.setText("View Inactive");
        }else{
            btnActiveView.setText("View Active");
        }
    }

    private void initStudentTable(ObservableList<Student> students){
        TableColumn<Student, String> colFirstName = new TableColumn<>("First Name");
        colFirstName.setMinWidth(100);
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Student, String> colLastName = new TableColumn<>("Last Name");
        colLastName.setMinWidth(100);
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Student, String> colRank = new TableColumn<>("Rank");
        colRank.setMinWidth(90);
        colRank.setCellValueFactory(new PropertyValueFactory<>("rankNameRounded"));

        TableColumn<Student, String> colClub = new TableColumn<>("Club");
        colClub.setMinWidth(100);
        colClub.setCellValueFactory(new PropertyValueFactory<>("club"));

        //Collections.sort(g.getNodeList(), Student.BY_RANK); // least to most

        TableColumn<Student, String> colEmail = new TableColumn<>("Email");
        colEmail.setMinWidth(105);
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Student, String> colNumber = new TableColumn<>("Number");
        colNumber.setMinWidth(110);
        colNumber.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn<Student, Integer> colAge = new TableColumn<>("Age");
        colAge.setMinWidth(20);
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<Student, LocalDate> colBirthdate = new TableColumn<>("Birth Date");
        colBirthdate.setMinWidth(100);
        colBirthdate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));

        /*
        colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        colFirstName.setOnEditCommit(
                (TableColumn.CellEditEvent<Student, String> t) ->
                        ( t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).updateFirstName(t.getNewValue())
        );
        */

        studentTable.setItems(students);

        initFilter(students);

        studentTable.getColumns().addAll(colFirstName, colLastName, colRank, colClub, colEmail, colNumber, colAge, colBirthdate);
        studentTable.setEditable(true);
    }

    private void initFilter(ObservableList<Student> students) {
        filterInput.textProperty().addListener(new InvalidationListener() {

            @Override
            public void invalidated(javafx.beans.Observable observable) {
                if(filterInput.textProperty().get().isEmpty()) {
                    studentTable.setItems(students);
                    return;
                }

                ObservableList<Student> tableItems = FXCollections.observableArrayList();
                ObservableList<TableColumn<Student, ?>> cols = studentTable.getColumns();

                for(int i=0; i<students.size(); i++) {
                    for(int j=0; j<cols.size(); j++) {
                        TableColumn col = cols.get(j);
                        String cellValue = col.getCellData(students.get(i)).toString();
                        cellValue = cellValue.toLowerCase();
                        if(cellValue.contains(filterInput.textProperty().get().toLowerCase())) {
                            tableItems.add(students.get(i));
                            break;
                        }
                    }
                }
                studentTable.setItems(tableItems);
            }
        });
    }

    public void pressStudentDetail(){
        Student studentSelected;
        studentSelected = studentTable.getSelectionModel().getSelectedItem();

        if (studentSelected == null) {
            return;
        }

        GraphicTools.setGraphicEffectOnRootView();

        StudentDetailController controller = StageLoader.loadStage("view/StudentDetail.fxml", "Student Detail").getController();
        controller.initData(studentSelected);
    }

    @FXML
    public void pressNewStudent(){
        GraphicTools.setGraphicEffectOnRootView();
        StageLoader.loadStage("view/NewStudent.fxml", "New Student");
    }

    @FXML
    public void pressEditStudent(){
        Student studentSelected;
        studentSelected = studentTable.getSelectionModel().getSelectedItem();

        if (studentSelected == null) {
            return;
        }

        GraphicTools.setGraphicEffectOnRootView();

        NewStudentController controller = StageLoader.loadStage("view/NewStudent.fxml", "Edit Student").getController();
        controller.initData(studentSelected);
    }

    public void pressRemove(){
        Student studentSelected;
        studentSelected = studentTable.getSelectionModel().getSelectedItem();

        if (studentSelected == null) {
            return;
        }

        Optional<ButtonType> action = alertUser("Confirmation", "Remove student? (Student will be deleted, and all data will be lost)", Alert.AlertType.CONFIRMATION);

        if (action.get() == ButtonType.OK){
            removeStudent(studentSelected);
        }
    }

    private void removeStudent(Student studentSelected){
        studentDAO.delete(studentSelected.getFirstName(), studentSelected.getLastName());

        Test_StudentDAOImpl test_studentDAO = new Test_StudentDAOImpl();
        test_studentDAO.deleteByStudentId(studentSelected.getId());

        AttendanceDAOImpl attendanceDAO = new AttendanceDAOImpl();
        attendanceDAO.deleteByStudentId(studentSelected.getId());

        studentTable.getItems().remove(studentSelected);
    }

    public void pressActiveView(){
        activeStudentsOnly = !activeStudentsOnly;

        if (activeStudentsOnly){
            btnActiveView.setText("View Inactive");
            lblStudents.setText("Active Students");

        }else{
            btnActiveView.setText("View Active");
            lblStudents.setText("Inactive Students");
        }

        updateStudentTable();
    }

    public void pressListByRank(){
        Collections.sort(students); // compare by rank value, then age
        Collections.reverse(students);
    }

    public void studentTableInsert(Student student){
        studentTable.getItems().add(student);
    }

    public void updateStudentTable(){
        studentTable.getItems().clear();

        if (activeStudentsOnly){
            students = studentDAO.selectAllActiveObservable();
        }else{
            students = studentDAO.selectAllInactiveObservable();
        }

        studentTable.setItems(students);
        initFilter(students);

        AttendanceController.getInstance().init();
    }
}
