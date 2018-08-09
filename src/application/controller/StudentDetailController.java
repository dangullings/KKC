package application.controller;

import application.model.Student;
import application.model.Test;
import application.util.StudentDAOImpl;
import application.util.TestDAOImpl;
import application.util.Test_StudentDAOImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Border;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class StudentDetailController implements Initializable {

    @FXML
    TableView<Test> studentTestsTable;
    Student student;

    public void initData(Student student) {
        this.student = new Student();
        this.student = student;

        Test_StudentDAOImpl t_sdi = new Test_StudentDAOImpl();

        ObservableList<Test> studentTests = t_sdi.selectAllObservable(student);

        TableColumn<Test, String> colType = new TableColumn<>("Type");
        colType.setMinWidth(120);
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Test, LocalDate> colDate = new TableColumn<>("Date");
        colDate.setMinWidth(120);
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Test, String> colLocation = new TableColumn<>("Location");
        colLocation.setMinWidth(120);
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Test, String> colScores = new TableColumn<>("Scores");
        colScores.setMinWidth(120);
        //colScores.setCellValueFactory(new PropertyValueFactory<>("scores"));

        TableColumn<Test, String> colScore1 = new TableColumn<>("Score1");
        TableColumn<Test, String> colScore2 = new TableColumn<>("Score2");
        TableColumn<Test, String> colScore3 = new TableColumn<>("Score3");

        colScores.getColumns().addAll(colScore1, colScore2, colScore3);

        studentTestsTable.setItems(studentTests);
        studentTestsTable.getColumns().addAll(colType, colDate, colLocation, colScores);
        studentTestsTable.setEditable(true);
    }

    public void initialize(URL location, ResourceBundle resources) {

    }

}
