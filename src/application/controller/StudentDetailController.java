package application.controller;

import application.model.Student;
import application.model.Test;
import application.model.Test_Student;
import application.util.DAO.StudentDAOImpl;
import application.util.DAO.TestDAOImpl;
import application.util.DAO.Test_StudentDAOImpl;
import application.util.StageLoader;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class StudentDetailController implements Initializable {

    @FXML TableView<Student.TestView> studentTestsTable;
    @FXML Label lblName;
    @FXML Label lblDOB;
    @FXML Label lblRank;
    @FXML Label lblClub;
    @FXML Label lblNumber;
    @FXML Label lblEmail;
    @FXML Button btnOk;
    @FXML ToggleButton toggleActive;

    private Student student;
    private Test test;

    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(Student student) {
        this.student = student;
        initStudentTableView();
        initTables();
        setUIData();
    }

    private void initStudentTableView(){
        Test_StudentDAOImpl test_studentDAO = new Test_StudentDAOImpl();

        ObservableList<Test> studentTests = test_studentDAO.selectAllObservable(student);
        ObservableList<Test_Student> studentTestScores = test_studentDAO.selectAllObservableScores(student);

        student.getTestViews().clear();
        student.setTestViews(studentTests, studentTestScores);
    }

    private void initTables(){
        TableColumn<Student.TestView, String> colTest = new TableColumn<>("Test");
        colTest.setMinWidth(95);
        colTest.setCellValueFactory(new PropertyValueFactory<>("rankName"));

        TableColumn<Student.TestView, LocalDate> colDate = new TableColumn<>("Date");
        colDate.setMinWidth(95);
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Student.TestView, String> colLocation = new TableColumn<>("Location");
        colLocation.setMinWidth(95);
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Student.TestView, String> colScores = new TableColumn<>("Scores");
        colScores.setMinWidth(120);

        TableColumn<Student.TestView, String> colForm = new TableColumn<>("Form");
        colForm.setMinWidth(60);
        colForm.setMaxWidth(60);
        colForm.setCellValueFactory(new PropertyValueFactory<>("form"));

        TableColumn<Student.TestView, String> colSteps = new TableColumn<>("Steps");
        colSteps.setMinWidth(60);
        colSteps.setMaxWidth(60);
        colSteps.setCellValueFactory(new PropertyValueFactory<>("steps"));

        TableColumn<Student.TestView, String> colPower = new TableColumn<>("Power");
        colPower.setMinWidth(60);
        colPower.setMaxWidth(60);
        colPower.setCellValueFactory(new PropertyValueFactory<>("power"));

        TableColumn<Student.TestView, String> colKiap = new TableColumn<>("Kiap");
        colKiap.setMinWidth(60);
        colKiap.setMaxWidth(60);
        colKiap.setCellValueFactory(new PropertyValueFactory<>("kiap"));

        TableColumn<Student.TestView, String> colQuestions = new TableColumn<>("Questions");
        colQuestions.setMinWidth(60);
        colQuestions.setMaxWidth(60);
        colQuestions.setCellValueFactory(new PropertyValueFactory<>("questions"));

        TableColumn<Student.TestView, String> colAttitude = new TableColumn<>("Attitude");
        colAttitude.setMinWidth(60);
        colAttitude.setMaxWidth(60);
        colAttitude.setCellValueFactory(new PropertyValueFactory<>("attitude"));

        TableColumn<Student.TestView, String> colSparring = new TableColumn<>("Sparring");
        colSparring.setMinWidth(60);
        colSparring.setMaxWidth(60);
        colSparring.setCellValueFactory(new PropertyValueFactory<>("sparring"));

        TableColumn<Student.TestView, String> colBreaking = new TableColumn<>("Breaking");
        colBreaking.setMinWidth(60);
        colBreaking.setMaxWidth(60);
        colBreaking.setCellValueFactory(new PropertyValueFactory<>("breaking"));

        colScores.getColumns().addAll(colForm, colSteps, colPower, colKiap, colQuestions, colAttitude, colSparring, colBreaking);

        studentTestsTable.setItems(student.getObservableTestViews());
        studentTestsTable.getColumns().addAll(colTest, colDate, colLocation, colScores);
        colDate.setSortType(TableColumn.SortType.DESCENDING);
        studentTestsTable.getSortOrder().setAll(colDate);

        studentTestsTable.setRowFactory( tv -> {
            TableRow<Student.TestView> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Student.TestView rowData = row.getItem();
                    loadTestDetail(rowData.getTestId());
                }
            });
            return row ;
        });

        if (test != null) {
            Student.TestView focusTest = new Student.TestView();

            for (Student.TestView testView : student.getObservableTestViews()) {
                if (testView.getTestId() == test.getId()) {
                    focusTest = testView;
                    break;
                }
            }

            studentTestsTable.getSelectionModel().select(focusTest);
            studentTestsTable.scrollTo(focusTest);
        }
    }

    private void loadTestDetail(int testId){
        TestDAOImpl testDAO = new TestDAOImpl();
        test = testDAO.selectById(testId);

        TestDetailController controller = StageLoader.loadStage("view/TestDetail.fxml", "Test Detail").getController();
        controller.setStudent(student);
        controller.initData(test);
    }

    private void setUIData(){
        lblName.setText(student.getFirstName() + " " + student.getLastName());
        lblDOB.setText("DOB: " + student.getBirthDate().toString());

        String rankDetail = "";
        if (student.getRankValue() == 11){
            rankDetail = "[" + student.getRankName() + "]";
        } else if ((student.getRankValue() >= 13) && (student.getRankValue() < 15)){
            rankDetail = "[" + student.getRankName() + "]";
        } else if ((student.getRankValue() >= 16) && (student.getRankValue() < 19)){
            rankDetail = "[" + student.getRankName() + "]";
        } else if ((student.getRankValue() >= 20) && (student.getRankValue() < 24)){
            rankDetail = "[" + student.getRankName() + "]";
        }

        lblRank.setText("Rank: " + student.getRankNameRounded() + " " + rankDetail);
        lblClub.setText("Club: " + student.getClub());
        lblNumber.setText("Phone Number: " + student.getNumber());
        lblEmail.setText("Email: " + student.getEmail());

        if (student.getActive()){
            toggleActive.setStyle("-fx-base: #A1B56C;");
            toggleActive.setText("Active");
        }else{
            toggleActive.setStyle("-fx-base: #AB4642;");
            toggleActive.setText("Inactive");
        }
        toggleActive.setSelected(student.getActive());
    }

    public void setTest(Test test){
        this.test = test;
    }

    public void pressOk(){
        Stage stage = (Stage) btnOk.getScene().getWindow();
        stage.close();
        StudentController.getInstance().updateStudentTable();
        RootLayoutController.getInstance().borderPane.setEffect(null);
    }

    public void pressActive(){
        StudentDAOImpl studentDAO = new StudentDAOImpl();

        student.setActive(toggleActive.isSelected());
        studentDAO.update(student, student.getId());

        if (student.getActive()){
            toggleActive.setStyle("-fx-base: #A1B56C;");
            toggleActive.setText("Active");
        }else{
            toggleActive.setStyle("-fx-base: #AB4642;");
            toggleActive.setText("Inactive");
        }
    }
}
