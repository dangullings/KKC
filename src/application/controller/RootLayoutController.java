package application.controller;

import application.Main;
import application.model.Student;
import application.util.StudentDAOImpl;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class RootLayoutController {

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
}
