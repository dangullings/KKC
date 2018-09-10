package application.controller;

import application.LOCATION;
import application.model.ClassDate;
import application.model.ClassSession;
import application.util.ClassDateDAOImpl;
import application.util.ClassSessionDAOImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class NewClassSessionController implements Initializable{

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

    ArrayList<LocalDate> classDates;
    ArrayList<SecondHourDate> secondHourDates;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choiceLocation.getItems().addAll(LOCATION.values());
        secondHourDates = new ArrayList<>();
        classDates = new ArrayList<>();
    }

    public void pressAddDate(){
        if (pickerSpecificDate == null){
            return;
        }

        LocalDate date = pickerSpecificDate.getValue();

        classDates.add(date);

        if (secondHourSpecific.isSelected()){
            SecondHourDate secondHourDate = new SecondHourDate(date, true);
            secondHourDates.add(secondHourDate);
        }

        choiceDates.getItems().add(date);
    }

    public void pressRemoveDate(){
        SecondHourDate tempSecondHourDate = new SecondHourDate();
        LocalDate date = choiceDates.getValue();

        if (date == null){
            return;
        }

        classDates.remove(date);

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
        ClassSession classSession = new ClassSession();

        classSession.setLocation(choiceLocation.getValue().name());
        classSession.setStartDate(Collections.min(classDates));
        classSession.setEndDate(Collections.max(classDates));

        classSessionDAO.insert(classSession);

        for (LocalDate date : classDates){
            ClassDate classDate = new ClassDate();

            classDate.setDate(date);
            classDate.setSessionId(classSession.getId());

            for (SecondHourDate secondHourDate : secondHourDates){
                if (secondHourDate.getDate().equals(date)){
                    classDate.setSecondHour(true);
                    break;
                }
            }

            classDateDAO.insert(classDate);
        }

        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();

        ClassSessionController.getInstance().sessionTableInsert(classSession);
        ClassSessionController.getInstance().updateSessionTable();
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
        classDates.clear();

        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            DayOfWeek dayOfWeek = DayOfWeek.from(date);
            if ((checkMonday.isSelected()) && (dayOfWeek.getValue() == 1)) {
                classDates.add(date);

                if (secondHourMonday.isSelected()){
                    SecondHourDate secondHourDate = new SecondHourDate(date, true);
                    secondHourDates.add(secondHourDate);
                }
            }
            if ((checkTuesday.isSelected()) && (dayOfWeek.getValue() == 2)) {
                classDates.add(date);

                if (secondHourTuesday.isSelected()){
                    SecondHourDate secondHourDate = new SecondHourDate(date, true);
                    secondHourDates.add(secondHourDate);
                }
            }
            if ((checkWednesday.isSelected()) && (dayOfWeek.getValue() == 3)) {
                classDates.add(date);

                if (secondHourWednesday.isSelected()){
                    SecondHourDate secondHourDate = new SecondHourDate(date, true);
                    secondHourDates.add(secondHourDate);
                }
            }
            if ((checkThursday.isSelected()) && (dayOfWeek.getValue() == 4)) {
                classDates.add(date);

                if (secondHourThursday.isSelected()){
                    SecondHourDate secondHourDate = new SecondHourDate(date, true);
                    secondHourDates.add(secondHourDate);
                }
            }
            if ((checkFriday.isSelected()) && (dayOfWeek.getValue() == 5)) {
                classDates.add(date);

                if (secondHourFriday.isSelected()){
                    SecondHourDate secondHourDate = new SecondHourDate(date, true);
                    secondHourDates.add(secondHourDate);
                }
            }
            if ((checkSaturday.isSelected()) && (dayOfWeek.getValue() == 6)) {
                classDates.add(date);

                if (secondHourSaturday.isSelected()){
                    SecondHourDate secondHourDate = new SecondHourDate(date, true);
                    secondHourDates.add(secondHourDate);
                }
            }

            choiceDates.getItems().clear();
            choiceDates.getItems().addAll(classDates);
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
