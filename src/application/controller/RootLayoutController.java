package application.controller;

import application.util.DAO.Test_StudentDAOImpl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class RootLayoutController implements Initializable {

    private static RootLayoutController instance;

    public RootLayoutController(){
        instance = this;
    }

    public static RootLayoutController getInstance(){
        return instance;
    }

    public BorderPane borderPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Test_StudentDAOImpl stdi = new Test_StudentDAOImpl();

        stdi.createTest_StudentTable();
    }

    @FXML
    public void pressCloseProgram(){
        Platform.exit();
    }
}
