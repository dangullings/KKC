package application.controller;

import application.model.*;
import application.util.DAO.*;
import application.util.GraphicTools;
import application.util.StageLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class StudentDetailController implements Initializable {

    @FXML TableView<Student.TestView> studentTestsTable;
    @FXML TableView<DemoPointAwarded> studentDemoPointsTable;
    @FXML TableView<Order> studentOrderTable;
    @FXML Label lblName;
    @FXML Label lblDOB;
    @FXML Label lblRank;
    @FXML Label lblDemoPoints;
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
        colTest.setMinWidth(99);
        colTest.setCellValueFactory(new PropertyValueFactory<>("rankName"));

        TableColumn<Student.TestView, LocalDate> colDate = new TableColumn<>("Date");
        colDate.setMinWidth(98);
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Student.TestView, String> colLocation = new TableColumn<>("Location");
        colLocation.setMinWidth(98);
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
        studentTestsTable.setPlaceholder(new Label("No Tests Taken"));

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


        TableColumn<DemoPointAwarded, String> colName = new TableColumn<>("Name");
        colName.setMinWidth(185);
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<DemoPointAwarded, String> colInfo = new TableColumn<>("Info");
        colInfo.setMinWidth(175);
        colInfo.setCellValueFactory(new PropertyValueFactory<>("info"));

        TableColumn<DemoPointAwarded, String> colValue = new TableColumn<>("Value");
        colValue.setMinWidth(40);
        colValue.setMaxWidth(40);
        colValue.setCellValueFactory(new PropertyValueFactory<>("value"));

        studentDemoPointsTable.getColumns().addAll(colName, colInfo, colValue);

        DemoPointAwardedDAO demoPointAwardedDAO = new DemoPointAwardedDAO();
        ObservableList<DemoPointAwarded> demoPointsAwarded = demoPointAwardedDAO.selectAllObservableByStudentId(student.getId());
        studentDemoPointsTable.setItems(demoPointsAwarded);
        studentDemoPointsTable.setPlaceholder(new Label("No Demo Team Points Awarded"));




        TableColumn<Order, Integer> colNumber = new TableColumn<>("id");
        colNumber.setMinWidth(50);
        colNumber.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Order, LocalDate> colOrderDate = new TableColumn<>("Date");
        colOrderDate.setMinWidth(150);
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Order, String> colSalePrice = new TableColumn<>("Sale Price");
        colSalePrice.setMinWidth(150);
        colSalePrice.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        TableColumn<Order, String> colDesc = new TableColumn<>("Note");
        colDesc.setMinWidth(425);
        colDesc.setCellValueFactory(new PropertyValueFactory<>("note"));

        studentOrderTable.getColumns().addAll(colNumber, colOrderDate, colSalePrice, colDesc);

        OrderDAOImpl orderDAO = new OrderDAOImpl();
        ObservableList<Order> studentOrders = orderDAO.selectAllObservableOrdersByStudent(student.getId());
        studentOrderTable.setItems(studentOrders);
        studentOrderTable.setPlaceholder(new Label("No Orders/Transactions By "+student.getFirstName()));

        studentOrderTable.setRowFactory( tv -> {
            TableRow<Order> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Order rowData = row.getItem();
                    loadOrderDetail(rowData.getId());
                }
            });
            return row ;
        });
    }

    private void loadTestDetail(int testId){
        TestDAOImpl testDAO = new TestDAOImpl();
        test = testDAO.selectById(testId);

        TestDetailController controller = StageLoader.loadStage("view/TestDetail.fxml", "Test Detail").getController();
        controller.setStudent(student);
        controller.initData(test);
    }

    private void loadOrderDetail(int orderId){
        OrderDAOImpl orderDAO = new OrderDAOImpl();
        Order order = orderDAO.selectById(orderId);

        OrderDetailController controller = StageLoader.loadStage("view/OrderDetail.fxml", "Order Detail").getController();
        controller.initData(order);
    }

    private void setUIData(){
        int demoPoints = 0;

        DemoPointAwardedDAO demoPointAwardedDAO = new DemoPointAwardedDAO();
        List<DemoPointAwarded> demoPointsAwarded = demoPointAwardedDAO.selectAllByStudentId(student.getId());

        for (DemoPointAwarded demoPointAwarded : demoPointsAwarded){
            demoPoints += demoPointAwarded.getValue();
        }

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
        lblDemoPoints.setText("Demo Team Points: " + demoPoints);
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
        GraphicTools.removeGraphicEffectOnRootView();
        Stage stage = (Stage) btnOk.getScene().getWindow();
        stage.close();
        StudentController.getInstance().updateStudentTable();
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
