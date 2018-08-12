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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Border;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
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

    @FXML Button btnOk;

    public void initData(Student student) {
        this.student = new Student();
        this.student = student;

        Test_StudentDAOImpl t_sdi = new Test_StudentDAOImpl();

        ObservableList<Test> studentTests = t_sdi.selectAllObservable(student);
        ObservableList<Test_Student> studentTestScores = t_sdi.selectAllObservableScores(student);

        student.setTestViews(studentTests, studentTestScores);

        TableColumn<Student.TestView, String> colTest = new TableColumn<>("Test");
        colTest.setMinWidth(100);
        colTest.setCellValueFactory(new PropertyValueFactory<>("rankName"));

        TableColumn<Student.TestView, LocalDate> colDate = new TableColumn<>("Date");
        colDate.setMinWidth(100);
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Student.TestView, String> colLocation = new TableColumn<>("Location");
        colLocation.setMinWidth(100);
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));


        TableColumn<Student.TestView, String> colScores = new TableColumn<>("Scores");
        colScores.setMinWidth(120);

        TableColumn<Student.TestView, String> colForm = new TableColumn<>("Form");
        colForm.setMinWidth(60);
        colForm.setMaxWidth(60);
        colForm.setCellValueFactory(new PropertyValueFactory<>("form"));

        TableColumn<Student.TestView, String> colSteps = new TableColumn<>("Steps");
        colSteps.setMinWidth(60);
        colSteps.setMaxWidth(60);
        colSteps.setCellValueFactory(new PropertyValueFactory<>("steps"));

        TableColumn<Student.TestView, String> colPower = new TableColumn<>("Power");
        colPower.setMinWidth(60);
        colPower.setMaxWidth(60);
        colPower.setCellValueFactory(new PropertyValueFactory<>("power"));

        TableColumn<Student.TestView, String> colKiap = new TableColumn<>("Kiap");
        colKiap.setMinWidth(60);
        colKiap.setMaxWidth(60);
        colKiap.setCellValueFactory(new PropertyValueFactory<>("kiap"));

        TableColumn<Student.TestView, String> colQuestions = new TableColumn<>("Questions");
        colQuestions.setMinWidth(60);
        colQuestions.setMaxWidth(60);
        colQuestions.setCellValueFactory(new PropertyValueFactory<>("questions"));

        TableColumn<Student.TestView, String> colAttitude = new TableColumn<>("Attitude");
        colAttitude.setMinWidth(60);
        colAttitude.setMaxWidth(60);
        colAttitude.setCellValueFactory(new PropertyValueFactory<>("attitude"));

        TableColumn<Student.TestView, String> colSparring = new TableColumn<>("Sparring");
        colSparring.setMinWidth(60);
        colSparring.setMaxWidth(60);
        colSparring.setCellValueFactory(new PropertyValueFactory<>("sparring"));

        TableColumn<Student.TestView, String> colBreaking = new TableColumn<>("Breaking");
        colBreaking.setMinWidth(60);
        colBreaking.setMaxWidth(60);
        colBreaking.setCellValueFactory(new PropertyValueFactory<>("breaking"));

        colScores.getColumns().addAll(colForm, colSteps, colPower, colKiap, colQuestions, colAttitude, colSparring, colBreaking);

        studentTestsTable.setItems(student.getObservableTestViews());
        studentTestsTable.getColumns().addAll(colTest, colDate, colLocation, colScores);
        //studentTestsTable_Test.setEditable(true);

        lblName.setText(student.getFirstName() + " " + student.getLastName());
        lblDOB.setText("DOB: " + student.getBirthDate().toString());
        lblRank.setText("Rank: " + student.getRankName());
        lblClub.setText("Club: " + student.getClub());
        lblNumber.setText("Phone Number: " + student.getNumber());
        lblEmail.setText("Email: " + student.getEmail());
    }

    public void initialize(URL location, ResourceBundle resources) {

    }

    public void pressOk(){
        Stage stage = (Stage) btnOk.getScene().getWindow();
        stage.close();
    }

}

/*

*/
