package application.controller;

import application.LOCATION;
import application.model.Attendance;
import application.model.ClassDate;
import application.model.ClassSession;
import application.util.AlertUser;
import application.util.DAO.AttendanceDAOImpl;
import application.util.DAO.ClassDateDAOImpl;
import application.util.DAO.ClassSessionDAOImpl;
import application.util.GraphicTools;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import static application.util.AlertUser.alertUser;

public class NewClassSessionController implements Initializable{

    private AttendanceDAOImpl attendanceDAO = new AttendanceDAOImpl();

    private ClassSession classSession = new ClassSession();

    private boolean isNewSession, isPartialFinal;

    @FXML
    Button btnRemoveDate;
    @FXML
    Button btnAddDate;
    @FXML
    Button btnCancel;
    @FXML
    Button btnSave;

    @FXML
    ComboBox<LOCATION> choiceLocation;
    @FXML
    ComboBox<LocalDate> choiceDates;

    @FXML
    CheckBox checkMonday;
    @FXML
    CheckBox checkTuesday;
    @FXML
    CheckBox checkWednesday;
    @FXML
    CheckBox checkThursday;
    @FXML
    CheckBox checkFriday;
    @FXML
    CheckBox checkSaturday;
    @FXML
    CheckBox secondHourMonday;
    @FXML
    CheckBox secondHourTuesday;
    @FXML
    CheckBox secondHourWednesday;
    @FXML
    CheckBox secondHourThursday;
    @FXML
    CheckBox secondHourFriday;
    @FXML
    CheckBox secondHourSaturday;
    @FXML
    CheckBox secondHourSpecific;

    @FXML
    DatePicker pickerStartDate;
    @FXML
    DatePicker pickerEndDate;
    @FXML
    DatePicker pickerSpecificDate;

    ClassSessionDAOImpl classSessionDAO = new ClassSessionDAOImpl();
    ClassDateDAOImpl classDateDAO = new ClassDateDAOImpl();

    LocalDate startDate;
    LocalDate endDate;

    private ArrayList<Integer> finalMonths;
    private List<ClassDate> classDateList;

    List<LocalDate> classDates;
    List<SecondHourDate> secondHourDates;

