package application.controller;

import application.LOCATION;
import application.model.Student;
import application.model.Test;
import application.model.Test_Student;
import application.util.DAO.StudentDAOImpl;
import application.util.DAO.TestDAOImpl;
import application.util.DAO.Test_StudentDAOImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static application.util.AlertUser.alertUser;

public class NewTestController implements Initializable {

    private static NewTestController instance;

    public NewTestController(){
        instance = this;
    }

    public static NewTestController getInstance(){
        return instance;
    }

    private ArrayList<Test_Student> test_students;
    private ArrayList<Test_Student> test_studentsBeforeEdit;

    private boolean isNewTest;

    private Test test;
    private Test_Student test_student;
    private Student selectedStudent = new Student();
    private Test_Student selectedTest_Student = new Test_Student();

    private StudentDAOImpl studentDAO = new StudentDAOImpl();
    private TestDAOImpl testDAO = new TestDAOImpl();
    private Test_StudentDAOImpl testStudentDAO = new Test_StudentDAOImpl();

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

    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private Button btnAdd;

    private LocalDate testDate;
    private String location;
    private String type;
    private int numStudents;

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

        if (studentsSelected.size() == 0){
            return;
        }

        for (Student student : studentsSelected) {
            Test_Student test_student = new Test_Student(student.getId());
            test_student.setRank(student.getRankValue());
            test_students.add(test_student);
        }

