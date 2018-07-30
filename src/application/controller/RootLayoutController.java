package application.controller;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class RootLayoutController {

    @FXML private Label lbl1;
    @FXML private Button btn1;
    @FXML private Button btn2;

    private Main main;

    public void setMain(Main main){
        this.main = main;
    }

    public void pressButtonOne(ActionEvent event){

    }

    public void pressButtonTwo(ActionEvent event) throws IOException{

    }
}
