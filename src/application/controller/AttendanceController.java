package application.controller;

import application.model.*;
import application.util.DAO.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
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
    private boolean monthComplete;

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

    @FXML
    Button btnFinalizeAttendance;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //scrollPaneAnchor.setStyle("-fx-background-color: #e0e0e0");
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

        Label lblTotal = new Label("Total");
        if (!classDates.isEmpty())
            lblTotal.setStyle("-fx-font-color: black; -fx-font-size: 20; font-weight: bold; -fx-rotate: -90;");
        lblTotal.setAlignment(Pos.CENTER_RIGHT);
        lblTotal.setMinWidth(Region.USE_PREF_SIZE);
        lblTotal.setMaxWidth(Region.USE_PREF_SIZE);
        GridPane.setFillHeight(lblTotal, true);
        GridPane.setFillWidth(lblTotal, true);
        GridPane.setConstraints(lblTotal, (classDates.size()+1), 0);
        grid.getChildren().add(lblTotal);

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
            RowConstraints row = new RowConstraints(40,40, 40);
            row.setValignment(VPos.CENTER);
            grid.getRowConstraints().add(row);

            grid.getChildren().add(lblStudentName);
        }

        ColumnConstraints col = new ColumnConstraints(40,40, 40);
        col.setHalignment(HPos.CENTER);
        RowConstraints row = new RowConstraints(40,40, 40);
        row.setValignment(VPos.CENTER);

        cellItems.clear();

        if (!classDates.isEmpty()) {
            for (int i = 1; i <= classDates.size() + 1; i++) {
                grid.getColumnConstraints().add(col);

                for (int j = 1; j < students.size() + 1; j++) {

                    if (i == (classDates.size() + 1)) {
                        CellItem cellItem = new CellItem(false, false, false, i, j, classDates.get(i - 2), students.get(j - 1), null);
                        grid.add(cellItem, i, j);
                        cellItems.add(cellItem);
                        continue;
                    }

                    HBox hbox = new HBox();
                    hbox.setPrefSize(10, 10);
                    hbox.setMaxSize(40, 40);
                    hbox.setSpacing(6);

                    if (j % 2 != 0) {
                        hbox.setStyle(students.get(j - 1).getStyleLight());
                    }

                    hbox.setAlignment(Pos.CENTER);

                    Attendance attendance = attendanceDAO.selectByClassDateIdAndStudentId(classDates.get(i - 1).getId(), students.get(j - 1).getId());

                    if (attendance.getId() == -1) { // record does not exist, so create record for this student and date
                        attendance.setStudentId(students.get(j - 1).getId());
                        attendance.setClassDateId(classDates.get(i - 1).getId());
                        attendanceDAO.insert(attendance);
                    }

                    if (classDates.get(i - 1).hasSecondHour()) {
                        CellItem cellItem = new CellItem(false, attendance.isFirstHour(), attendance.isSecondHour(), i, j, classDates.get(i - 1), students.get(j - 1), attendance);
                        CellItem cellItem1 = new CellItem(true, attendance.isFirstHour(), attendance.isSecondHour(), i, j, classDates.get(i - 1), students.get(j - 1), attendance);
                        cellItem.setPrefSize(25, 20);
                        cellItem1.setPrefSize(25, 20);
                        cellItem.setMaxSize(25, 20);
                        cellItem1.setMaxSize(25, 20);

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
                    } else {
                        CellItem cellItem = new CellItem(false, attendance.isFirstHour(), attendance.isSecondHour(), i, j, classDates.get(i - 1), students.get(j - 1), attendance);
                        cellItem.setPrefSize(34, 34);
                        cellItem.setMaxSize(34, 34);
                        hbox.getChildren().add(cellItem);
                        grid.add(hbox, i, j);
                        cellItems.add(cellItem);
                    }
                }
            }
        }

        tallyAttendance();

        scrollPane.setContent(grid);
    }

    private void setBtnFinalizeAttendanceEnabled(){
        btnFinalizeAttendance.setDisable(true);
        btnFinalizeAttendance.setText("Finalize Now");

        if (classDates.isEmpty())
            return;

        if (classDates.get(0).getComplete()){
            btnFinalizeAttendance.setDisable(true);
            btnFinalizeAttendance.setText("Finalized");

            DropShadow glow = new DropShadow();

            grid.setEffect(glow);

            return;
        }else{
            btnFinalizeAttendance.setDisable(false);
        }

        LocalDate date = LocalDate.of(year,month,28);
        date = date.with(TemporalAdjusters.lastDayOfMonth());

        if (LocalDate.now().isAfter(classDates.get(classDates.size()-1).getDate()) || (LocalDate.now().isEqual(classDates.get(classDates.size()-1).getDate()))){
            btnFinalizeAttendance.setDisable(false);
            btnFinalizeAttendance.setText("Finalize Now (will auto finalize "+date.plusWeeks(1)+")");
        }else{
            btnFinalizeAttendance.setDisable(true);
        }

        if (LocalDate.now().isAfter(date.plusWeeks(1))){
            pressFinalizeAttendance();
        }
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if ((Label.class == node.getClass()) && (GridPane.getColumnIndex(node) == (classDates.size()+1))){
                //Label lblTotal = (Label)node;
                //lblTotal.setText("2");
            }

            //if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
            //    return node;
            //}
        }
        return null;
    }

    private void setupDates(){
        AttendanceDAOImpl attendanceDAO = new AttendanceDAOImpl();

        LocalDate startDate = LocalDate.of(year, month, 1);
        int lastDay = startDate.lengthOfMonth();
        LocalDate endDate = LocalDate.of(year, month, lastDay);

        classDates = classDateDAO.selectAllByDate(startDate, endDate);

        ArrayList<Attendance> attendances = new ArrayList<>();

        for (ClassDate classDate : classDates){
            attendances.addAll(attendanceDAO.selectAllByClassDateId(classDate.getId()));
        }

        Collections.sort(classDates);

        for (ClassDate classDate : classDates){
            classDate.setLocation(classSessionDAO.selectById(classDate.getSessionId()).getLocation());
        }

        if (!classDates.isEmpty()) {
            lblHeader.setText("Attendance for " + classDates.get(0).getDate().getMonth() + " " + classDates.get(0).getDate().getYear());
        }else{
            lblHeader.setText("Attendance");
        }

        students.clear();

        ArrayList<Integer> studentIds = new ArrayList<>();
        Student student;

        if ((LocalDate.now().getYear() == year) && (LocalDate.now().getMonthValue() <= month)){
            students = studentDAO.selectAllActive();
        }else{
            for (Attendance attendance : attendances){
                student = studentDAO.selectById(attendance.getStudentId());
                if (!studentIds.contains(student.getId())) {
                    studentIds.add(student.getId());
                    students.add(student);
                }
            }
        }

        Collections.sort(students);
        Collections.reverse(students);

        monthComplete = false;
        if (!classDates.isEmpty()) {
            if (classDates.get(0).getComplete()) {
                monthComplete = true;
            }
        }

        setupGrid();

        setBtnFinalizeAttendanceEnabled();
    }

    @FXML void pressFinalizeAttendance(){
        ClassDateDAOImpl classDateDAO = new ClassDateDAOImpl();
        DemoPointDAO demoPointDAO = new DemoPointDAO();

        for (ClassDate classDate : classDates){
            classDate.setComplete(true);
            classDateDAO.update(classDate, classDate.getId());
        }

        DemoPointAwardedDAO demoPointAwardedDAO = new DemoPointAwardedDAO();
        DemoPointAwarded demoPointAwarded;

        int most = 0;
        int bestStudentId = 0;
        double totalClasses = 0;

        for (ClassDate classDate : classDates){
            totalClasses++;

            if (classDate.hasSecondHour())
                totalClasses++;
        }

        for (Student student : students){
            double i = 0;

            i = ((double)student.getTally() / totalClasses);

            if (i >= .80){
                demoPointAwarded = new DemoPointAwarded(student.getId(), demoPointDAO.selectById(1).getName(), classDates.get(0).getDate().getMonth().toString().substring(0,3) + " avg 5x week", demoPointDAO.selectById(1).getValue());
                demoPointAwardedDAO.insert(demoPointAwarded);
            }else if (i >= .65){
                demoPointAwarded = new DemoPointAwarded(student.getId(), demoPointDAO.selectById(2).getName(), classDates.get(0).getDate().getMonth().toString().substring(0,3) + " avg 4x week", demoPointDAO.selectById(2).getValue());
                demoPointAwardedDAO.insert(demoPointAwarded);
            }

            if (i == 1){
                demoPointAwarded = new DemoPointAwarded(student.getId(), demoPointDAO.selectById(7).getName(), classDates.get(0).getDate().getMonth().toString().substring(0,3) + " perfect attendance", demoPointDAO.selectById(7).getValue());
                demoPointAwardedDAO.insert(demoPointAwarded);
            }

            if (student.getTally() > most){
                most = student.getTally();
                bestStudentId = student.getId();
            }
        }

        demoPointAwarded = new DemoPointAwarded(bestStudentId, demoPointDAO.selectById(3).getName(), classDates.get(0).getDate().getMonth().toString().substring(0,3) + " most classes", demoPointDAO.selectById(3).getValue());
        demoPointAwardedDAO.insert(demoPointAwarded);

        calculateYearAttendance();

        btnFinalizeAttendance.setDisable(true);
    }

    private void tallyAttendance(){
        CellItem first, second;

        for (int j = 1; j <= students.size(); j++) {
            int studentTally = 0;
            for (int i = 1; i <= classDates.size(); i++) {
                DoubleCellItem doubleCellItem = getDoubleCellItemByColRow(i, j);
                first = doubleCellItem.first;
                second = doubleCellItem.second;

                first.total = 0;
                if (first.isFirstHourSelected)
                    first.total++;

                if (second != null){
                    second.total = 0;
                    if (second.isSecondHourSelected) {
                        second.total++;
                    }
                }

                studentTally += first.total;
                if (second != null){
                    studentTally += second.total;
                }
            }

            CellItem cellItem = getCellItemByColRow(classDates.size()+1, j);

            if ((cellItem != null) && (cellItem.attendance == null)) {
                //cellItem.lblTotal.setStyle("-fx-font-color: black; -fx-font-size: 28;-fx-background-color:#D28FFE;");
                cellItem.lblTotal.setText(""+studentTally);
            }

            students.get(j-1).setTally(studentTally);
        }
    }

    private void calculateYearAttendance(){
        DemoPointDAO demoPointDAO = new DemoPointDAO();
        DemoPointAwardedDAO demoPointAwardedDAO = new DemoPointAwardedDAO();
        DemoPointAwarded demoPointAwarded;
        List<Student> students = this.students;
        List<ClassDate> classDates;
        AttendanceDAOImpl attendanceDAO = new AttendanceDAOImpl();

        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        classDates = classDateDAO.selectAllByDate(startDate, endDate);

        ArrayList<Attendance> attendances = new ArrayList<>();

        for (ClassDate classDate : classDates){
            attendances.addAll(attendanceDAO.selectAllByClassDateId(classDate.getId()));
        }

        for (Student student : students){
            student.setTally(0);
        }

        for (Attendance attendance : attendances){
            if (attendance.isFirstHour()) {
                for (Student student : students){
                    if (student.getId() == attendance.getStudentId()){
                        student.incTally(1);
                        break;
                    }
                }
            }
            if (attendance.isSecondHour()) {
                for (Student student : students){
                    if (student.getId() == attendance.getStudentId()){
                        student.incTally(1);
                        break;
                    }
                }
            }
        }

        Collections.sort(students, Student.BY_TALLY);

        if (!students.isEmpty()) {
            demoPointAwarded = new DemoPointAwarded(students.get(0).getId(), demoPointDAO.selectById(4).getName()+"("+students.get(0).getTally()+")", Integer.toString(year) + " Most Classes", demoPointDAO.selectById(4).getValue());
            demoPointAwardedDAO.insert(demoPointAwarded);

            if (students.size() > 1) {
                demoPointAwarded = new DemoPointAwarded(students.get(1).getId(), demoPointDAO.selectById(5).getName() + "(" + students.get(1).getTally() + ")", Integer.toString(year) + " 2nd Most Classes", demoPointDAO.selectById(5).getValue());
                demoPointAwardedDAO.insert(demoPointAwarded);
            }

            if (students.size() > 2) {
                demoPointAwarded = new DemoPointAwarded(students.get(2).getId(), demoPointDAO.selectById(6).getName() + "(" + students.get(2).getTally() + ")", Integer.toString(year) + " 3rd Most Classes", demoPointDAO.selectById(6).getValue());
                demoPointAwardedDAO.insert(demoPointAwarded);
            }
        }
    }

    public void changeYear(){
        year = spinnerYear.getValue();
        setupDates();
    }

    public void changeMonth(){
        month = spinnerMonth.getValue();
        setupDates();
        getNodeFromGridPane(grid,classDates.size(),students.size());
    }

    private class DoubleCellItem{
        CellItem first, second;

        DoubleCellItem(CellItem first, CellItem second){
            this.first = first;
            this.second = second;
        }
    }

    private CellItem getCellItemByColRow(int col, int row){
        for (CellItem cellItem : cellItems){
            if ((cellItem.getCol() == col) && (cellItem.getRow() == row)){
                    return cellItem;
            }
        }

        return null;
    }

    private DoubleCellItem getDoubleCellItemByColRow(int col, int row){
        for (CellItem cellItem : cellItems){
            if ((cellItem.getCol() == col) && (cellItem.getRow() == row)){
                if (cellItem.attendance == null)
                    break;

                if (cellItem.attendance.isSecondHour()){
                    return new DoubleCellItem(cellItem, cellItems.get(cellItems.indexOf(cellItem)+1));
                }else{
                    return new DoubleCellItem(cellItem, null);
                }
            }
        }

        return null;
    }

    public class CellItem extends Pane{

        boolean isFirstHourSelected;
        boolean isSecondHourSelected;
        boolean isSecondHourItem;
        int col;
        int row;
        int total;
        String style;
        String highlightStyle;
        ClassDate classDate;
        Student student;
        Attendance attendance;
        Label lblTotal = new Label();

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
            this.highlightStyle = this.getStyle() + "-fx-background-color:#C8AE01;";

            if (attendance == null) {
                lblTotal.setStyle("-fx-font-color: black; -fx-font-size: 28;");
                lblTotal.setAlignment(Pos.CENTER);
                lblTotal.setMinWidth(40);
                this.getChildren().add(lblTotal);
            }

            loadMark();

            //setOnMouseMoved(e -> {
            //    doMoveEvent();
            //});

            setOnMouseClicked(e -> {
                if (!monthComplete) {
                    doEvent();
                    tallyAttendance();
                }
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
            if (col == classDates.size()+1){
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
            if (col == classDates.size()+1){
                return;
            }

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
