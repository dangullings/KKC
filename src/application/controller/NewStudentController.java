package application.controller;

import application.CLUB;
import application.LOCATION;
import application.Main;
import application.model.Student;
import application.util.StudentDAOImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.stage.Stage;

import javax.swing.text.MaskFormatter;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewStudentController implements Initializable {

    @FXML ComboBox<CLUB> cboClub = new ComboBox<>();
    @FXML ComboBox<String> cboRank = new ComboBox<>();
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtNumber;
    @FXML DatePicker datePickerDOB;

    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cboRank.setItems(Main.Ranks);
        cboRank.setValue(Main.Ranks.get(0));

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
                //if (!validPhoneNumber(txtNumber.getText())){
                //    txtNumber.setStyle("-fx-text-inner-color: red");
                //}else{
                //    txtNumber.setStyle("-fx-text-inner-color: black");
                //}
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
                //if (!validPhoneNumber(txtNumber.getText())){
                //    txtNumber.setStyle("-fx-text-inner-color: red");
                //}else{
                //    txtNumber.setStyle("-fx-text-inner-color: black");
                //}
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
                //if (!validPhoneNumber(txtNumber.getText())){
                //    txtNumber.setStyle("-fx-text-inner-color: red");
                //}else{
                //    txtNumber.setStyle("-fx-text-inner-color: black");
                //}
            }
        });

        cboClub.getItems().addAll(CLUB.values());
        cboClub.setValue(CLUB.CLUB_ONE);
    }

    public void pressSave(ActionEvent event){
        StudentDAOImpl sdi = new StudentDAOImpl();

        if (validStudent()){
            Student student = new Student(txtFirstName.getText(), txtLastName.getText(), cboRank.getValue(), cboClub.getValue().name(), txtEmail.getText(), formatPhoneNumber(txtNumber.getText()), datePickerDOB.getValue());
            sdi.insert(student);

            txtFirstName.clear();
            txtLastName.clear();
            txtEmail.clear();
            txtNumber.clear();

            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();

            if (StudentController.getInstance() != null) {
                StudentController.getInstance().studentTableInsert(student);
            }

            if (NewTestController.getInstance() != null){
                NewTestController.getInstance().studentTableInsert(student);
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid Student Information");
            alert.setHeaderText(null);
            alert.setContentText("Cannot save student. Please enter valid information.");
            Optional<ButtonType> action = alert.showAndWait();

            if (action.get() == ButtonType.OK){ }
        }
    }

    public void pressCancel(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Exit student creation? (all data will be lost)");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK){
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
    }

    public void pressDOB(){
        datePickerDOB.setStyle(cssClear);
    }

    public boolean validStudent(){
        boolean valid = true;

        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String rank = cboRank.getValue();
        String club = cboClub.getValue().name();
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

    final String cssError = "-fx-border-color: red;";
    final String cssClear = "";

    public String formatPhoneNumber(String number){
        number = "("+number.substring(0, 3)+")-"+number.substring(3, 6)+"-"+number.substring(6, 10);

        return number;
    }

    public boolean validPhoneNumber(String input) {
        if (input.length() == 10){
            return true;
        } else{
            return false;
        }

        //String pattern = "^[1-9]\\d{2}-\\d{3}-\\d{4}";

        //if (isRegexMatch(pattern, input)) {
        //    return true;
       // }
        //else {
        //    return false;
        //}
    }

    public boolean validEmail(String input) {
        String pattern = "^\\w+[\\w-\\.]*\\@\\w+((-\\w+)|(\\w*))\\.[a-z]{2,3}$";

        if (isRegexMatch(pattern, input)) {
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isRegexMatch(String pattern, String value) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(value);
        return matcher.matches();
    }
}
