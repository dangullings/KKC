package application.controller;

import application.LOCATION;
import application.model.Student;
import application.util.StudentDAOImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;

public class AttendanceController implements Initializable{

    private StudentDAOImpl studentDAO = new StudentDAOImpl();
    private List<Student> students;

    @FXML
    GridPane grid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        students = studentDAO.selectAllActive();

        grid.setAlignment(Pos.CENTER);
        grid.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        grid.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        LocalDate date = LocalDate.now();

        ColumnConstraints col1 = new ColumnConstraints(100,100, 100);
        col1.setHalignment(HPos.LEFT);
        col1.setHgrow(Priority.ALWAYS) ; // allow row to grow
        col1.setFillWidth(true);
        grid.getColumnConstraints().clear();
        grid.getColumnConstraints().add(col1);

        RowConstraints row1 = new RowConstraints(150,150, Double.MAX_VALUE);
        row1.setVgrow(Priority.ALWAYS);
        row1.setValignment(VPos.CENTER);
        //row1.setFillHeight(true);
        grid.getRowConstraints().clear();
        grid.getRowConstraints().add(row1);

        for (int i = 1; i < 10; i++){
            date = date.plus(7, ChronoUnit.DAYS);
            Label lblDate = new Label(LOCATION.Waconia + " " + date.getMonthValue() + "/" + date.getDayOfMonth());
            lblDate.setStyle("-fx-font-color: black; -fx-font-size: 16; -fx-rotate: -85;");
            lblDate.setAlignment(Pos.CENTER_RIGHT);
            lblDate.setMinWidth(Region.USE_PREF_SIZE);
            lblDate.setMaxWidth(Region.USE_PREF_SIZE);
            grid.addColumn(i, lblDate);
        }

        for (int i = 1; i < students.size(); i++){
            Label lblStudentName = new Label(students.get(i).getFirstName() + " " + students.get(i).getLastName().substring(0, 1));
            lblStudentName.setStyle("-fx-font-color: black; -fx-border-color: black; -fx-background-color: #FE9494; -fx-font-size: 16;");
            lblStudentName.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            grid.addRow(i, lblStudentName);
        }

        ColumnConstraints col = new ColumnConstraints(40,40, 40);
        col.setHalignment(HPos.CENTER);
        RowConstraints row = new RowConstraints(40,40, 40);
        row.setValignment(VPos.CENTER);
        for (int i = 1; i < 10; i++){
            grid.getColumnConstraints().add(col);
            //grid.getRowConstraints().add(row);
            for (int j = 1; j < students.size(); j++){
                CheckBox checkBox = new CheckBox();
                grid.add(checkBox, i, j);
            }
        }
    }
}