    private List<ClassDate> classDateListBeforeEdit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setNewSession(true);
        choiceLocation.getItems().addAll(LOCATION.values());
        secondHourDates = new ArrayList<>();
        classDates = new ArrayList<>();
        classDateList = new ArrayList<>();
        finalMonths = new ArrayList<>();
    }

    private void loadClassSessionData(ClassSession c){
        classSession = c;

        classDateList = classDateDAO.selectAllBySessionId(classSession.getId());
        classDateListBeforeEdit = classDateDAO.selectAllBySessionId(classSession.getId());

        finalMonths.clear();
        classDates.clear();
        for (ClassDate classDate : classDateList){
            classDates.add(classDate.getDate());

            if (classDate.getComplete()){
                if (!finalMonths.contains(classDate.getDate().getMonthValue())){
                    finalMonths.add(classDate.getDate().getMonthValue());
                }
            }
        }

        if (!finalMonths.isEmpty()){
            setUIDisable(true);
        }else{
            setUIDisable(false);
        }

        choiceDates.getItems().addAll(classDates);
        choiceLocation.setValue(LOCATION.valueOf(classSession.getLocation()));
        pickerStartDate.setValue(classSession.getStartDate());
        pickerEndDate.setValue(classSession.getEndDate());

        startDate = pickerStartDate.getValue();
        endDate = pickerEndDate.getValue();

        for (ClassDate classDate : classDateList){
            LocalDate date = classDate.getDate();
            DayOfWeek day = date.getDayOfWeek();

            if (day.getValue() == 1){
                checkMonday.setSelected(true);
                if (classDate.hasSecondHour()){
                    secondHourMonday.setSelected(true);
                }
            }else if (day.getValue() == 2){
                checkTuesday.setSelected(true);
                if (classDate.hasSecondHour()){
                    secondHourTuesday.setSelected(true);
                }
            }else if (day.getValue() == 3){
                checkWednesday.setSelected(true);
                if (classDate.hasSecondHour()){
                    secondHourWednesday.setSelected(true);
                }
            }else if (day.getValue() == 4){
                checkThursday.setSelected(true);
                if (classDate.hasSecondHour()){
                    secondHourThursday.setSelected(true);
                }
            }else if (day.getValue() == 5){
                checkFriday.setSelected(true);
                if (classDate.hasSecondHour()){
                    secondHourFriday.setSelected(true);
                }
            }else if (day.getValue() == 6){
                checkSaturday.setSelected(true);
                if (classDate.hasSecondHour()){
                    secondHourSaturday.setSelected(true);
                }
            }
        }

        /*
        choiceDates.setCellFactory(lv -> new ListCell<LocalDate>() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.toString());
                    setDisable(classDateList.contains(item.getMonthValue()));
                    setStyle("-fx-opacity: 0.4;");
                }
            }
        });
        */

        setNewSession(false);
    }

    public void initData(ClassSession classSession) {
        this.classSession = new ClassSession();
        this.classSession = classSession;

        loadClassSessionData(classSession);

        isPartialFinal = false;
    }

    public void pressAddDate(){
        if (pickerSpecificDate == null){
            return;
        }

        ClassDate classDate = new ClassDate();
        LocalDate date = pickerSpecificDate.getValue();

        if (finalMonths.contains(date.getMonthValue())){
            return;
        }

        classDates.add(date);
        classDate.setDate(date);

        if (secondHourSpecific.isSelected()){
            SecondHourDate secondHourDate = new SecondHourDate(date, true);
            secondHourDates.add(secondHourDate);
            classDate.setSecondHour(true);
        }

        choiceDates.getItems().add(date);
        classDateList.add(classDate);
        pickerSpecificDate.getEditor().clear();
    }

    public void pressRemoveDate(){
        SecondHourDate tempSecondHourDate = new SecondHourDate();
        LocalDate date = choiceDates.getValue();

        if (date == null){
            return;
        }

        classDates.remove(date);

        for (ClassDate classDate : classDateList){
            if (classDate.getDate().equals(date)){
                classDateList.remove(classDate);
                break;
            }
        }

        choiceDates.getItems().remove(date);

        for (SecondHourDate secondHourDate : secondHourDates){
            if (secondHourDate.getDate().equals(date)){
                tempSecondHourDate = secondHourDate;
                break;
            }
        }

        secondHourDates.remove(tempSecondHourDate);
    }

    public void pressSave(){
        List<Attendance> attendances;

        classSession.setLocation(choiceLocation.getValue().name());
        classSession.setStartDate(Collections.min(classDates));
        classSession.setEndDate(Collections.max(classDates));

        if (isNewSession){
            classSession = new ClassSession(choiceLocation.getValue().name(), Collections.min(classDates), Collections.max(classDates));
            classSessionDAO.insert(classSession);

            for (ClassDate classDate : classDateList) {
                classDate.setSessionId(classSession.getId());
                classDateDAO.insert(classDate);
            }
        }else{
            classSessionDAO.update(classSession, classSession.getId());

            for (ClassDate classDate_b : classDateListBeforeEdit) {
                if (!classDateList.contains(classDate_b)) {
                    attendances = attendanceDAO.selectAllByClassDateId(classDate_b.getId());

                    for (Attendance attendance : attendances){
                        attendanceDAO.deleteById(attendance.getId());
                    }

                    classDateDAO.deleteById(classDate_b.getId());
                }
            }

            for (ClassDate classDate : classDateList) {
                if (!classDateListBeforeEdit.contains(classDate)){
                    classDate.setSessionId(classSession.getId());
                    classDateDAO.insert(classDate);
                }
            }

        }

        GraphicTools.removeGraphicEffectOnRootView();

        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();

        ClassSessionController.getInstance().sessionTableInsert(classSession);
        ClassSessionController.getInstance().updateSessionTable();

        AttendanceController.getInstance().init();
    }

    private void setUIDisable(boolean d){
        checkMonday.setDisable(d);
        checkTuesday.setDisable(d);
        checkWednesday.setDisable(d);
        checkThursday.setDisable(d);
        checkFriday.setDisable(d);
        checkSaturday.setDisable(d);
        secondHourMonday.setDisable(d);
        secondHourTuesday.setDisable(d);
        secondHourWednesday.setDisable(d);
        secondHourThursday.setDisable(d);
        secondHourFriday.setDisable(d);
        secondHourSaturday.setDisable(d);
        pickerStartDate.setDisable(d);
        pickerEndDate.setDisable(d);
        choiceLocation.setDisable(d);
    }

    public void choiceDatesChange(){
        if (finalMonths.contains(choiceDates.getValue().getMonthValue())){
            btnRemoveDate.setDisable(true);
        }else{
            btnRemoveDate.setDisable(false);
        }
    }

    public void pickerSpecificDateChange(){
        if (finalMonths.contains(pickerSpecificDate.getValue().getMonthValue())){
            btnAddDate.setDisable(true);
        }else{
            btnAddDate.setDisable(false);
        }
    }

    public void changeStartDate(){
        startDate = pickerStartDate.getValue();

        updateClassDates();
    }

    public void changeEndDate(){
        endDate = pickerEndDate.getValue();

        updateClassDates();
    }

    public boolean validDateSelections(){
        return ((startDate != null) && ((endDate != null)) && (endDate.isAfter(startDate)));
    }

    public void updateClassDates(){
        if (!validDateSelections()){
            return;
        }

        secondHourDates.clear();
        choiceDates.getItems().clear();
        classDateList.clear();
        classDates.clear();
        classDateList.clear();

        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            DayOfWeek dayOfWeek = DayOfWeek.from(date);

            if ((checkMonday.isSelected()) && (dayOfWeek.getValue() == 1)) {
                ClassDate classDate = new ClassDate();
                classDates.add(date);
                classDate.setDate(date);

                if (secondHourMonday.isSelected()){
                    SecondHourDate secondHourDate = new SecondHourDate(date, true);
                    secondHourDates.add(secondHourDate);
                    classDate.setSecondHour(true);
                }

                classDateList.add(classDate);
            }
            if ((checkTuesday.isSelected()) && (dayOfWeek.getValue() == 2)) {
                ClassDate classDate = new ClassDate();
                classDates.add(date);
                classDate.setDate(date);

                if (secondHourTuesday.isSelected()){
                    SecondHourDate secondHourDate = new SecondHourDate(date, true);
                    secondHourDates.add(secondHourDate);
                    classDate.setSecondHour(true);
                }

                classDateList.add(classDate);
            }
            if ((checkWednesday.isSelected()) && (dayOfWeek.getValue() == 3)) {
                ClassDate classDate = new ClassDate();
                classDates.add(date);
                classDate.setDate(date);

                if (secondHourWednesday.isSelected()){
                    SecondHourDate secondHourDate = new SecondHourDate(date, true);
                    secondHourDates.add(secondHourDate);
                    classDate.setSecondHour(true);
                }

                classDateList.add(classDate);
            }
            if ((checkThursday.isSelected()) && (dayOfWeek.getValue() == 4)) {
                ClassDate classDate = new ClassDate();
                classDates.add(date);
                classDate.setDate(date);

                if (secondHourThursday.isSelected()){
                    SecondHourDate secondHourDate = new SecondHourDate(date, true);
                    secondHourDates.add(secondHourDate);
                    classDate.setSecondHour(true);
                }

                classDateList.add(classDate);
            }
            if ((checkFriday.isSelected()) && (dayOfWeek.getValue() == 5)) {
                ClassDate classDate = new ClassDate();
                classDates.add(date);
                classDate.setDate(date);

                if (secondHourFriday.isSelected()){
                    SecondHourDate secondHourDate = new SecondHourDate(date, true);
                    secondHourDates.add(secondHourDate);
                    classDate.setSecondHour(true);
                }

                classDateList.add(classDate);
            }
            if ((checkSaturday.isSelected()) && (dayOfWeek.getValue() == 6)) {
                ClassDate classDate = new ClassDate();
                classDates.add(date);
                classDate.setDate(date);

                if (secondHourSaturday.isSelected()){
                    SecondHourDate secondHourDate = new SecondHourDate(date, true);
                    secondHourDates.add(secondHourDate);
                    classDate.setSecondHour(true);
                }

                classDateList.add(classDate);
            }

            choiceDates.getItems().clear();
            choiceDates.getItems().addAll(classDates);
        }
    }

    @FXML
    private void pressCancel(){
        Optional<ButtonType> action = alertUser("Confirmation", "Exit? (all changed data will be lost)", Alert.AlertType.CONFIRMATION);

        if (action.get() == ButtonType.OK){
            GraphicTools.removeGraphicEffectOnRootView();
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
    }

    public void changeLocation(){
        choiceDates.getItems().clear();
        classDates.clear();
        secondHourDates.clear();

        checkMonday.setSelected(false);
        checkTuesday.setSelected(false);
        checkWednesday.setSelected(false);
        checkThursday.setSelected(false);
        checkFriday.setSelected(false);
        checkSaturday.setSelected(false);
        secondHourMonday.setSelected(false);
        secondHourTuesday.setSelected(false);
        secondHourWednesday.setSelected(false);
        secondHourThursday.setSelected(false);
        secondHourFriday.setSelected(false);
        secondHourSaturday.setSelected(false);
        secondHourSpecific.setSelected(false);
        pickerStartDate.setValue(null);
        pickerStartDate.setPromptText("start date");
        pickerEndDate.setValue(null);
        pickerEndDate.setPromptText("end date");
        pickerSpecificDate.setValue(null);
        pickerSpecificDate.setPromptText("specific date");
    }

    public boolean isNewSession() {
        return isNewSession;
    }

    public void setNewSession(boolean newSession) {
        isNewSession = newSession;
    }

    public void changeMonday(){
        updateClassDates();
    }

    public void changeTuesday(){
        updateClassDates();
    }

    public void changeWednesday(){
        updateClassDates();
    }

    public void changeThursday(){
        updateClassDates();
    }

    public void changeFriday(){
        updateClassDates();
    }

    public void changeSaturday(){
        updateClassDates();
    }

    public void changeSecondHourMonday(){
        updateClassDates();
    }

    public void changeSecondHourTuesday(){
        updateClassDates();
    }

    public void changeSecondHourWednesday(){
        updateClassDates();
    }

    public void changeSecondHourThursday(){
        updateClassDates();
    }

    public void changeSecondHourFriday(){
        updateClassDates();
    }

    public void changeSecondHourSaturday(){
        updateClassDates();
    }

    private class SecondHourDate {
        LocalDate date;
        boolean hasSecondHour;

        public SecondHourDate(){

        }

        public SecondHourDate(LocalDate date, boolean hasSecondHour) {
            this.date = date;
            this.hasSecondHour = hasSecondHour;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public boolean isHasSecondHour() {
            return hasSecondHour;
        }

        public void setHasSecondHour(boolean hasSecondHour) {
            this.hasSecondHour = hasSecondHour;
        }
    }
}
