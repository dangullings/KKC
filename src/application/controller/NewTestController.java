package application.controller;

import application.LOCATION;
import application.model.Student;
import application.model.Test;
import application.model.Test_Student;
import application.util.StudentDAOImpl;
import application.util.TestDAOImpl;
import application.util.Test_StudentDAOImpl;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class NewTestController implements Initializable {

    Test test;
    Test_Student test_student;

    Test_StudentDAOImpl stdi = new Test_StudentDAOImpl();

    @FXML TableView<Student> studentsTable;
    @FXML TableView<Student> testStudentsTable;
    @FXML DatePicker datePicker;
    @FXML ChoiceBox<LOCATION> choiceLocation = new ChoiceBox<>();
    @FXML CheckBox checkIsBlackbelt;

    @FXML Button btnNewTest;

    LocalDate testDate;
    String location;
    String type;

    @FXML
    private Button btnSave;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stdi.createTest_StudentTable();

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

        studentsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        studentsTable.setItems(students);
        studentsTable.getColumns().addAll(colFirstNameList, colLastNameList);

        TableColumn<Student, String> colFirstName = new TableColumn<>("First Name");
        colFirstName.setMinWidth(120);
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Student, String> colLastName = new TableColumn<>("Last Name");
        colLastName.setMinWidth(120);
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

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

        testStudentsTable.getColumns().addAll(colFirstName, colLastName, colEmail, colNumber, colAge, colBirthdate);

        choiceLocation.getItems().addAll(LOCATION.values());
        choiceLocation.setValue(LOCATION.LOC_ONE);
    }

    public void pressAdd(){
        ObservableList<Student> studentsSelected;
        studentsSelected = studentsTable.getSelectionModel().getSelectedItems();

        for (Student student : studentsSelected) {
            if (testStudentsTable.getItems().contains(student))
                testStudentsTable.getItems().remove(student);
        }

        testStudentsTable.getItems().addAll(studentsSelected);
    }

    public void pressDelete(ActionEvent event){

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
        TestDAOImpl tdi = new TestDAOImpl();
        int testId;

        location = choiceLocation.getValue().name();
        testDate = datePicker.getValue();
        if (checkIsBlackbelt.isSelected()){ type = "Black"; } else { type = "Color"; }

        test = new Test(type, testDate, location);

        tdi.insert(test);
        testId = test.getId();

        for (Student student : testStudentsTable.getItems()){
            test_student = new Test_Student(testId, student.getId());
            stdi.insert(test_student);
        }

        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();

        TestController.getInstance().testTableInsert(test);
    }

    // when deleting test and or students, need to delete all records with their id in the test_student database
}
