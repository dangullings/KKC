package application.controller;

import application.Main;
import application.model.Student;
import application.util.StudentDAOImpl;
import application.util.Test_StudentDAOImpl;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class StudentController implements Initializable{

    private static StudentController instance;

    public StudentController(){
        instance = this;
    }

    public static StudentController getInstance(){
        return instance;
    }

    @FXML private Button btnActiveView;
    @FXML TextField filterInput;
    @FXML TableView<Student> studentTable;
    @FXML Label lblStudents;

    private boolean activeStudentsOnly = true;
    private List<Student> studentList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        StudentDAOImpl sdi = new StudentDAOImpl();
        sdi.createStudentTable();

        ObservableList<Student> students;

        if (activeStudentsOnly){
            students = sdi.selectAllActiveObservable();
        }else{
            students = sdi.selectAllInactiveObservable();
        }

        studentList = students;

        TableColumn<Student, String> colFirstName = new TableColumn<>("First Name");
        colFirstName.setMinWidth(100);
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Student, String> colLastName = new TableColumn<>("Last Name");
        colLastName.setMinWidth(100);
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Student, String> colRank = new TableColumn<>("Rank");
        colRank.setMinWidth(100);
        colRank.setCellValueFactory(new PropertyValueFactory<>("rankNameRounded"));

        TableColumn<Student, String> colClub = new TableColumn<>("Club");
        colClub.setMinWidth(100);
        colClub.setCellValueFactory(new PropertyValueFactory<>("club"));

        //Collections.sort(g.getNodeList(), Student.BY_RANK); // least to most

        TableColumn<Student, String> colEmail = new TableColumn<>("Email");
        colEmail.setMinWidth(100);
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Student, String> colNumber = new TableColumn<>("Number");
        colNumber.setMinWidth(100);
        colNumber.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn<Student, Integer> colAge = new TableColumn<>("Age");
        colAge.setMinWidth(30);
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

        if (activeStudentsOnly){
            btnActiveView.setText("View Inactive");
        }else{
            btnActiveView.setText("View Active");
        }
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
        loadStudentDetail();
    }

    public void loadStudentDetail(){
        Student studentSelected;
        studentSelected = studentTable.getSelectionModel().getSelectedItem();

        if (studentSelected == null) {
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/StudentDetail.fxml"));
        try {
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Student Detail");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene((Pane) loader.load()));
            StudentDetailController controller = loader.<StudentDetailController>getController();
            controller.initData(studentSelected);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void pressNewStudent(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/NewStudent.fxml"));
        try {
            Parent root1 = (Parent) loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("New Student");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void pressEditStudent(){
        Student studentSelected;
        studentSelected = studentTable.getSelectionModel().getSelectedItem();

        if (studentSelected == null) {
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/NewStudent.fxml"));
        try {
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Edit Student");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene((Pane) loader.load()));
            NewStudentController controller = loader.<NewStudentController>getController();
            controller.initData(studentSelected);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pressRemove(){ // remove test_student
        Student studentSelected;
        studentSelected = studentTable.getSelectionModel().getSelectedItem();

        if (studentSelected == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Remove student? (Student will be deleted, and all data will be lost)");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK){
            StudentDAOImpl sdi = new StudentDAOImpl();
            sdi.delete(studentSelected.getFirstName(), studentSelected.getLastName());

            Test_StudentDAOImpl tsdi = new Test_StudentDAOImpl();
            tsdi.deleteByStudentId(studentSelected.getId());

            studentTable.getItems().remove(studentSelected);
        }
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
        Collections.sort(studentList); // compare by rank value, then age
        Collections.reverse(studentList);
    }

    public void studentTableInsert(Student student){
        studentTable.getItems().add(student);
    }

    public void updateStudentTable(){
        StudentDAOImpl sdi = new StudentDAOImpl();
        studentTable.getItems().clear();
        ObservableList<Student> students;

        if (activeStudentsOnly){
            students = sdi.selectAllActiveObservable();
        }else{
            students = sdi.selectAllInactiveObservable();
        }

        studentList = students;

        studentTable.setItems(students);
        initFilter(students);

        AttendanceController.getInstance().init();
    }
}
