package application.controller;

import application.LOCATION;
import application.Main;
import application.model.Student;
import application.util.DAO.StudentDAOImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static application.util.AlertUser.alertUser;

public class NewStudentController implements Initializable {

    private static NewStudentController instance;

    public NewStudentController(){
        instance = this;
    }

    private StudentDAOImpl studentDAO = new StudentDAOImpl();

    public static NewStudentController getInstance(){
        return instance;
    }

    private Student student;
    private boolean isNewStudent;

    @FXML ComboBox<LOCATION> cboClub = new ComboBox<>();
    @FXML ComboBox<String> cboRank = new ComboBox<>();
    @FXML public TextField txtFirstName;
    @FXML public TextField txtLastName;
    @FXML public TextField txtEmail;
    @FXML public TextField txtNumber;
    @FXML DatePicker datePickerDOB;
    @FXML public Button btnSave;
    @FXML public Button btnCancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setNewStudent(true);
        setUIData();
        addListeners();
    }

    private void addListeners(){
        txtEmail.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                if (!validEmail(txtEmail.getText())){
                    txtEmail.setStyle("-fx-border-color: red;");
                }else{
                    txtEmail.setStyle("");
                }
            }
        });

        txtNumber.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                if (newValue.length() == 10){
                    txtNumber.setStyle(cssClear);
                } else{
                    txtNumber.setStyle(cssError);
                }
            }
        });

        txtFirstName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                if (newValue.length() > 0){
                    txtFirstName.setStyle(cssClear);
                } else{
                    txtFirstName.setStyle(cssError);
                }
            }
        });

        txtLastName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                if (newValue.length() > 0){
                    txtLastName.setStyle(cssClear);
                } else{
                    txtLastName.setStyle(cssError);
                }
            }
        });
    }

    private void setUIData(){
        cboRank.setItems(Main.Ranks);
        cboRank.setValue(Main.Ranks.get(0));
        cboClub.getItems().addAll(LOCATION.values());
        cboClub.setValue(LOCATION.Waconia);
        LocalDate initDate = LocalDate.now().minusYears(6);
        datePickerDOB.setValue(initDate);
    }

    private void loadStudentData(Student s){
        student = new Student();
        student = s;

        txtFirstName.setText(student.getFirstName());
        txtLastName.setText(student.getLastName());
        txtEmail.setText(student.getEmail());
        txtNumber.setText(stripPhoneNumber(student.getNumber()));
        cboRank.setValue(Main.Ranks.get(student.getRankValue()));
        cboClub.setValue(LOCATION.valueOf(student.getClub()));
        datePickerDOB.setValue(student.getBirthDate());

        setNewStudent(false);
    }

    public void initData(Student student) {
        this.student = new Student();
        this.student = student;

        loadStudentData(student);
    }

    public void pressSave(){
        if (validStudent()){
            if (isNewStudent){
                Student student = new Student(txtFirstName.getText(), txtLastName.getText(), cboRank.getValue(), cboClub.getValue().name(), txtEmail.getText(), formatPhoneNumber(txtNumber.getText()), datePickerDOB.getValue());
                this.student = student;
                studentDAO.insert(student);
            }else{
                setStudentInfo();

                studentDAO.update(student, student.getId());
            }

            clearTextFields();

            if (StudentController.getInstance() != null) {
                StudentController.getInstance().studentTableInsert(student);
                StudentController.getInstance().updateStudentTable();
            }

            if (NewTestController.getInstance() != null){
                NewTestController.getInstance().studentTableInsert(student);
                NewTestController.getInstance().updateStudentsTable();
            }

            RootLayoutController.getInstance().borderPane.setEffect(null);
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();
        }else{
            Optional<ButtonType> action = alertUser("Invalid Student Information", "Cannot save student. Please enter valid information.", Alert.AlertType.INFORMATION);
            if (action.get() == ButtonType.OK){ }
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

    private void clearTextFields(){
        txtFirstName.clear();
        txtLastName.clear();
        txtEmail.clear();
        txtNumber.clear();
    }

    private void setStudentInfo(){
        student.setFirstName(txtFirstName.getText());
        student.setLastName(txtLastName.getText());
        student.setRankValue(Main.Ranks.indexOf(cboRank.getValue()));
        student.setRankName(cboRank.getValue());
        student.setClub(cboClub.getValue().name());
        student.setEmail(txtEmail.getText());
        student.setNumber(formatPhoneNumber(txtNumber.getText()));
        student.setBirthDate(datePickerDOB.getValue());
    }

    public void pressDOB(){
        datePickerDOB.setStyle(cssClear);
    }

    public boolean validStudent(){
        boolean valid = true;

        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String email = txtEmail.getText();
        String number = txtNumber.getText();
        LocalDate dob = datePickerDOB.getValue();

        if (firstName.isEmpty()){
            txtFirstName.setStyle(cssError);
            valid = false;
        }

        if (lastName.isEmpty()){
            txtLastName.setStyle(cssError);
            valid = false;
        }

        if (!validEmail(email)){
            txtEmail.setStyle(cssError);
            valid = false;
        }

        if (!validPhoneNumber(number)){
            txtNumber.setStyle(cssError);
            valid = false;
        }

        if (dob == null){
            datePickerDOB.setStyle(cssError);
            valid = false;
        }

        return valid;
    }

    private final String cssError = "-fx-border-color: red;";
    private final String cssClear = "";

    private String formatPhoneNumber(String number){
        number = ""+number.substring(0, 3)+"-"+number.substring(3, 6)+"-"+number.substring(6, 10);

        return number;
    }

    private String stripPhoneNumber(String number){
        return number.replace("-", "");
    }

    private boolean validPhoneNumber(String input) {
        return (input.length() == 10);
    }

    private boolean validEmail(String input) {
        String pattern = "^\\w+[\\w-\\.]*\\@\\w+((-\\w+)|(\\w*))\\.[a-z]{2,3}$";

        return (isRegexMatch(pattern, input));
    }

    private boolean isRegexMatch(String pattern, String value) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(value);
        return matcher.matches();
    }

    public boolean isNewStudent() {
        return isNewStudent;
    }

    private void setNewStudent(boolean newStudent) {
        isNewStudent = newStudent;
    }
}
