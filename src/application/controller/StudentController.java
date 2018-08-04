package application.controller;

import application.Main;
import application.model.Student;
import application.util.DBUtil;
import application.util.StudentDAOImpl;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class StudentController {

    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private Button btnAdd;
    @FXML private Button btnDelete;
    @FXML private Button btnPopulate;

    @FXML
    TableView<Student> studentTable;

    private Main main;

    public void setMain(Main main){
        this.main = main;
    }

    public void pressAdd(ActionEvent event){
        StudentDAOImpl sdi = new StudentDAOImpl();
        //sdi.createStudentTable();

        Student student = new Student(txtFirstName.getText(), txtLastName.getText());
        sdi.insert(student);
        studentTable.getItems().add(student);

        txtFirstName.clear();
        txtLastName.clear();

        //Student student = sdi.selectById(1);

        //Student student = new Student("Dan", "Gullings");
        //sdi.update(student, 1);
    }

    public void pressDelete(ActionEvent event){
        ObservableList<Student> studentSelected, students;
        students = studentTable.getItems();
        studentSelected = studentTable.getSelectionModel().getSelectedItems();

        StudentDAOImpl sdi = new StudentDAOImpl();
        sdi.delete(studentSelected.get(0).getFirstName(), studentSelected.get(0).getLastName());

        studentSelected.forEach(students::remove);
    }

    public void pressPopulate(){
        StudentDAOImpl sdi = new StudentDAOImpl();
        ObservableList<Student> students = sdi.selectAllObservable();

        TableColumn<Student, String> colFirstName = new TableColumn<>("First Name");
        colFirstName.setMinWidth(100);
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Student, String> colLastName = new TableColumn<>("Last Name");
        colLastName.setMinWidth(100);
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        studentTable.setItems(students);
        studentTable.getColumns().addAll(colFirstName, colLastName);
    }

}
