package application.controller;

import application.Main;
import application.model.Student;
import application.model.Test;
import application.model.Test_Student;
import application.util.StudentDAOImpl;
import application.util.TestDAOImpl;
import application.util.Test_StudentDAOImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Border;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class StudentDetailController implements Initializable {

    @FXML TableView<Student.TestView> studentTestsTable;

    Student student;

    @FXML Label lblName;
    @FXML Label lblDOB;
    @FXML Label lblRank;
    @FXML Label lblClub;
    @FXML Label lblNumber;
    @FXML Label lblEmail;

    public void initData(Student student) {
        this.student = new Student();
        this.student = student;

        Test_StudentDAOImpl t_sdi = new Test_StudentDAOImpl();

        ObservableList<Test> studentTests = t_sdi.selectAllObservable(student);
        ObservableList<Test_Student> studentTestScores = t_sdi.selectAllObservableScores(student);

        student.setTestViews(studentTests, studentTestScores);

        TableColumn<Student.TestView, String> colTest = new TableColumn<>("Test");
        colTest.setMinWidth(120);
        colTest.setCellValueFactory(new PropertyValueFactory<>("rankName"));

        TableColumn<Student.TestView, LocalDate> colDate = new TableColumn<>("Date");
        colDate.setMinWidth(120);
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Student.TestView, String> colLocation = new TableColumn<>("Location");
        colLocation.setMinWidth(120);
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));


        TableColumn<Student.TestView, String> colScores = new TableColumn<>("Scores");
        colScores.setMinWidth(120);

        TableColumn<Student.TestView, String> colForm = new TableColumn<>("Form");
        colForm.setMinWidth(80);
        colForm.setCellValueFactory(new PropertyValueFactory<>("form"));

        TableColumn<Student.TestView, String> colSteps = new TableColumn<>("Steps");
        colSteps.setMinWidth(80);
        colSteps.setCellValueFactory(new PropertyValueFactory<>("steps"));

        TableColumn<Student.TestView, String> colPower = new TableColumn<>("Power");
        colPower.setMinWidth(80);
        colPower.setCellValueFactory(new PropertyValueFactory<>("power"));

        TableColumn<Student.TestView, String> colKiap = new TableColumn<>("Kiap");
        colKiap.setMinWidth(80);
        colKiap.setCellValueFactory(new PropertyValueFactory<>("kiap"));

        TableColumn<Student.TestView, String> colQuestions = new TableColumn<>("Questions");
        colQuestions.setMinWidth(80);
        colQuestions.setCellValueFactory(new PropertyValueFactory<>("questions"));

        TableColumn<Student.TestView, String> colAttitude = new TableColumn<>("Attitude");
        colAttitude.setMinWidth(80);
        colAttitude.setCellValueFactory(new PropertyValueFactory<>("attitude"));

        TableColumn<Student.TestView, String> colSparring = new TableColumn<>("Sparring");
        colSparring.setMinWidth(80);
        colSparring.setCellValueFactory(new PropertyValueFactory<>("sparring"));

        TableColumn<Student.TestView, String> colBreaking = new TableColumn<>("Breaking");
        colBreaking.setMinWidth(80);
        colBreaking.setCellValueFactory(new PropertyValueFactory<>("breaking"));

        colScores.getColumns().addAll(colForm, colSteps, colPower, colKiap, colQuestions, colAttitude, colSparring, colBreaking);

        studentTestsTable.setItems(student.getObservableTestViews());
        studentTestsTable.getColumns().addAll(colTest, colDate, colLocation, colScores);
        //studentTestsTable_Test.setEditable(true);
    }

    public void initialize(URL location, ResourceBundle resources) {

    }

}

/*

*/
