package application.controller;

import application.model.DemoPoint;
import application.model.DemoPointAwarded;
import application.model.Student;
import application.util.DAO.DemoPointAwardedDAO;
import application.util.DAO.DemoPointDAO;
import application.util.DAO.StudentDAOImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static application.util.AlertUser.alertUser;


public class DemoPointsController implements Initializable {

    private DemoPointDAO demoPointDAO = new DemoPointDAO();
    private DemoPointAwardedDAO demoPointAwardedDAO = new DemoPointAwardedDAO();

    private DemoPoint selectedDemoPoint = new DemoPoint();
    private DemoPointAwarded selectedStudentDemoPoint = new DemoPointAwarded();
    private Student selectedStudent = new Student();

    ObservableList<DemoPoint> demoPoints1 = demoPointDAO.selectAllObservableByCategory(1);
    ObservableList<DemoPoint> demoPoints2 = demoPointDAO.selectAllObservableByCategory(2);
    ObservableList<DemoPoint> demoPoints3 = demoPointDAO.selectAllObservableByCategory(3);
    ObservableList<DemoPointAwarded> studentDemoPoints;

    @FXML
    Accordion accordDemoPoints;

    @FXML
    TableView tableAccordPane1;
    @FXML
    TableView tableAccordPane2;
    @FXML
    TableView tableAccordPane3;
    @FXML
    TableView tableStudentDemoPoints;

    @FXML
    TextField txtAccordPane1Name;
    @FXML
    TextField txtAccordPane2Name;
    @FXML
    TextField txtAccordPane3Name;

    @FXML
    TextField txtAccordPane1Value;
    @FXML
    TextField txtAccordPane2Value;
    @FXML
    TextField txtAccordPane3Value;

    @FXML
    Button accordPane1Submit;
    @FXML
    Button accordPane2Submit;
    @FXML
    Button accordPane3Submit;
    @FXML
    Button btnAwardDemoPoint;
    @FXML
    Button btnRemoveStudentDemoPoint;

    @FXML ComboBox comboboxStudentDemoPoints;

    @FXML
    VBox midVBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initAccordTable(demoPoints1, 1);
        initAccordTable(demoPoints2, 2);
        initAccordTable(demoPoints3, 3);

