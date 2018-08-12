package application.controller;

import application.LOCATION;
import application.Main;
import application.model.Student;
import application.model.Test;
import application.model.Test_Student;
import application.util.StudentDAOImpl;
import application.util.TestDAOImpl;
import application.util.Test_StudentDAOImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class NewTestController implements Initializable {

    private static NewTestController instance;

    public NewTestController(){
        instance = this;
    }

    public static NewTestController getInstance(){
        return instance;
    }

    ArrayList<Test_Student> test_students;

    Test test;
    Test_Student test_student;
    Student selectedStudent = new Student();
    Test_Student selectedTest_Student = new Test_Student();

    Test_StudentDAOImpl stdi = new Test_StudentDAOImpl();

    @FXML TableView<Student> studentsTable;
    @FXML TableView<Student> testStudentsTable;
    @FXML DatePicker datePicker;
    @FXML ChoiceBox<LOCATION> choiceLocation = new ChoiceBox<>();
    @FXML CheckBox checkIsBlackbelt;

    @FXML Button btnNewTest;

    @FXML Button btnRemoveStudent;
    @FXML Button btnNewStudent;

    @FXML TextField txtForm;
    @FXML TextField txtSteps;
    @FXML TextField txtPower;
    @FXML TextField txtKiap;
    @FXML TextField txtQuestions;
    @FXML TextField txtAttitude;
    @FXML TextField txtSparring;
    @FXML TextField txtBreaking;

    LocalDate testDate;
    String location;
    String type;

    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnAdd;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        test_students = new ArrayList<>();

        StudentDAOImpl sdi = new StudentDAOImpl();
        ObservableList<Student> students = sdi.selectAllObservable();

        TableColumn<Student, String> colFirstNameList = new TableColumn<>("First");
        colFirstNameList.setMinWidth(50);
        colFirstNameList.setMaxWidth(50);
        colFirstNameList.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Student, String> colLastNameList = new TableColumn<>("Last");
        colLastNameList.setMinWidth(50);
        colLastNameList.setMaxWidth(50);
        colLastNameList.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Student, String> colRankList = new TableColumn<>("Rank");
        colRankList.setMinWidth(50);
        colRankList.setMaxWidth(75);
        colRankList.setCellValueFactory(new PropertyValueFactory<>("rankName"));

        TableColumn<Student, String> colClubList = new TableColumn<>("Club");
        colClubList.setMinWidth(50);
        colClubList.setMaxWidth(75);
        colClubList.setCellValueFactory(new PropertyValueFactory<>("club"));

        studentsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        studentsTable.setItems(students);
        studentsTable.getColumns().addAll(colFirstNameList, colLastNameList, colRankList, colClubList);

        TableColumn<Student, String> colFirstName = new TableColumn<>("First Name");
        colFirstName.setMinWidth(120);
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Student, String> colLastName = new TableColumn<>("Last Name");
        colLastName.setMinWidth(120);
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Student, String> colRank = new TableColumn<>("Rank");
        colRank.setMinWidth(120);
        colRank.setCellValueFactory(new PropertyValueFactory<>("rankName"));

        TableColumn<Student, String> colClub = new TableColumn<>("Club");
        colClub.setMinWidth(120);
        colClub.setCellValueFactory(new PropertyValueFactory<>("club"));

        TableColumn<Student, String> colEmail = new TableColumn<>("Email");
        colEmail.setMinWidth(120);
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Student, String> colNumber = new TableColumn<>("Number");
        colNumber.setMinWidth(120);
        colNumber.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn<Student, Integer> colAge = new TableColumn<>("Age");
        colAge.setMinWidth(120);
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<Student, LocalDate> colBirthdate = new TableColumn<>("Birth Date");
        colBirthdate.setMinWidth(120);
        colBirthdate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));

        testStudentsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedStudent = testStudentsTable.getSelectionModel().getSelectedItems().get(0);

                for (Test_Student test_student : test_students){
                    if (test_student.getStudentId() == selectedStudent.getId()){
                        selectedTest_Student = test_student;
                        txtForm.setText(String.valueOf(test_student.getForm()));
                        txtSteps.setText(String.valueOf(test_student.getSteps()));
                        txtPower.setText(String.valueOf(test_student.getPower()));
                        txtKiap.setText(String.valueOf(test_student.getKiap()));
                        txtQuestions.setText(String.valueOf(test_student.getQuestions()));
                        txtAttitude.setText(String.valueOf(test_student.getAttitude()));
                        txtSparring.setText(String.valueOf(test_student.getSparring()));
                        txtBreaking.setText(String.valueOf(test_student.getBreaking()));
                        break;
                    }
                }
            }
        });

        testStudentsTable.getColumns().addAll(colFirstName, colLastName, colRank, colClub, colEmail, colNumber, colAge, colBirthdate);

        choiceLocation.getItems().addAll(LOCATION.values());
        choiceLocation.setValue(LOCATION.LOC_ONE);

        txtForm.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                if (newValue.isEmpty()){
                    selectedTest_Student.setForm(0);
                }else {
                    selectedTest_Student.setForm(Integer.valueOf(newValue));
                }
            }
        });

        txtSteps.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                if (newValue.isEmpty()){
                    selectedTest_Student.setSteps(0);
                }else {
                    selectedTest_Student.setSteps(Integer.valueOf(newValue));
                }
            }
        });

        txtPower.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                if (newValue.isEmpty()){
                    selectedTest_Student.setPower(0);
                }else {
                    selectedTest_Student.setPower(Integer.valueOf(newValue));
                }
            }
        });

        txtKiap.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                if (newValue.isEmpty()){
                    selectedTest_Student.setKiap(0);
                }else {
                    selectedTest_Student.setKiap(Integer.valueOf(newValue));
                }
            }
        });

        txtQuestions.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                if (newValue.isEmpty()){
                    selectedTest_Student.setQuestions(0);
                }else {
                    selectedTest_Student.setQuestions(Integer.valueOf(newValue));
                }
            }
        });

        txtAttitude.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                if (newValue.isEmpty()){
                    selectedTest_Student.setAttitude(0);
                }else {
                    selectedTest_Student.setAttitude(Integer.valueOf(newValue));
                }
            }
        });

        txtSparring.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                if (newValue.isEmpty()){
                    selectedTest_Student.setSparring(0);
                }else {
                    selectedTest_Student.setSparring(Integer.valueOf(newValue));
                }
            }
        });

        txtBreaking.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                if (newValue.isEmpty()){
                    selectedTest_Student.setBreaking(0);
                }else {
                    selectedTest_Student.setBreaking(Integer.valueOf(newValue));
                }
            }
        });
    }

    public void pressAdd(){
        ObservableList<Student> studentsSelected;
        studentsSelected = studentsTable.getSelectionModel().getSelectedItems();

        for (Student student : studentsSelected) {
            if (testStudentsTable.getItems().contains(student))
                testStudentsTable.getItems().remove(student);

            Test_Student test_student = new Test_Student(student.getId());
            test_student.setRank(student.getRankValue());
            test_students.add(test_student);
        }

        testStudentsTable.getItems().addAll(studentsSelected);
    }

    public void pressRemoveStudent(ActionEvent event){
        ObservableList<Student> studentSelected, students;
        students = studentsTable.getItems();
        studentSelected = studentsTable.getSelectionModel().getSelectedItems();

        for (Test_Student test_student : test_students){
            if (test_student.getStudentId() == studentSelected.get(0).getId()){
                test_students.remove(test_student);
            }
        }

        testStudentsTable.getItems().remove(studentSelected);
    }

    @FXML
    public void pressNewStudent(ActionEvent event){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/NewStudent.fxml"));
        try {
            Parent root1 = (Parent) loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("New Student");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void typeChanged(){
        if (checkIsBlackbelt.isSelected()){
            //isBlackbelt = true;
        }
        else{
            //isBlackbelt = false;
        }
    }

    public void locationSelected(){
        //location = choiceLocation.getValue().name();
    }

    public void setTestDate(){
        //testDate = datePicker.getValue();
    }

    public void saveTest(){
        StudentDAOImpl sdi = new StudentDAOImpl();
        TestDAOImpl tdi = new TestDAOImpl();
        int testId;

        location = choiceLocation.getValue().name();
        testDate = datePicker.getValue();
        if (checkIsBlackbelt.isSelected()){ type = "Black"; } else { type = "Color"; }

        test = new Test(type, testDate, location);

        tdi.insert(test);
        testId = test.getId();

        for (Test_Student test_student : test_students) {
            test_student.setTestId(testId);
            stdi.insert(test_student);
        }

        for (Student student : testStudentsTable.getItems()){
            student.increaseRank();
            sdi.update(student, student.getId());
        }

        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();

        TestController.getInstance().testTableInsert(test);
        StudentController.getInstance().updateStudentTable();
    }

    public void pressCancel(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Exit test creation? (all data will be lost)");
        Optional <ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK){
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
    }

    public void studentTableInsert(Student student){ studentsTable.getItems().add(student); }

    @FXML private void cleartxtForm(MouseEvent event) { txtForm.clear(); }

    @FXML private void cleartxtSteps(MouseEvent event) { txtSteps.clear(); }

    @FXML private void cleartxtPower(MouseEvent event) { txtPower.clear(); }

    @FXML private void cleartxtKiap(MouseEvent event) { txtKiap.clear(); }

    @FXML private void cleartxtQuestions(MouseEvent event) { txtQuestions.clear(); }

    @FXML private void cleartxtAttitude(MouseEvent event) { txtAttitude.clear(); }

    @FXML private void cleartxtSparring(MouseEvent event) { txtSparring.clear(); }

    @FXML private void cleartxtBreaking(MouseEvent event) { txtBreaking.clear(); }
}
