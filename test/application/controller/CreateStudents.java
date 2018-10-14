package application.controller;

import application.LOCATION;
import application.model.Student;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class CreateStudents {

    private StudentController studentController = new StudentController();

    @Test
    public void CreateStudent (){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        studentController.pressNewStudent();

        NewStudentController newStudentController = new NewStudentController();

        for (int i = 0; i < 3; i++){
            newStudentController.txtFirstName.setText("Firstname"+i);
            newStudentController.txtLastName.setText("Lastname"+i);
            newStudentController.txtNumber.setText("1234567890");
            newStudentController.txtEmail.setText(newStudentController.txtLastName.getText()+"gmail.com");
            newStudentController.cboRank.setValue("Gold Belt");
            newStudentController.cboClub.setValue(LOCATION.Waconia);
            LocalDate date = LocalDate.parse("1985-05-01", formatter);
            newStudentController.datePickerDOB.setValue(date);

            newStudentController.pressSave();
        }
    }

}