        Callback<ListView<Student>, ListCell<Student>> factory = lv -> new ListCell<Student>() {
            @Override
            protected void updateItem(Student item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getFirstName() + " " + item.getLastName());
            }
        };

        StudentDAOImpl studentDAO = new StudentDAOImpl();

        comboboxStudentDemoPoints.setCellFactory(factory);
        comboboxStudentDemoPoints.setButtonCell(factory.call(null));
        comboboxStudentDemoPoints.setItems(studentDAO.selectAllActiveObservable());

        TableColumn<DemoPointAwarded, String> colName = new TableColumn<>("Name");
        colName.setMinWidth(185);
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<DemoPointAwarded, String> colInfo = new TableColumn<>("Info");
        colInfo.setMinWidth(170);
        colInfo.setCellValueFactory(new PropertyValueFactory<>("info"));

        TableColumn<DemoPointAwarded, String> colValue = new TableColumn<>("Value");
        colValue.setMinWidth(10);
        colValue.setMaxWidth(40);
        colValue.setCellValueFactory(new PropertyValueFactory<>("value"));

        tableStudentDemoPoints.getColumns().addAll(colName, colInfo, colValue);

        tableStudentDemoPoints.setPlaceholder(new Label("No Demo Points Awarded"));

        tableStudentDemoPoints.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
                Pane header = (Pane) tableStudentDemoPoints.lookup("TableHeaderRow");
                if (header.isVisible()){
                    header.setMaxHeight(0);
                    header.setMinHeight(0);
                    header.setPrefHeight(0);
                    header.setVisible(false);
                }
            }
        });
    }

    private void initStudentDemoPointsTable(){
        studentDemoPoints = demoPointAwardedDAO.selectAllObservableByStudentId(selectedStudent.getId());
        tableStudentDemoPoints.setItems(studentDemoPoints);
    }

    private void initAccordTable(ObservableList<DemoPoint> demoPoints, int category){
        TableColumn<DemoPoint, String> colName = new TableColumn<>("Name");
        colName.setMinWidth(349);
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<DemoPoint, String> colValue = new TableColumn<>("Value");
        colValue.setMinWidth(30);
        colValue.setCellValueFactory(new PropertyValueFactory<>("strValue"));

        // Custom rendering of the table cell.
        colName.setCellFactory(column -> {
            return new TableCell<DemoPoint, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                        if (item.equalsIgnoreCase("Attend 80% of Classes a Month (avg 5x week)")) {
                            setTextFill(Color.CHOCOLATE);
                            setStyle("-fx-background-color: yellow");
                        } else {
                            setTextFill(Color.BLACK);
                            setStyle("");
                        }
                }
            };
        });
        /*
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit(
                (TableColumn.CellEditEvent<DemoPoint, String> t) ->
                        ( t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).updateName(t.getNewValue())
        );
*/
        //tableAccordPane3.setRowFactory( tv -> {
        //    TableRow<DemoPoint> row = new TableRow<>();
        //    row.setOnMouseClicked(event -> {
        //        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    colName.setCellFactory(TextFieldTableCell.forTableColumn());
                    colName.setOnEditCommit(
                            (TableColumn.CellEditEvent<DemoPoint, String> t) ->
                                    ( t.getTableView().getItems().get(
                                            t.getTablePosition().getRow())
                                    ).updateName(t.getNewValue())
                    );

                    colValue.setCellFactory(TextFieldTableCell.forTableColumn());
                    colValue.setOnEditCommit(
                            (TableColumn.CellEditEvent<DemoPoint, String> t) ->
                                    ( t.getTableView().getItems().get(
                                            t.getTablePosition().getRow())
                                    ).updateValue(t.getNewValue())
                    );
        //        }
        //    });
        //    return row ;
        //});

        tableAccordPane1.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            DemoPoint demoPoint = (DemoPoint) newSelection;

            if (demoPoint.isModifiable()){
                selectedDemoPoint = (DemoPoint) newSelection;
            }else {
                selectedDemoPoint = null;
                //tableAccordPane1.getSelectionModel().clearSelection();
            }
        });
        tableAccordPane2.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            DemoPoint demoPoint = (DemoPoint) newSelection;

            if (demoPoint.isModifiable()){
                selectedDemoPoint = (DemoPoint) newSelection;
            }else {
                selectedDemoPoint = null;
                //tableAccordPane2.getSelectionModel().clearSelection();
            }
        });
        tableAccordPane3.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            DemoPoint demoPoint = (DemoPoint) newSelection;

            if (demoPoint.isModifiable()){
                selectedDemoPoint = (DemoPoint) newSelection;
            }else {
                selectedDemoPoint = null;
                //tableAccordPane3.getSelectionModel().clearSelection();
            }
        });

        tableStudentDemoPoints.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedStudentDemoPoint = (DemoPointAwarded) newSelection;
        });

        if (category == 1) {
            tableAccordPane1.setItems(demoPoints);
            tableAccordPane1.getColumns().addAll(colName, colValue);
            tableAccordPane1.setEditable(true);
        }else if (category == 2){
            tableAccordPane2.setItems(demoPoints);
            tableAccordPane2.getColumns().addAll(colName, colValue);
            tableAccordPane2.setEditable(true);
        }else if (category == 3){
            tableAccordPane3.setItems(demoPoints);
            tableAccordPane3.getColumns().addAll(colName, colValue);
            tableAccordPane3.setEditable(true);
        }

        tableAccordPane1.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
                Pane header = (Pane) tableAccordPane1.lookup("TableHeaderRow");
                if (header.isVisible()){
                    header.setMaxHeight(0);
                    header.setMinHeight(0);
                    header.setPrefHeight(0);
                    header.setVisible(false);
                }
            }
        });

        tableAccordPane2.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
                Pane header = (Pane) tableAccordPane2.lookup("TableHeaderRow");
                if (header.isVisible()){
                    header.setMaxHeight(0);
                    header.setMinHeight(0);
                    header.setPrefHeight(0);
                    header.setVisible(false);
                }
            }
        });

        tableAccordPane3.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
                Pane header = (Pane) tableAccordPane3.lookup("TableHeaderRow");
                if (header.isVisible()){
                    header.setMaxHeight(0);
                    header.setMinHeight(0);
                    header.setPrefHeight(0);
                    header.setVisible(false);
                }
            }
        });

        tableAccordPane2.setPlaceholder(new Label("No Demo Points Created"));
    }

    @FXML void pressSubmit1(){
        if ((txtAccordPane1Name.getText().isEmpty()) || (txtAccordPane1Value.getText().isEmpty())){
            Optional<ButtonType> action = alertUser("Information", "Missing data. (Type a name and value before submitting)", Alert.AlertType.INFORMATION);
            return;
        }

        String newName = txtAccordPane1Name.getText();
        int newValue = Integer.parseInt(txtAccordPane1Value.getText());

        DemoPoint demoPoint = new DemoPoint(newName, newValue, 1, true);
        demoPointDAO.insert(demoPoint);
        demoPoints1.add(demoPoint);

        txtAccordPane1Name.clear();
        txtAccordPane1Value.clear();
    }

    @FXML void pressSubmit2(){
        if ((txtAccordPane2Name.getText().isEmpty()) || (txtAccordPane2Value.getText().isEmpty())){
            Optional<ButtonType> action = alertUser("Information", "Missing data. (Type a name and value before submitting)", Alert.AlertType.INFORMATION);
            return;
        }

        String newName = txtAccordPane2Name.getText();
        int newValue = Integer.parseInt(txtAccordPane2Value.getText());

        DemoPoint demoPoint = new DemoPoint(newName, newValue, 2, true);
        demoPointDAO.insert(demoPoint);
        demoPoints2.add(demoPoint);

        txtAccordPane2Name.clear();
        txtAccordPane2Value.clear();
    }

    @FXML void pressSubmit3(){
        if ((txtAccordPane3Name.getText().isEmpty()) || (txtAccordPane3Value.getText().isEmpty())){
            Optional<ButtonType> action = alertUser("Information", "Missing data. (Type a name and value before submitting)", Alert.AlertType.INFORMATION);
            return;
        }

        String newName = txtAccordPane3Name.getText();
        int newValue = Integer.parseInt(txtAccordPane3Value.getText());

        DemoPoint demoPoint = new DemoPoint(newName, newValue, 3, true);
        demoPointDAO.insert(demoPoint);
        demoPoints3.add(demoPoint);

        txtAccordPane3Name.clear();
        txtAccordPane3Value.clear();
    }

    @FXML void pressDeleteDemoPoint(){
        if (selectedDemoPoint.getName() == null){
            Optional<ButtonType> action = alertUser("Information", "No demo point selected. (select a demo point before deleting)", Alert.AlertType.INFORMATION);
            return;
        }

        Optional<ButtonType> action = alertUser("Confirmation Dialog", "Remove Demo Point?", Alert.AlertType.CONFIRMATION);

        if (action.get() == ButtonType.OK){
            demoPointDAO.deleteById(selectedDemoPoint.getId());

            if (selectedDemoPoint.getCategory() == 1){
                demoPoints1.remove(selectedDemoPoint);
            } else if (selectedDemoPoint.getCategory() == 2){
                demoPoints2.remove(selectedDemoPoint);
            } else if (selectedDemoPoint.getCategory() == 3){
                demoPoints3.remove(selectedDemoPoint);
            }
        }
    }

    @FXML void pressAwardDemoPoint(){
        if ((selectedDemoPoint.getName() == null) || (selectedStudent == null) || (studentDemoPoints == null)){
            Optional<ButtonType> action = alertUser("Information", "Demo Point and or student not selected. (select a demo point and student before awarding)", Alert.AlertType.INFORMATION);
            return;
        }

        DemoPointAwarded demoPointAwarded = new DemoPointAwarded(selectedStudent.getId(), selectedDemoPoint.getName(), selectedDemoPoint.getInfo(), selectedDemoPoint.getValue());
        studentDemoPoints.add(demoPointAwarded);
        demoPointAwardedDAO.insert(demoPointAwarded);
    }

    @FXML void pressRemoveStudentDemoPoint(){
        if ((selectedStudentDemoPoint.getName() == null) || (selectedStudent == null)){
            Optional<ButtonType> action = alertUser("Information", "Demo Point and or student not selected. (select a demo point and student before removing)", Alert.AlertType.INFORMATION);
            return;
        }

        Optional<ButtonType> action = alertUser("Confirmation Dialog", "Remove Demo Point? (Demo Point will be removed from "+selectedStudent.getFirstName()+")", Alert.AlertType.CONFIRMATION);

        if (action.get() == ButtonType.OK) {
            demoPointAwardedDAO.deleteById(selectedStudentDemoPoint.getId());
            //demoPointAwardedDAO.deleteByDemoPointIdAndStudentId(selectedStudentDemoPoint.getId(), selectedStudent.getId());
            studentDemoPoints.remove(selectedStudentDemoPoint);
        }
    }

    @FXML void chooseStudent(){
        selectedStudent = (Student) comboboxStudentDemoPoints.getValue();

        initStudentDemoPointsTable();
    }
}
