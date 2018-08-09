package application.model;

import application.util.StudentDAOImpl;

import java.time.LocalDate;
import java.time.Period;

public class Student {

    private int id;
    private int age;
    private String firstName;
    private String lastName;
    private String rank;
    private String email;
    private String number;
    private LocalDate birthDate;

    public Student(){

    }

    public Student(String firstName, String lastName, String rank, String email, String number, int year, int month, int day){
        this.firstName = firstName;
        this.lastName = lastName;
        this.rank = rank;
        this.email = email;
        this.number = number;
        this.setBirthDate(year, month, day);
        this.age = getAge();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void updateFirstName(String firstName) {
        this.firstName = firstName;

        StudentDAOImpl sdi = new StudentDAOImpl();
        sdi.update(this, this.id);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void updateLastName(String lastName) {
        this.lastName = lastName;

        StudentDAOImpl sdi = new StudentDAOImpl();
        sdi.update(this, this.id);
    }

    public void setBirthDate(int year, int month, int day){
        birthDate = LocalDate.of(year, month, day);
    }

    public void setBirthDate(java.sql.Date birthDate){
        this.birthDate = birthDate.toLocalDate();
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setAge(int age){

    }

    public int getAge(){
        AgeCalculator ageCalculator = new AgeCalculator();
        this.age = ageCalculator.calculateAge(birthDate, LocalDate.now());

        return age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public class AgeCalculator {

        public int calculateAge(LocalDate birthDate, LocalDate currentDate) {
            if ((birthDate != null) && (currentDate != null)) {
                return Period.between(birthDate, currentDate).getYears();
            } else {
                return 0;
            }
        }
    }
}
