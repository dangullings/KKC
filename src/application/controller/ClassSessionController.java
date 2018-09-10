package application.controller;

import application.Main;
import application.model.ClassSession;
import application.util.ClassDateDAOImpl;
import application.util.ClassSessionDAOImpl;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;
import java.util.ResourceBundle;

public class ClassSessionController implements Initializable{

    private static ClassSessionController instance;

    public ClassSessionController(){
        instance = this;
    }

    public static ClassSessionController getInstance(){
        return instance;
    }

    @FXML
    TableView sessionTable;

    @FXML
    Button btnNewClass;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ClassSessionDAOImpl classSessionDAO = new ClassSessionDAOImpl();
        ClassDateDAOImpl classDateDAO = new ClassDateDAOImpl();

        classSessionDAO.createClassSessionTable();
        classDateDAO.createClassTable();

        ObservableList<ClassSession> classSessions = classSessionDAO.selectAllObservable();

        TableColumn<ClassSession, String> colLocation = new TableColumn<>("Location");
        colLocation.setMinWidth(120);
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<ClassSession, LocalDate> colStartDate = new TableColumn<>("Start Date");
        colStartDate.setMinWidth(120);
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<ClassSession, String> colEndDate = new TableColumn<>("End Date");
        colEndDate.setMinWidth(120);
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        sessionTable.setItems(classSessions);
        sessionTable.getColumns().addAll(colLocation, colStartDate, colEndDate);
    }

    public void pressNewClass(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/NewClass.fxml"));
        try {
            Parent root1 = (Parent) loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("New Class");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateSessionTable(){
        ClassSessionDAOImpl classSessionDAO = new ClassSessionDAOImpl();
        sessionTable.getItems().clear();
        ObservableList<ClassSession> classSessions = classSessionDAO.selectAllObservable();
        sessionTable.setItems(classSessions);
    }

    public void sessionTableInsert(ClassSession classSession){
        sessionTable.getItems().add(classSession);
    }
}