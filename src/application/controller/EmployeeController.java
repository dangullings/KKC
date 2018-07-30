package application.controller;

import application.Main;
import application.util.DBUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class EmployeeController {

    @FXML private Label lbl1;
    @FXML private Button btn1;
    @FXML private Button btn2;

    private Main main;

    public void setMain(Main main){
        this.main = main;
    }

    public void pressAdd(ActionEvent event){
        DBUtil db = new DBUtil();
        try {
            db.main();
            System.out.println("testing add");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void pressButtonTwo(ActionEvent event) throws IOException{
        //Parent scene2ViewParent = FXMLLoader.load(getClass().getResource("view/scene2.fxml"));

        //Scene scene2View = new Scene(scene2ViewParent);

        //Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        //window.setScene(scene2View);
        //window.show();
    }
}
