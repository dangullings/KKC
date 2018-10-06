package application.controller;

import application.model.Attendance;
import application.model.ClassDate;
import application.model.ClassSession;
import application.model.Student;
import application.util.AttendanceDAOImpl;
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
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class AttendanceController implements Initializable{

    private static AttendanceController instance;

    public AttendanceController(){
        instance = this;
    }

    public static AttendanceController getInstance(){
        return instance;
    }

    private ClassSessionDAOImpl classSessionDAO = new ClassSessionDAOImpl();
    private ClassDateDAOImpl classDateDAO = new ClassDateDAOImpl();
    private AttendanceDAOImpl attendanceDAO = new AttendanceDAOImpl();

    private StudentDAOImpl studentDAO = new StudentDAOImpl();
    private List<Student> students;

    private List<ClassDate> classDates;

    private ArrayList<CellItem> cellItems = new ArrayList<>();

    private int month;
    private int year;

    private GridPane grid;

    @FXML Label lblHeader;

    @FXML
    AnchorPane scrollPaneAnchor;

    @FXML
    ScrollPane scrollPane;

    @FXML
    BorderPane borderpane;

    @FXML
    Spinner<Integer> spinnerYear;
    @FXML
    Spinner<Integer> spinnerMonth;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scrollPaneAnchor.setStyle("-fx-background-color: #e0e0e0");
        scrollPane.setStyle("-fx-background-color: #ffffff");
        init();
    }

    public void init(){
        SpinnerValueFactory<Integer> yearValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2000, 2100, LocalDate.now().getYear());
        spinnerYear.setValueFactory(yearValueFactory);
        year = LocalDate.now().getYear();

        SpinnerValueFactory<Integer> monthValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, LocalDate.now().getMonthValue());
        spinnerMonth.setValueFactory(monthValueFactory);
        month = LocalDate.now().getMonthValue();

        students = studentDAO.selectAllActive();

        Collections.sort(students);
        Collections.reverse(students);

        setupDates();
        setupGrid();
    }

    private void setupGrid(){
        if (grid != null)
            grid.getChildren().clear();

        final int BORDER_WIDTH = 695;
        final int BORDER_HEIGHT = 720;

        grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(1);
        grid.setHgap(1);
        grid.setGridLinesVisible(true);
        grid.setPrefSize(BORDER_WIDTH, BORDER_HEIGHT);

        RowConstraints row1 = new RowConstraints(150,150, 150);
        row1.setVgrow(Priority.ALWAYS);
        row1.setValignment(VPos.CENTER);
        row1.setFillHeight(true);
        grid.getRowConstraints().add(row1);

        for (int i = 0; i < classDates.size(); i++){
            Label lblDate = new Label(classDates.get(i).getLocation() + " " + classDates.get(i).getDate().getMonthValue() + "/" + classDates.get(i).getDate().getDayOfMonth());
            if (classDates.get(i).getDate().equals(classSessionDAO.selectById(classDates.get(i).getSessionId()).getStartDate())){
                classDates.get(i).setStyle("-fx-font-color: black; -fx-font-size: 20; -fx-background-color: #147800; font-weight: bold; -fx-rotate: -90;");
            }else if (classDates.get(i).getDate().equals(classSessionDAO.selectById(classDates.get(i).getSessionId()).getEndDate())){
                classDates.get(i).setStyle("-fx-font-color: black; -fx-font-size: 20; -fx-background-color: #E30101; font-weight: bold; -fx-rotate: -90;");
            }else{
                classDates.get(i).setStyle("-fx-font-color: black; -fx-font-size: 20; font-weight: bold; -fx-rotate: -90;");
            }
            lblDate.setStyle(classDates.get(i).getStyle());
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
            lblStudentName.setStyle(students.get(i).getStyleDark());
            lblStudentName.setPrefSize(150,40);

            GridPane.setConstraints(lblStudentName, 0, (i+1));
            grid.getChildren().add(lblStudentName);
        }

        ColumnConstraints col = new ColumnConstraints(40,40, 40);
        col.setHalignment(HPos.CENTER);
        RowConstraints row = new RowConstraints(40,40, 40);
        row.setValignment(VPos.CENTER);

        for (int i = 1; i < classDates.size()+1; i++){
            grid.getColumnConstraints().add(col);
            for (int j = 1; j < students.size()+1; j++){

                HBox hbox = new HBox();
                hbox.setPrefSize(10,10);
                hbox.setMaxSize(40,40);
                hbox.setSpacing(6);

                if (j % 2 != 0){
                    hbox.setStyle(students.get(j-1).getStyleLight());
                }

                hbox.setAlignment(Pos.CENTER);

                Attendance attendance = attendanceDAO.selectByClassDateIdAndStudentId(classDates.get(i-1).getId(), students.get(j-1).getId());

                if (attendance.getId() == -1){ // record does not exist, so create record for this student and date
                    attendance.setStudentId(students.get(j-1).getId());
                    attendance.setClassDateId(classDates.get(i-1).getId());
                    attendanceDAO.insert(attendance);
                }

                if (classDates.get(i-1).hasSecondHour()){
                    CellItem cellItem = new CellItem(false, attendance.isFirstHour(), attendance.isSecondHour(), i, j, classDates.get(i-1), students.get(j-1), attendance);
                    CellItem cellItem1 = new CellItem(true, attendance.isFirstHour(), attendance.isSecondHour(), i, j, classDates.get(i-1), students.get(j-1), attendance);
                    cellItem.setPrefSize(25,20);
                    cellItem1.setPrefSize(25,20);
                    cellItem.setMaxSize(25,20);
                    cellItem1.setMaxSize(25,20);

                    Line line = new Line();
                    line.setStrokeWidth(2);
                    line.setStartX(38.0f);
                    line.setStartY(0.0f);
                    line.setEndX(0.0f);
                    line.setEndY(38.0f);

                    hbox.getChildren().addAll(cellItem, cellItem1);

                    hbox.setMargin(cellItem, new Insets(0, 0, 16, 0));
                    hbox.setMargin(cellItem1, new Insets(16, 0, 0, 0));

                    grid.add(hbox, i, j);
                    grid.add(line, i, j);
                    cellItems.add(cellItem);
                    cellItems.add(cellItem1);
                }else {
                    CellItem cellItem = new CellItem(false, attendance.isFirstHour(), attendance.isSecondHour(), i, j, classDates.get(i-1), students.get(j-1), attendance);
                    cellItem.setPrefSize(34,34);
                    cellItem.setMaxSize(34,34);
                    hbox.getChildren().add(cellItem);
                    grid.add(hbox, i, j);
                    cellItems.add(cellItem);
                }
            }
        }

        scrollPane.setContent(grid);
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

        if (!classDates.isEmpty()) {
            lblHeader.setText("Attendance for " + classDates.get(0).getDate().getMonth() + " " + classDates.get(0).getDate().getYear());
        }else{
            lblHeader.setText("Attendance");
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

    public class CellItem extends Pane{

        boolean isFirstHourSelected;
        boolean isSecondHourSelected;
        boolean isSecondHourItem;
        int col;
        int row;
        String style;
        String highlightStyle;
        ClassDate classDate;
        Student student;
        Attendance attendance;

        private CellItem(boolean isSecondHourItem, boolean isFirstHourSelected, boolean isSecondHourSelected, int col, int row, ClassDate classDate, Student student, Attendance attendance) {
            this.isSecondHourItem = isSecondHourItem;
            this.isFirstHourSelected = isFirstHourSelected;
            this.isSecondHourSelected = isSecondHourSelected;
            this.col = col;
            this.row = row;
            this.classDate = classDate;
            this.student = student;
            this.attendance = attendance;
            this.style = this.getStyle();
            this.highlightStyle = this.getStyle() + " -fx-background-color:#C8AE01;";

            loadMark();

            //setOnMouseMoved(e -> {
            //    doMoveEvent();
            //});

            setOnMouseClicked(e -> {
                doEvent();
            });
        }

        private void doMoveEvent(){
            for (Node node : grid.getChildren()) {
                node.setOnMouseEntered(e -> grid.getChildren().forEach(c -> {
                    Integer targetIndex = GridPane.getColumnIndex(node);
                    if (GridPane.getColumnIndex(c) == targetIndex) {
                        c.setStyle(highlightStyle);
                    }
                }));
                node.setOnMouseExited(e -> grid.getChildren().forEach(c -> {
                    Integer targetIndex = GridPane.getColumnIndex(node);
                    if (GridPane.getColumnIndex(c) == targetIndex) {
                        c.setStyle(style);
                    }
                }));
            }
        }

        private void doEvent(){
            if (classDate.getDate().isAfter(LocalDate.now())){
                return;
            }

            double v = 30;
            double k = 5;

            setFirstHourSelected(attendance.isFirstHour());
            setSecondHourSelected(attendance.isSecondHour());

            if (isSecondHourItem()){
                if (isSecondHourSelected()){
                    this.getChildren().clear();
                    setSecondHourSelected(false);
                }else{
                    drawMark(15, 0);
                    setSecondHourSelected(true);
                }
            }else{
                if (isFirstHourSelected()){
                    this.getChildren().clear();
                    setFirstHourSelected(false);
                }else{
                    if (classDate.hasSecondHour()){
                        v = 15;
                        k = 0;
                    }
                    drawMark(v, k);
                    setFirstHourSelected(true);
                }
            }

            if ((!isFirstHourSelected) && (!isSecondHourSelected)){
                //attendanceDAO.deleteById(attendance.getId());
                //return;
            }


            attendance.setFirstHour(isFirstHourSelected());
            attendance.setSecondHour(isSecondHourSelected());

            attendanceDAO.update(attendance, attendance.getId());
        }

        private void loadMark(){
            double v = 30;
            double k = 5;

            if (isSecondHourItem){
                if (!isSecondHourSelected){
                    this.getChildren().clear();
                }else{
                    drawMark(15, 0);
                }
            }else{
                if (!isFirstHourSelected){
                    this.getChildren().clear();
                }else{
                    if (classDate.hasSecondHour()){
                        v = 15;
                        k = 0;
                    }
                    drawMark(v, k);
                }
            }
        }

        private void drawMark(double v, double k){
            Line line = new Line();
            line.setStrokeWidth(3);
            line.setStartX(k);
            line.setStartY(k);
            line.setEndX(v);
            line.setEndY(v);
            Line linex = new Line();
            linex.setStrokeWidth(3);
            linex.setStartX(v);
            linex.setStartY(k);
            linex.setEndX(k);
            linex.setEndY(v);

            this.getChildren().add(line);
            this.getChildren().add(linex);
        }

        public CellItem(){

        }

        public boolean isSecondHourItem() {
            return isSecondHourItem;
        }

        public void setSecondHourItem(boolean secondHourItem) {
            isSecondHourItem = secondHourItem;
        }

        public boolean isFirstHourSelected() {
            return isFirstHourSelected;
        }

        public void setFirstHourSelected(boolean firstHourSelected) {
            isFirstHourSelected = firstHourSelected;
        }

        public boolean isSecondHourSelected() {
            return isSecondHourSelected;
        }

        public void setSecondHourSelected(boolean secondHourSelected) {
            isSecondHourSelected = secondHourSelected;
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
                    "isSelected=" + isFirstHourSelected +
                    "isSelected=" + isSecondHourSelected +
                    ", col=" + col +
                    ", row=" + row +
                    ", classDate=" + classDate.getDate() +
                    ", student=" + student.getFirstName() +
                    '}';
        }
    }
}
