package application.controller;

import application.util.Test_StudentDAOImpl;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class RootLayoutController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Test_StudentDAOImpl stdi = new Test_StudentDAOImpl();

        stdi.createTest_StudentTable();
    }
}
