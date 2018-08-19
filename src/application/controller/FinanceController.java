package application.controller;

import application.model.Student;
import application.util.StudentDAOImpl;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class FinanceController{

    @FXML private Button btnPopulate;

    @FXML
    TableView<Student> studentTable;

    public void pressPopulate() {
        StudentDAOImpl sdi = new StudentDAOImpl();
        //ObservableList<Student> students = sdi.selectAllObservable();

        TableColumn<Student, String> colFirstName = new TableColumn<>("First Name");
        colFirstName.setMinWidth(100);
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Student, String> colLastName = new TableColumn<>("Last Name");
        colLastName.setMinWidth(100);
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        //studentTable = new TableView<>();
        //studentTable.setItems(students);
        //studentTable.getColumns().addAll(colFirstName, colLastName);
    }
}
