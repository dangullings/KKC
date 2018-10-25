package application.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    }

    @FXML
    public void pressCloseProgram(){
        Platform.exit();
    }
}
