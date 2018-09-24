package application.controller;

import application.Main;
import application.model.*;
import application.util.AttendanceDAOImpl;
import application.util.ClassDateDAOImpl;
import application.util.ClassSessionDAOImpl;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClassSessionController implements Initializable{

    private static ClassSessionController instance;

    public ClassSessionController(){
        instance = this;
    }

    public static ClassSessionController getInstance(){
        return instance;
    }

    private ClassSessionDAOImpl classSessionDAO = new ClassSessionDAOImpl();
    private ClassDateDAOImpl classDateDAO = new ClassDateDAOImpl();
    private AttendanceDAOImpl attendanceDAO = new AttendanceDAOImpl();

    @FXML
    TableView sessionTable;

    @FXML
    Button btnNewSession;
    @FXML
    Button btnEditSession;
    @FXML
    Button btnRemoveSession;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    public void pressNewSession(){
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

    @FXML
    public void pressEditSession(){
        ClassSession sessionSelected;
        sessionSelected = (ClassSession) sessionTable.getSelectionModel().getSelectedItem();

        if (sessionSelected == null) {
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/NewClass.fxml"));
        try {
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Edit Session");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene((Pane) loader.load()));
            NewClassSessionController controller = loader.<NewClassSessionController>getController();
            controller.initData(classSessionDAO.selectById(sessionSelected.getId()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pressRemoveSession(){
        ClassSession sessionSelected;
        sessionSelected = (ClassSession) sessionTable.getSelectionModel().getSelectedItem();

        if (sessionSelected == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Remove Session? (Session will be deleted, and all data will be lost)");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK){
            List<ClassDate> classDates;
            classDates = classDateDAO.selectAllBySessionId(sessionSelected.getId());

            List<Attendance> attendances;
            attendances = attendanceDAO.selectAllByClassDateId(sessionSelected.getId());

            for (Attendance attendance : attendances){
                attendanceDAO.deleteById(attendance.getId());
            }

            for (ClassDate classDate : classDates){
                classDateDAO.deleteById(classDate.getId());
            }

            classSessionDAO.deleteById(sessionSelected.getId());
            sessionTable.getItems().remove(sessionSelected);

            AttendanceController.getInstance().init();
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