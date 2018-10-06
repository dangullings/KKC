package application.controller;

import application.Main;
import application.model.Student;
import application.model.Test;
import application.util.Test_StudentDAOImpl;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TestDetailController implements Initializable {

    @FXML TableView<Student> studentTable;
    @FXML Label lblTest;
    @FXML Label lblDate;
    @FXML Label lblLocation;
    @FXML Label lblType;
    @FXML Label lblStudentNum;
    @FXML Button btnOk;

    private Test test;
    private Student student;

    public void initData(Test test) {
        this.test = test;

        Test_StudentDAOImpl test_studentDAO = new Test_StudentDAOImpl();

        ObservableList<Student> students = test_studentDAO.selectAllObservableStudentsByTestId(test.getId());

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

        studentTable.setItems(students);
        studentTable.getColumns().addAll(colFirstName, colLastName, colRank, colClub, colEmail, colNumber, colAge, colBirthdate);
        colLastName.setSortType(TableColumn.SortType.DESCENDING);
        studentTable.getSortOrder().setAll(colLastName);

        studentTable.setRowFactory( tv -> {
            TableRow<Student> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Student rowData = row.getItem();
                    loadStudentDetail(rowData);
                }
            });
            return row ;
        });

        if (student != null) {
            Student focusStudent = new Student();

            for (Student student : students) {
                if (student.getId() == this.student.getId()) {
                    focusStudent = student;
                    break;
                }
            }

            studentTable.getSelectionModel().select(focusStudent);
            studentTable.scrollTo(focusStudent);
        }

        lblTest.setText("Test Detail");
        lblDate.setText("Date: " + test.getDate().toString());
        lblType.setText("Type: " + test.getType());
        lblLocation.setText("Location: " + test.getLocation());
        lblStudentNum.setText("Number of Students: " + test.getNumStudents());
    }

    public void initialize(URL location, ResourceBundle resources) {

    }

    private void loadStudentDetail(Student student){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/StudentDetail.fxml"));
        try {
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Student Detail");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene((Pane) loader.load()));
            StudentDetailController controller = loader.<StudentDetailController>getController();
            controller.setTest(test);
            controller.initData(student);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStudent(Student student){
        this.student = student;
    }

    public void pressOk(){
        Stage stage = (Stage) btnOk.getScene().getWindow();
        stage.close();
    }
}