package application.controller;

import application.Main;
import application.model.Student;
import application.model.Test;
import application.util.StudentDAOImpl;
import application.util.Test_StudentDAOImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.util.Collections;
import java.util.ResourceBundle;
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

    @FXML private Button btnNewStudent;
    @FXML private Button btnStudentDetail;
    @FXML private Button btnRemoveStudent;

    @FXML
    TableView<Student> studentTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        StudentDAOImpl sdi = new StudentDAOImpl();
        sdi.createStudentTable();

        ObservableList<Student> students = sdi.selectAllObservable();

        TableColumn<Student, String> colFirstName = new TableColumn<>("First Name");
        colFirstName.setMinWidth(120);
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Student, String> colLastName = new TableColumn<>("Last Name");
        colLastName.setMinWidth(120);
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Student, String> colRank = new TableColumn<>("Rank");
        colRank.setMinWidth(120);
        colRank.setCellValueFactory(new PropertyValueFactory<>("rankName"));

        TableColumn<Student, String> colClub = new TableColumn<>("Club");
        colClub.setMinWidth(120);
        colClub.setCellValueFactory(new PropertyValueFactory<>("club"));

        //Collections.sort(g.getNodeList(), Student.BY_RANK); // least to most

        TableColumn<Student, String> colEmail = new TableColumn<>("Email");
        colEmail.setMinWidth(140);
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Student, String> colNumber = new TableColumn<>("Number");
        colNumber.setMinWidth(100);
        colNumber.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn<Student, Integer> colAge = new TableColumn<>("Age");
        colAge.setMinWidth(30);
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<Student, LocalDate> colBirthdate = new TableColumn<>("Birth Date");
        colBirthdate.setMinWidth(80);
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

        studentTable.setItems(students);
        studentTable.getColumns().addAll(colFirstName, colLastName, colRank, colClub, colEmail, colNumber, colAge, colBirthdate);
        studentTable.setEditable(true);
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

    public void studentTableInsert(Student student){
        studentTable.getItems().add(student);
    }

    public void pressRemove(ActionEvent event){ // remove test_student
        ObservableList<Student> studentSelected, students;
        students = studentTable.getItems();
        studentSelected = studentTable.getSelectionModel().getSelectedItems();

        StudentDAOImpl sdi = new StudentDAOImpl();
        sdi.delete(studentSelected.get(0).getFirstName(), studentSelected.get(0).getLastName());

        Test_StudentDAOImpl tsdi = new Test_StudentDAOImpl();
        tsdi.deleteByStudentId(studentSelected.get(0).getId());

        studentSelected.forEach(students::remove);
    }

    public void updateStudentTable(){
        StudentDAOImpl sdi = new StudentDAOImpl();
        studentTable.getItems().clear();
        ObservableList<Student> students = sdi.selectAllObservable();
        studentTable.setItems(students);
    }

}
