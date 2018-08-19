package application.controller;

import application.Main;
import application.model.Student;
import application.model.Test;
import application.util.StudentDAOImpl;
import application.util.Test_StudentDAOImpl;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableListValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentController implements Initializable{

    private static StudentController instance;

    public StudentController(){
        instance = this;
    }

    public static StudentController getInstance(){
        return instance;
    }

    private boolean activeStudentsOnly = true;

    @FXML private Button btnNewStudent;
    @FXML private Button btnStudentDetail;
    @FXML private Button btnRemoveStudent;
    @FXML private Button btnActiveView;
    @FXML private Button btnEdit;

    @FXML
    TableView<Student> studentTable;

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

        TableColumn<Student, String> colFirstName = new TableColumn<>("First Name");
        colFirstName.setMinWidth(100);
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Student, String> colLastName = new TableColumn<>("Last Name");
        colLastName.setMinWidth(100);
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Student, String> colRank = new TableColumn<>("Rank");
        colRank.setMinWidth(100);
        colRank.setCellValueFactory(new PropertyValueFactory<>("rankName"));

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

        colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        colFirstName.setOnEditCommit(
                (TableColumn.CellEditEvent<Student, String> t) ->
                        ( t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).updateFirstName(t.getNewValue())
        );

        colLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        colLastName.setOnEditCommit(
                (TableColumn.CellEditEvent<Student, String> t) ->
                        ( t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).updateLastName(t.getNewValue())
        );

        colEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        colEmail.setOnEditCommit(
                (TableColumn.CellEditEvent<Student, String> t) ->
                        ( t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).updateEmail(t.getNewValue())
        );

        colNumber.setCellFactory(TextFieldTableCell.forTableColumn());
        colNumber.setOnEditCommit(
                (TableColumn.CellEditEvent<Student, String> t) ->
                        ( t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).updateNumber(t.getNewValue())
        );

        studentTable.setItems(students);
        studentTable.getColumns().addAll(colFirstName, colLastName, colRank, colClub, colEmail, colNumber, colAge, colBirthdate);
        studentTable.setEditable(true);

        if (activeStudentsOnly){
            //toggleActive.setStyle("-fx-base: #A1B56C;");
            btnActiveView.setText("View Inactive");
        }else{
            //toggleActive.setStyle("-fx-base: #AB4642;");
            btnActiveView.setText("View Active");
        }
    }

    public void pressStudentDetail(){
        loadStudentDetail();
    }

    public void loadStudentDetail(){
        ObservableList<Student> studentSelected, students;
        students = studentTable.getItems();
        studentSelected = studentTable.getSelectionModel().getSelectedItems();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/StudentDetail.fxml"));
        try {
            //Parent root1 = (Parent) loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Student Detail");
            stage.setScene(new Scene((Pane) loader.load()));
            StudentDetailController controller = loader.<StudentDetailController>getController();
            controller.initData(studentSelected.get(0));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void pressNewStudent(ActionEvent event){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/NewStudent.fxml"));
        try {
            Parent root1 = (Parent) loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("New Student");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void pressEditStudent(ActionEvent event){
        ObservableList<Student> studentSelected, students;
        students = studentTable.getItems();
        studentSelected = studentTable.getSelectionModel().getSelectedItems();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/NewStudent.fxml"));
        try {
            //Parent root1 = (Parent) loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Edit Student");
            stage.setScene(new Scene((Pane) loader.load()));
            NewStudentController controller = loader.<NewStudentController>getController();
            controller.initData(studentSelected.get(0));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void studentTableInsert(Student student){
        studentTable.getItems().add(student);
    }

    public void pressRemove(ActionEvent event){ // remove test_student
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Remove student? (Student will be deleted, and all data will be lost)");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK){
            ObservableList<Student> studentSelected, students;
            students = studentTable.getItems();
            studentSelected = studentTable.getSelectionModel().getSelectedItems();

            StudentDAOImpl sdi = new StudentDAOImpl();
            sdi.delete(studentSelected.get(0).getFirstName(), studentSelected.get(0).getLastName());

            Test_StudentDAOImpl tsdi = new Test_StudentDAOImpl();
            tsdi.deleteByStudentId(studentSelected.get(0).getId());

            studentSelected.forEach(students::remove);
        }
    }

    public void updateStudentTable(){
        StudentDAOImpl sdi = new StudentDAOImpl();
        studentTable.getItems().clear();
        ObservableList<Student> students;

        if (activeStudentsOnly){
            students = sdi.selectAllActiveObservable();
            System.out.println("testing");
        }else{
            students = sdi.selectAllInactiveObservable();
        }

        studentTable.setItems(students);
    }

    public void pressActiveView(){
        activeStudentsOnly = !activeStudentsOnly;

        if (activeStudentsOnly){
            //toggleActive.setStyle("-fx-base: #A1B56C;");
            btnActiveView.setText("View Inactive");
        }else{
            //toggleActive.setStyle("-fx-base: #AB4642;");
            btnActiveView.setText("View Active");
        }

        updateStudentTable();
    }

}
