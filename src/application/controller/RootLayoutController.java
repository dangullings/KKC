package application.controller;

import application.Main;
import application.model.Student;
import application.util.StudentDAOImpl;
import application.util.Test_StudentDAOImpl;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RootLayoutController implements Initializable {

    @FXML private Label lbl1;
    @FXML private Button btn1;
    @FXML private Button btn2;

    @FXML
    private Tab students;

    @FXML
    TableView<Student> studentTable;

    @FXML
    void studentTabSelected() {
        if (students.isSelected()) {

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Test_StudentDAOImpl stdi = new Test_StudentDAOImpl();

        stdi.createTest_StudentTable();
    }
}
