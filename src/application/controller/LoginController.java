package application.controller;

import application.Main;
import application.model.User;
import application.util.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    UserDAO userDAO = new UserDAO();

    @FXML
    TextField txtUser;
    @FXML
    TextField txtPassword;
    @FXML
    Button btnLogin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userDAO.createUserTable();

        User user = userDAO.getUserByCredential("admin", "password");

        txtUser.setText(user.getName());
        txtPassword.setText(user.getPassword());
    }

    public void pressLogin(){
        User user = userDAO.getUserByCredential(txtUser.getText(), txtPassword.getText());

        if (user.getName() == null){
            return;
        }else{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
            try {
                Parent root1 = (Parent) loader.load();
                Stage stage = new Stage();
                stage.initStyle(StageStyle.DECORATED);
                stage.setTitle("Kroells Karate Club");
                stage.setScene(new Scene(root1));
                stage.setWidth(1000);
                stage.setHeight(800);
                stage.show();
                Stage stageExit = (Stage) btnLogin.getScene().getWindow();
                stageExit.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
