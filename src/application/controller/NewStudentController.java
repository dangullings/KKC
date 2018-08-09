package application.controller;

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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewStudentController implements Initializable {

    @FXML
    ComboBox<String> rank = new ComboBox<>();
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtNumber;
    @FXML private TextField txtYear;
    @FXML private TextField txtMonth;
    @FXML private TextField txtDay;

    @FXML private Button btnSave;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        rank.setItems(Main.Ranks);
        rank.setValue(Main.Ranks.get(0));

        txtYear.setAlignment(Pos.CENTER);
        //txtYear.setBackground(Background.EMPTY);
        txtYear.setBorder(Border.EMPTY);
        txtYear.setPrefColumnCount(4);

        txtMonth.setAlignment(Pos.CENTER);
        //txtMonth.setBackground(Background.EMPTY);
        txtMonth.setBorder(Border.EMPTY);
        txtMonth.setPrefColumnCount(2);

        txtDay.setAlignment(Pos.CENTER);
        //txtDay.setBackground(Background.EMPTY);
        txtDay.setBorder(Border.EMPTY);
        txtDay.setPrefColumnCount(2);

        txtNumber.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                if (!validPhoneNumber(txtNumber.getText())){
                    txtNumber.setStyle("-fx-text-inner-color: red");
                }else{
                    txtNumber.setStyle("-fx-text-inner-color: black");
                }
            }
        });
    }

    public void pressSave(ActionEvent event){
        StudentDAOImpl sdi = new StudentDAOImpl();

        Student student = new Student(txtFirstName.getText(), txtLastName.getText(), rank.getValue(), txtEmail.getText(), txtNumber.getText(), Integer.parseInt(txtYear.getText()), Integer.parseInt(txtMonth.getText()), Integer.parseInt(txtDay.getText()));
        sdi.insert(student);

        txtFirstName.clear();
        txtLastName.clear();
        txtEmail.clear();
        txtNumber.clear();
        txtYear.clear();
        txtMonth.clear();

        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();

        StudentController.getInstance().studentTableInsert(student);
    }

    public boolean validPhoneNumber(String number) {
        String pattern = "^[1-9]\\d{2}-\\d{3}-\\d{4}";
        if (isRegexMatch(pattern, number)) {
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
