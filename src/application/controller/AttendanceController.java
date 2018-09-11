package application.controller;

import application.model.ClassDate;
import application.model.ClassSession;
import application.model.Student;
import application.util.ClassDateDAOImpl;
import application.util.ClassSessionDAOImpl;
import application.util.StudentDAOImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.time.LocalDate;
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

    private ArrayList<CellItem> cellItems = new ArrayList<>();

    private int month;
    private int year;

    GridPane grid;

    @FXML
    BorderPane borderpane;

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

        grid.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->{
            e.getX();
            e.getY();
            //And if applicable
            e.getZ();
            mouseEntered(e);

            System.out.println("x"+e.getX() + " y"+e.getY());
        });
    }

    private void addPane(int colIndex, int rowIndex) {
        Pane pane = new Pane();
        pane.setOnMouseClicked(e -> {
            System.out.printf("Mouse enetered cell [%d, %d]%n", colIndex, rowIndex);
        });
        grid.add(pane, colIndex, rowIndex);
    }

    private void setupGrid(){
        if (grid != null)
            grid.getChildren().clear();

        grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(1);
        grid.setHgap(1);
        grid.setGridLinesVisible(true);

        RowConstraints row1 = new RowConstraints(150,150, Double.MAX_VALUE);
        row1.setVgrow(Priority.ALWAYS);
        row1.setValignment(VPos.CENTER);
        row1.setFillHeight(true);
        grid.getRowConstraints().add(row1);

        for (int i = 0; i < classDates.size(); i++){
            Label lblDate = new Label(classDates.get(i).getLocation() + " " + classDates.get(i).getDate().getMonthValue() + "/" + classDates.get(i).getDate().getDayOfMonth());
            lblDate.setStyle("-fx-font-color: black; -fx-font-size: 16; -fx-rotate: -85;");
            lblDate.setAlignment(Pos.CENTER_RIGHT);
            lblDate.setMinWidth(Region.USE_PREF_SIZE);
            lblDate.setMaxWidth(Region.USE_PREF_SIZE);

            GridPane.setFillHeight(lblDate, true);
            GridPane.setFillWidth(lblDate, true);

            GridPane.setConstraints(lblDate, (i+1), 0);
            grid.getChildren().add(lblDate);
        }

        ColumnConstraints col1 = new ColumnConstraints(100,100, 100);
        col1.setHalignment(HPos.LEFT);
        col1.setHgrow(Priority.ALWAYS) ; // allow row to grow
        col1.setFillWidth(true);
        grid.getColumnConstraints().add(col1);

        for (int i = 0; i < students.size(); i++){
            Label lblStudentName = new Label(students.get(i).getFirstName() + " " + students.get(i).getLastName().substring(0, 1));
            lblStudentName.setStyle("-fx-font-color: black; -fx-border-color: black; -fx-background-color: #FE9494; -fx-font-size: 16;");
            lblStudentName.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            GridPane.setConstraints(lblStudentName, 0, (i+1));
            grid.getChildren().add(lblStudentName);
        }

        ColumnConstraints col = new ColumnConstraints(40,40, 40);
        col.setHalignment(HPos.CENTER);
        RowConstraints row = new RowConstraints(40,40, 40);
        row.setValignment(VPos.CENTER);
        for (int i = 1; i < classDates.size()+1; i++){
            grid.getColumnConstraints().add(col);
            //grid.getRowConstraints().add(row);
            for (int j = 1; j < students.size()+1; j++){

                Pane pane = new Pane();
                int finalI = i;
                int finalJ = j;
                pane.setOnMouseEntered(e -> {
                    System.out.printf("cell [%d, %d]%n", finalI, finalJ);
                });

                HBox hbox = new HBox();
                hbox.setPadding(new Insets(3,2,2,2));
                hbox.setAlignment(Pos.CENTER);
                CheckBox checkBox = new CheckBox();
                hbox.getChildren().add(checkBox);

                if (classDates.get(i-1).hasSecondHour()){
                    CheckBox checkBox1 = new CheckBox();
                    hbox.getChildren().add(checkBox1);
                }

                pane.getChildren().add(hbox);
                //pane.setStyle("-fx-border-color: black; -fx-background-color: blue;");
                grid.add(pane, i, j);

                Student student = students.get(j-1);
                ClassDate classDate = classDates.get(i-1);

                CellItem cellItem = new CellItem(checkBox.isSelected(), i, j, classDate, student);
                cellItems.add(cellItem);
            }
        }

        borderpane.getChildren().add(grid);

        for (CellItem cellItem : cellItems){
            System.out.println(cellItem.toString());
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

    @FXML
    private void mouseEntered(MouseEvent e) {
        Node source = (Node)e.getSource() ;
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);
        System.out.println("col"+colIndex+" row"+rowIndex);
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

    public class CellItem{

        boolean isSelected;
        int col;
        int row;
        ClassDate classDate;
        Student student;

        public CellItem(boolean isSelected, int col, int row, ClassDate classDate, Student student) {
            this.isSelected = isSelected;
            this.col = col;
            this.row = row;
            this.classDate = classDate;
            this.student = student;
        }

        public CellItem(){

        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public int getCol() {
            return col;
        }

        public void setCol(int col) {
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public ClassDate getClassDate() {
            return classDate;
        }

        public void setClassDate(ClassDate classDate) {
            this.classDate = classDate;
        }

        public Student getStudent() {
            return student;
        }

        public void setStudent(Student student) {
            this.student = student;
        }

        @Override
        public String toString() {
            return "CellItem{" +
                    "isSelected=" + isSelected +
                    ", col=" + col +
                    ", row=" + row +
                    ", classDate=" + classDate.getDate() +
                    ", student=" + student.getFirstName() +
                    '}';
        }
    }
}