        testStudentsTable.getItems().addAll(studentsSelected);
        studentsTable.getItems().removeAll(studentsSelected);
    }

    public void pressRemoveStudent(){
        Student selectedStudent = testStudentsTable.getSelectionModel().getSelectedItem();

        if (selectedStudent == null){
            return;
        }

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
    public void pressNewStudent(){
        StudentController.getInstance().pressNewStudent();
    }

    public void saveTest(){
        int testId;
        numStudents = test_students.size();

        setUIValues();

        if (validTest()){
            if (isNewTest) {
                test = new Test(type, testDate, location, numStudents);
                testDAO.insert(test);
            }else{
                setTestValues();
                testDAO.update(test, test.getId());
            }

            testId = test.getId();

            for (Test_Student test_student : test_students) {
                test_student.setTestId(testId);

                if (isNewTest) {
                    testStudentDAO.insert(test_student);
                }else{
                    if (test_studentsBeforeEdit.contains(test_student)){
                        testStudentDAO.update(test_student, test_student.getId());
                    }else{
                        testStudentDAO.insert(test_student);

                        Student student;
                        student = studentDAO.selectById(test_student.getStudentId());
                        student.increaseRank();
                        studentDAO.update(student, student.getId());
                    }
                }
            }

            if (!isNewTest) {
                for (Test_Student test_student_b : test_studentsBeforeEdit) {
                    if (!test_students.contains(test_student_b)) {
                        testStudentDAO.delete(test_student_b.getId());

                        Student student;
                        student = studentDAO.selectById(test_student_b.getStudentId());
                        student.decreaseRank();
                        studentDAO.update(student, student.getId());
                    }
                }
            }

            if (isNewTest) {
                for (Student student : testStudentsTable.getItems()) {
                    student.increaseRank();
                    studentDAO.update(student, student.getId());
                }
            }

            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();

            TestController.getInstance().testTableInsert(test);
            TestController.getInstance().updateTestTable();
            StudentController.getInstance().updateStudentTable();
            RootLayoutController.getInstance().borderPane.setEffect(null);
        }else{
            alertUser("WARNING - MISSING REQUIRED TEST DATA", "Check for a valid date, type and location.", Alert.AlertType.WARNING);
        }
    }

    public void studentTableInsert(Student student){ studentsTable.getItems().add(student); }

    public void updateStudentsTable(){
        StudentDAOImpl sdi = new StudentDAOImpl();
        studentsTable.getItems().clear();
        ObservableList<Student> students;

        students = sdi.selectAllActiveObservable();

        studentsTable.setItems(students);

        for (Student studento : testStudentsTable.getItems()){
            for (Student studenti : studentsTable.getItems()){
                if (studenti.getId() == studento.getId()){
                    studentsTable.getItems().remove(studenti);
                    break;
                }
            }
        }
    }

    public void pressCancel(){
        Optional<ButtonType> action = alertUser("Confirmation Dialog", "Exit? (all changed data will be lost)", Alert.AlertType.CONFIRMATION);

        if (action.get() == ButtonType.OK){
            RootLayoutController.getInstance().borderPane.setEffect(null);
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

    private void setTestValues(){
        test.setLocation(location);
        test.setDate(testDate);
        test.setNumStudents(numStudents);
        test.setType(type);
    }

    private void setUIValues(){
        location = choiceLocation.getValue().name();
        testDate = datePicker.getValue();

        RadioButton selectedRadioButton = (RadioButton) groupType.getSelectedToggle();

        if (selectedRadioButton != null)
            type = selectedRadioButton.getText();
    }

    @FXML private void cleartxtForm() { txtForm.clear(); }

    @FXML private void cleartxtSteps() { txtSteps.clear(); }

    @FXML private void cleartxtPower() { txtPower.clear(); }

    @FXML private void cleartxtKiap() { txtKiap.clear(); }

    @FXML private void cleartxtQuestions() { txtQuestions.clear(); }

    @FXML private void cleartxtAttitude() { txtAttitude.clear(); }

    @FXML private void cleartxtSparring() { txtSparring.clear(); }

    @FXML private void cleartxtBreaking() { txtBreaking.clear(); }

    public boolean isNewTest() { return isNewTest; }

    private void setNewTest(boolean newTest) { isNewTest = newTest; }

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

        populateStudentTable();
        resetUI();
        setUIListeners();
    }

    private void setUIListeners(){
        txtForm.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                if (newValue.length() > oldValue.length()) {
                    if (txtForm.getText().length() > 1) {
                        txtForm.setText(txtForm.getText().substring(0, 1));
                        newValue = txtForm.getText();
                    }
                }

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

                if (newValue.length() > oldValue.length()) {
                    if (txtSteps.getText().length() > 1) {
                        txtSteps.setText(txtSteps.getText().substring(0, 1));
                        newValue = txtSteps.getText();
                    }
                }

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

                if (newValue.length() > oldValue.length()) {
                    if (txtPower.getText().length() > 1) {
                        txtPower.setText(txtPower.getText().substring(0, 1));
                        newValue = txtPower.getText();
                    }
                }

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

                if (newValue.length() > oldValue.length()) {
                    if (txtKiap.getText().length() > 1) {
                        txtKiap.setText(txtKiap.getText().substring(0, 1));
                        newValue = txtKiap.getText();
                    }
                }

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

                if (newValue.length() > oldValue.length()) {
                    if (txtQuestions.getText().length() > 1) {
                        txtQuestions.setText(txtQuestions.getText().substring(0, 1));
                        newValue = txtQuestions.getText();
                    }
                }

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

                if (newValue.length() > oldValue.length()) {
                    if (txtAttitude.getText().length() > 1) {
                        txtAttitude.setText(txtAttitude.getText().substring(0, 1));
                        newValue = txtAttitude.getText();
                    }
                }

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

                if (newValue.length() > oldValue.length()) {
                    if (txtSparring.getText().length() > 1) {
                        txtSparring.setText(txtSparring.getText().substring(0, 1));
                        newValue = txtSparring.getText();
                    }
                }

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

                if (newValue.length() > oldValue.length()) {
                    if (txtBreaking.getText().length() > 1) {
                        txtBreaking.setText(txtBreaking.getText().substring(0, 1));
                        newValue = txtBreaking.getText();
                    }
                }

                if (newValue.isEmpty()){
                    selectedTest_Student.setBreaking("-");
                }else {
                    selectedTest_Student.setBreaking(newValue);
                }
            }
        });
    }

    private void resetUI(){
        txtForm.setDisable(true);
        txtSteps.setDisable(true);
        txtPower.setDisable(true);
        txtKiap.setDisable(true);
        txtQuestions.setDisable(true);
        txtAttitude.setDisable(true);
        txtSparring.setDisable(true);
        txtBreaking.setDisable(true);

        choiceLocation.getItems().addAll(LOCATION.values());
        choiceLocation.setValue(LOCATION.Waconia);
        type = "Color";
    }

    private void populateStudentTable(){
        ObservableList<Student> students = studentDAO.selectAllActiveObservable();

        TableColumn<Student, String> colFirstNameList = new TableColumn<>("First");
        colFirstNameList.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Student, String> colLastNameList = new TableColumn<>("Last");
        colLastNameList.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Student, String> colRankList = new TableColumn<>("Rank");
        colRankList.setCellValueFactory(new PropertyValueFactory<>("rankName"));

        TableColumn<Student, String> colClubList = new TableColumn<>("Club");
        colClubList.setCellValueFactory(new PropertyValueFactory<>("club"));

        studentsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        studentsTable.setItems(students);
        studentsTable.getColumns().addAll(colFirstNameList, colLastNameList, colRankList, colClubList);

        TableColumn<Student, String> colFirstName = new TableColumn<>("First Name");
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Student, String> colLastName = new TableColumn<>("Last Name");
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Student, String> colRank = new TableColumn<>("Rank");
        colRank.setCellValueFactory(new PropertyValueFactory<>("rankName"));

        TableColumn<Student, String> colClub = new TableColumn<>("Club");
        colClub.setCellValueFactory(new PropertyValueFactory<>("club"));

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
    }
}
