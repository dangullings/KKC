package application.controller;

import application.LOCATION;
import application.model.ClassDate;
import application.model.ClassSession;
import application.model.Student;
import application.util.ClassDateDAOImpl;
import application.util.ClassSessionDAOImpl;
import application.util.StudentDAOImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class AttendanceController implements Initializable{

    private ClassSessionDAOImpl classSessionDAO = new ClassSessionDAOImpl();
    private ClassDateDAOImpl classDateDAO = new ClassDateDAOImpl();

    private StudentDAOImpl studentDAO = new StudentDAOImpl();
    private List<Student> students;

    private List<ClassSession> classSessions;
    private List<ClassDate> classDates;

    private int month;
    private int year;

    @FXML
    GridPane grid;

    @FXML
    Spinner<Integer> spinnerYear;
    @FXML
    Spinner<Integer> spinnerMonth;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SpinnerValueFactory<Integer> yearValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2017, 2100, LocalDate.now().getYear());
        spinnerYear.setValueFactory(yearValueFactory);
        year = LocalDate.now().getYear();

        SpinnerValueFactory<Integer> monthValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, LocalDate.now().getMonthValue());
        spinnerMonth.setValueFactory(monthValueFactory);
        month = LocalDate.now().getMonthValue();

        students = studentDAO.selectAllActive();

        setupDates();
        setupGrid();
    }

    private void setupGrid(){
        //grid = new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        grid.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

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

        for (ClassDate classDate : classDates){
            Label lblDate = new Label(classDate.getLocation() + " " + classDate.getDate().getMonthValue() + "/" + classDate.getDate().getDayOfMonth());
            lblDate.setStyle("-fx-font-color: black; -fx-font-size: 16; -fx-rotate: -85;");
            lblDate.setAlignment(Pos.CENTER_RIGHT);
            lblDate.setMinWidth(Region.USE_PREF_SIZE);
            lblDate.setMaxWidth(Region.USE_PREF_SIZE);
            grid.addColumn(classDates.indexOf(classDate)+1, lblDate);
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
        for (int i = 1; i < classDates.size(); i++){
            grid.getColumnConstraints().add(col);
            //grid.getRowConstraints().add(row);
            for (int j = 1; j < students.size(); j++){
                CheckBox checkBox = new CheckBox();
                grid.add(checkBox, i, j);
            }
        }
    }

    private void setupDates(){
        LocalDate startDate = LocalDate.of(year, month, 1);
        int lastDay = startDate.lengthOfMonth();
        LocalDate endDate = LocalDate.of(year, month, lastDay);

        classDates = classDateDAO.selectAllByDate(startDate, endDate);

        Collections.sort(classDates);

        for (ClassDate classDate : classDates){
            classDate.setLocation(classSessionDAO.selectById(classDate.getSessionId()).getLocation());
        }

        setupGrid();
    }

    public void changeYear(){
        year = spinnerYear.getValue();
        setupDates();
    }

    public void changeMonth(){
        month = spinnerMonth.getValue();
        setupDates();
    }

    private int getRowCount(GridPane pane) {
        int numRows = pane.getRowConstraints().size();
        for (int i = 0; i < pane.getChildren().size(); i++) {
            Node child = pane.getChildren().get(i);
            if (child.isManaged()) {
                Integer rowIndex = GridPane.getRowIndex(child);
                if(rowIndex != null){
                    numRows = Math.max(numRows,rowIndex+1);
                }
            }
        }
        return numRows;
    }

    private int getColumnCount(GridPane pane) {
        int numColumns = pane.getRowConstraints().size();
        for (int i = 0; i < pane.getChildren().size(); i++) {
            Node child = pane.getChildren().get(i);
            if (child.isManaged()) {
                Integer columnIndex = GridPane.getColumnIndex(child);
                if(columnIndex != null){
                    numColumns = Math.max(numColumns,columnIndex+1);
                }
            }
        }
        return numColumns;
    }
}
