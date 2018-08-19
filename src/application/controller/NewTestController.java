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
import java.util.List;
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
    ArrayList<Test_Student> test_studentsBeforeEdit;

    boolean isNewTest;

    Test test;
    Test_Student test_student;
    Student selectedStudent = new Student();
    Test_Student selectedTest_Student = new Test_Student();

    Test_StudentDAOImpl stdi = new Test_StudentDAOImpl();

    @FXML TableView<Student> studentsTable;
    @FXML TableView<Student> testStudentsTable;
    @FXML DatePicker datePicker;
    @FXML ChoiceBox<LOCATION> choiceLocation = new ChoiceBox<>();

    @FXML Button btnRemoveStudent;
    @FXML Button btnNewStudent;

    @FXML ToggleGroup groupType;
    @FXML RadioButton radioBtnColor;
    @FXML RadioButton radioBtnBlackbelt;
    @FXML RadioButton radioBtnPrivate;

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
    int numStudents;

    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnAdd;

    public void initData(Test test) {
        this.test = new Test();
        this.test = test;

        loadTestData(test);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeData();
    }

    public void pressAdd(){
        ObservableList<Student> studentsSelected;
        studentsSelected = studentsTable.getSelectionModel().getSelectedItems();

        for (Student student : studentsSelected) {
            Test_Student test_student = new Test_Student(student.getId());
            test_student.setRank(student.getRankValue());
            test_students.add(test_student);
        }

        testStudentsTable.getItems().addAll(studentsSelected);
        studentsTable.getItems().removeAll(studentsSelected);
    }

    public void pressRemoveStudent(ActionEvent event){
        Student selectedStudent = testStudentsTable.getSelectionModel().getSelectedItem();

        for (Test_Student test_student : test_students){
            if (test_student.getStudentId() == selectedStudent.getId()){
                test_students.remove(test_student);
                break;
            }
        }

        testStudentsTable.getItems().remove(selectedStudent);
        studentsTable.getItems().add(selectedStudent);
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
        numStudents = test_students.size();

        RadioButton selectedRadioButton = (RadioButton) groupType.getSelectedToggle();

        if (selectedRadioButton != null)
            type = selectedRadioButton.getText();

        if (validTest()){
            if (isNewTest) {
                test = new Test(type, testDate, location, numStudents);
                tdi.insert(test);
            }else{
                test.setLocation(location);
                test.setDate(testDate);
                test.setNumStudents(numStudents);
                test.setType(type);
                tdi.update(test, test.getId());
            }

            testId = test.getId();

            for (Test_Student test_student : test_students) {
                test_student.setTestId(testId);

                if (isNewTest) {
                    stdi.insert(test_student);
                }else{
                    if (test_studentsBeforeEdit.contains(test_student)){
                        stdi.update(test_student, test_student.getId());
                    }else{
                        stdi.insert(test_student);

                        Student student;
                        student = sdi.selectById(test_student.getStudentId());
                        student.increaseRank();
                        sdi.update(student, student.getId());
                    }
                }
            }

            if (!isNewTest) {
                for (Test_Student test_student_b : test_studentsBeforeEdit) {
                    if (!test_students.contains(test_student_b)) {
                        stdi.delete(test_student_b.getId());

                        Student student;
                        student = sdi.selectById(test_student_b.getStudentId());
                        student.decreaseRank();
                        sdi.update(student, student.getId());
                    }
                }
            }

            if (isNewTest) {
                for (Student student : testStudentsTable.getItems()) {
                    student.increaseRank();
                    sdi.update(student, student.getId());
                }
            }else{

            }

            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();

            TestController.getInstance().testTableInsert(test);
            TestController.getInstance().updateTestTable();
            StudentController.getInstance().updateStudentTable();
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING - MISSING REQUIRED TEST DATA");
            alert.setHeaderText(null);
            alert.setContentText("Check for a valid date, type and location.");
            Optional <ButtonType> action = alert.showAndWait();

            if (action.get() == ButtonType.OK){

            }
        }
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

    public boolean validTest(){
        boolean valid = true;
        ArrayList<Integer> studentErrorList;
        studentErrorList = new ArrayList<>();

        ObservableList<Student> studentsTested;
        studentsTested = testStudentsTable.getItems();

        for (Test_Student test_student : test_students) {
            if (!validScores(test_student)){
                studentErrorList.add(test_student.getStudentId());
            }
        }

        if (studentsTested.isEmpty()){
            valid = false;
        }

        if (!studentErrorList.isEmpty()){
            //valid = false;
        }

        LocalDate dob = datePicker.getValue();

        if (dob == null){
            valid = false;
        }

        RadioButton selectedRadioButton = (RadioButton) groupType.getSelectedToggle();

        if (selectedRadioButton == null) {
            valid = false;
        }

        return valid;
    }

    public boolean validScores(Test_Student test_student){
        if (!test_student.inRange(test_student.getForm())){
            return false;
        }
        if (!test_student.inRange(test_student.getSteps())){
            return false;
        }
        if (!test_student.inRange(test_student.getPower())){
            return false;
        }
        if (!test_student.inRange(test_student.getKiap())){
            return false;
        }
        if (!test_student.inRange(test_student.getQuestions())){
            return false;
        }
        if (!test_student.inRange(test_student.getAttitude())){
            return false;
        }
        if (!test_student.inRange(test_student.getSparring())){
            return false;
        }
        if (!test_student.inRange(test_student.getBreaking())){
            return false;
        }

        return true;
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

    public boolean isNewTest() { return isNewTest; }

    public void setNewTest(boolean newTest) { isNewTest = newTest; }

    private void loadTestData(Test test){
        this.test = new Test();
        this.test = test;
        Test_StudentDAOImpl tsdi = new Test_StudentDAOImpl();
        ObservableList<Student> students = tsdi.selectAllObservableStudentsByTestId(test.getId());
        List<Test_Student> test_students = tsdi.selectAllTest_StudentsByTestId(test.getId());

        this.test_students.clear();
        this.test_students.addAll(test_students);
        test_studentsBeforeEdit.addAll(test_students);
        testStudentsTable.getItems().clear();
        testStudentsTable.getItems().addAll(students);
        datePicker.setValue(test.getDate());
        choiceLocation.setValue(LOCATION.valueOf(test.getLocation()));
        type = test.getType();

        if (this.test.getType().equalsIgnoreCase("Color")) {
            radioBtnColor.setSelected(true);
        }else if (this.test.getType().equalsIgnoreCase("Blackbelt")){
            radioBtnBlackbelt.setSelected(true);
        }else if (this.test.getType().equalsIgnoreCase("Private")){
            radioBtnPrivate.setSelected(true);
        }

        for (Student studento : testStudentsTable.getItems()){
            for (Student studenti : studentsTable.getItems()){
                if (studenti.getId() == studento.getId()){
                    studentsTable.getItems().remove(studenti);
                    break;
                }
            }
        }

        //studentsTable.getItems().removeAll(testStudentsTable.getItems());

        setNewTest(false);
    }

    private void initializeData(){
        setNewTest(true);
        test_students = new ArrayList<>();
        test_studentsBeforeEdit = new ArrayList<>();

        StudentDAOImpl sdi = new StudentDAOImpl();
        ObservableList<Student> students = sdi.selectAllActiveObservable();

        TableColumn<Student, String> colFirstNameList = new TableColumn<>("First");
        //colFirstNameList.setMinWidth(50);
        //colFirstNameList.setMaxWidth(50);
        colFirstNameList.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Student, String> colLastNameList = new TableColumn<>("Last");
        //colLastNameList.setMinWidth(50);
        //colLastNameList.setMaxWidth(50);
        colLastNameList.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Student, String> colRankList = new TableColumn<>("Rank");
        //colRankList.setMinWidth(50);
        //colRankList.setMaxWidth(75);
        colRankList.setCellValueFactory(new PropertyValueFactory<>("rankName"));

        TableColumn<Student, String> colClubList = new TableColumn<>("Club");
        //colClubList.setMinWidth(50);
        //colClubList.setMaxWidth(75);
        colClubList.setCellValueFactory(new PropertyValueFactory<>("club"));

        studentsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        studentsTable.setItems(students);
        studentsTable.getColumns().addAll(colFirstNameList, colLastNameList, colRankList, colClubList);

        TableColumn<Student, String> colFirstName = new TableColumn<>("First Name");
        //colFirstName.setMinWidth(120);
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Student, String> colLastName = new TableColumn<>("Last Name");
        //colLastName.setMinWidth(120);
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Student, String> colRank = new TableColumn<>("Rank");
        //colRank.setMinWidth(120);
        colRank.setCellValueFactory(new PropertyValueFactory<>("rankName"));

        TableColumn<Student, String> colClub = new TableColumn<>("Club");
        //colClub.setMinWidth(120);
        colClub.setCellValueFactory(new PropertyValueFactory<>("club"));

        txtForm.setDisable(true);
        txtSteps.setDisable(true);
        txtPower.setDisable(true);
        txtKiap.setDisable(true);
        txtQuestions.setDisable(true);
        txtAttitude.setDisable(true);
        txtSparring.setDisable(true);
        txtBreaking.setDisable(true);

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

                        txtForm.setDisable(false);
                        txtSteps.setDisable(false);
                        txtPower.setDisable(false);
                        txtKiap.setDisable(false);
                        txtQuestions.setDisable(false);
                        txtAttitude.setDisable(false);
                        txtSparring.setDisable(false);
                        txtBreaking.setDisable(false);
                        break;
                    }
                }
            }
        });

        testStudentsTable.getColumns().addAll(colFirstName, colLastName, colRank, colClub);

        choiceLocation.getItems().addAll(LOCATION.values());
        //choiceLocation.setValue(LOCATION.Waconia);
        type = "Color";

        txtForm.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                if (newValue.isEmpty()){
                    selectedTest_Student.setForm("-");
                }else {
                    selectedTest_Student.setForm(newValue);
                }
            }
        });

        txtSteps.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                if (newValue.isEmpty()){
                    selectedTest_Student.setSteps("-");
                }else {
                    selectedTest_Student.setSteps(newValue);
                }
            }
        });

        txtPower.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                if (newValue.isEmpty()){
                    selectedTest_Student.setPower("-");
                }else {
                    selectedTest_Student.setPower(newValue);
                }
            }
        });

        txtKiap.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                if (newValue.isEmpty()){
                    selectedTest_Student.setKiap("-");
                }else {
                    selectedTest_Student.setKiap(newValue);
                }
            }
        });

        txtQuestions.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                if (newValue.isEmpty()){
                    selectedTest_Student.setQuestions("-");
                }else {
                    selectedTest_Student.setQuestions(newValue);
                }
            }
        });

        txtAttitude.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                if (newValue.isEmpty()){
                    selectedTest_Student.setAttitude("-");
                }else {
                    selectedTest_Student.setAttitude(newValue);
                }
            }
        });

        txtSparring.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                if (newValue.isEmpty()){
                    selectedTest_Student.setSparring("-");
                }else {
                    selectedTest_Student.setSparring(newValue);
                }
            }
        });

        txtBreaking.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                if (newValue.isEmpty()){
                    selectedTest_Student.setBreaking("-");
                }else {
                    selectedTest_Student.setBreaking(newValue);
                }
            }
        });
    }
}

// some scores won't be judged, allow for null or 0 scores
// allow edit of test scores later
