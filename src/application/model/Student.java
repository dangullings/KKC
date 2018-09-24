package application.model;

import application.Main;
import application.util.StudentDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;

public class Student implements Comparable<Student>{

    private ArrayList<TestView> testViews;

    private int id;
    private int age;
    private String firstName;
    private String lastName;
    private int rankValue;
    private String rankName;
    private String club;
    private String email;
    private String number;
    private LocalDate birthDate;

    private Boolean active;

    public Student(){
        testViews = new ArrayList<>();
    }

    public Student(String firstName, String lastName, String rankName, String club, String email, String number, LocalDate dob){
        this.firstName = firstName;
        this.lastName = lastName;
        this.rankName = rankName;
        this.club = club;
        this.email = email;
        this.number = number;
        this.birthDate = dob;
        this.age = getAge();

        this.active = true;
        this.rankValue = Main.Ranks.indexOf(rankName);

        testViews = new ArrayList<>();
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

    public void updateEmail(String email) {
        this.email = email;

        StudentDAOImpl sdi = new StudentDAOImpl();
        sdi.update(this, this.id);
    }

    public void updateDOB(LocalDate dob) {
        this.birthDate = dob;

        StudentDAOImpl sdi = new StudentDAOImpl();
        sdi.update(this, this.id);
    }

    public void updateNumber(String number) {
        this.number = number;

        StudentDAOImpl sdi = new StudentDAOImpl();
        sdi.update(this, this.id);
    }

    public void setBirthDate(int year, int month, int day){
        birthDate = LocalDate.of(year, month, day);
    }

    public void setBirthDate(java.sql.Date birthDate){
        this.birthDate = birthDate.toLocalDate();
    }

    public void setBirthDate(LocalDate birthDate){
        this.birthDate = birthDate;
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

    public int getRankValue() {
        return rankValue;
    }

    public void setRankValue(int rankValue) { this.rankValue = rankValue; }

    public String getStyleLight(){
        String style;
        switch (rankValue) {
            case 0:  style = "-fx-font-color: black; -fx-border-color: black; -fx-background-color: #FEF78F; -fx-font-size: 16;"; // gold #C8AE01
                break;
            case 1:  style = "-fx-font-color: black; -fx-border-color: black; -fx-background-color: #FEF78F; -fx-font-size: 16;"; // gold #C8AE01
                break;
            case 2:  style = "-fx-font-color: black; -fx-border-color: black; -fx-background-color: #97FE8F; -fx-font-size: 16;"; // green #147800
                break;
            case 3:  style = "-fx-font-color: black; -fx-border-color: black; -fx-background-color: #97FE8F; -fx-font-size: 16;"; // green #147800
                break;
            case 4:  style = "-fx-font-color: black; -fx-border-color: black; -fx-background-color: #D28FFE; -fx-font-size: 16;"; // purple #A401BD
                break;
            case 5:  style = "-fx-font-color: black; -fx-border-color: black; -fx-background-color: #D28FFE; -fx-font-size: 16;"; // purple #A401BD
                break;
            case 6:  style = "-fx-font-color: black; -fx-border-color: black; -fx-background-color: #BC9F7E; -fx-font-size: 16;"; // brown #7D5E47
                break;
            case 7:  style = "-fx-font-color: black; -fx-border-color: black; -fx-background-color: #BC9F7E; -fx-font-size: 16;"; // brown #7D5E47
                break;
            case 8:  style = "-fx-font-color: black; -fx-border-color: black; -fx-background-color: #FE9191; -fx-font-size: 16;"; // red #E30101
                break;
            case 9: style = "-fx-font-color: black; -fx-border-color: black; -fx-background-color: #FE9191; -fx-font-size: 16;"; // red #E30101
                break;
            default: style = "-fx-font-color: black; -fx-border-color: black; -fx-background-color: #878787; -fx-font-size: 16;"; // black
                break;
        }

        return style;
    }

    public String getStyleDark(){
        String style;
        switch (rankValue) {
            case 0:  style = "-fx-font-color: black; -fx-font-weight: bold; -fx-border-color: black; -fx-background-color: #C8AE01; -fx-font-size: 16;"; // gold #C8AE01
                break;
            case 1:  style = "-fx-font-color: black; -fx-font-weight: bold; -fx-border-color: black; -fx-background-color: #C8AE01; -fx-font-size: 16;"; // gold #C8AE01
                break;
            case 2:  style = "-fx-font-color: black; -fx-font-weight: bold; -fx-border-color: black; -fx-background-color: #147800; -fx-font-size: 16;"; // green #147800
                break;
            case 3:  style = "-fx-font-color: black; -fx-font-weight: bold; -fx-border-color: black; -fx-background-color: #147800; -fx-font-size: 16;"; // green #147800
                break;
            case 4:  style = "-fx-font-color: black; -fx-font-weight: bold; -fx-border-color: black; -fx-background-color: #A401BD; -fx-font-size: 16;"; // purple #A401BD
                break;
            case 5:  style = "-fx-font-color: black; -fx-font-weight: bold; -fx-border-color: black; -fx-background-color: #A401BD; -fx-font-size: 16;"; // purple #A401BD
                break;
            case 6:  style = "-fx-font-color: black; -fx-font-weight: bold; -fx-border-color: black; -fx-background-color: #7D5E47; -fx-font-size: 16;"; // brown #7D5E47
                break;
            case 7:  style = "-fx-font-color: black; -fx-font-weight: bold; -fx-border-color: black; -fx-background-color: #7D5E47; -fx-font-size: 16;"; // brown #7D5E47
                break;
            case 8:  style = "-fx-font-color: black; -fx-font-weight: bold; -fx-border-color: black; -fx-background-color: #E30101; -fx-font-size: 16;"; // red #E30101
                break;
            case 9: style = "-fx-font-color: black; -fx-font-weight: bold; -fx-border-color: black; -fx-background-color: #E30101; -fx-font-size: 16;"; // red #E30101
                break;
            default: style = "-fx-font-color: black; -fx-font-weight: bold; -fx-border-color: black; -fx-background-color: #828281; -fx-font-size: 16;"; // black
                break;
        }

        return style;
    }

    public String getRankName() {
        return rankName;
    }

    public String getRankNameRounded() {
        String name = null;

        if ((rankValue >= 11) && (rankValue <= 12)){
            name = "1st Degree";
        }else if ((rankValue >= 13) && (rankValue <= 15)){
            name = "2nd Degree";
        }else if ((rankValue >= 16) && (rankValue <= 19)){
            name = "3rd Degree";
        }else if ((rankValue >= 20) && (rankValue <= 24)){
            name = "4th Degree";
        }else if ((rankValue >= 25) && (rankValue <= 30)){
            name = "5th Degree";
        }
            return name;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getClub() { return club; }

    public void setClub(String club) { this.club = club; }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void increaseRank(){
        setRankName(Main.Ranks.get(++rankValue));
    }

    public void decreaseRank(){
        setRankName(Main.Ranks.get(--rankValue));
    }

    @Override
    public int compareTo(Student other) { // natural order
        if (getRankValue() < other.getRankValue())
            return -1;
        else if (getRankValue() == other.getRankValue()){
            Integer i = new Integer(getAge());
            return i.compareTo(other.getAge());
        }
        else
            return 1;
    }

    static final Comparator<Student> BY_RANK =
            new Comparator<Student>() {

                @Override
                public int compare(Student o1, Student o2) {
                    Integer i = new Integer(o1.getRankValue());
                    return i.compareTo(o2.getRankValue());
                }
            };

    static final Comparator<Student> BY_AGE =
            new Comparator<Student>() {

                @Override
                public int compare(Student o1, Student o2) {
                    Integer i = new Integer(o1.getAge());
                    return i.compareTo(o2.getAge());
                }
            };


    public ObservableList<Student.TestView> getObservableTestViews() {
        ObservableList<Student.TestView> testViews = FXCollections.observableArrayList(getTestViews());

        return testViews;
    }

    public ArrayList<TestView> getTestViews(){
        return testViews;
    }

    public void setTestViews(ObservableList<Test> studentTests, ObservableList<Test_Student> studentScores){
        for (int i = 0; i < studentTests.size(); i++){
            TestView testView = new TestView();

            testView.setTestId(studentTests.get(i).getId());
            testView.setDate(Date.valueOf(studentTests.get(i).getDate()));
            testView.setLocation(studentTests.get(i).getLocation());

            testView.setRankValue(studentScores.get(i).getRank());
            testView.setRankName(testView.getRankValue());
            testView.setForm(studentScores.get(i).getForm());
            testView.setSteps(studentScores.get(i).getSteps());
            testView.setPower(studentScores.get(i).getPower());
            testView.setKiap(studentScores.get(i).getKiap());
            testView.setQuestions(studentScores.get(i).getQuestions());
            testView.setAttitude(studentScores.get(i).getAttitude());
            testView.setSparring(studentScores.get(i).getSparring());
            testView.setBreaking(studentScores.get(i).getBreaking());

            testViews.add(testView);
        }
    }

    private class AgeCalculator {

        private int calculateAge(LocalDate birthDate, LocalDate currentDate) {
            if ((birthDate != null) && (currentDate != null)) {
                return Period.between(birthDate, currentDate).getYears();
            } else {
                return 0;
            }
        }
    }

    public static class TestView{

        private int testId;
        private int rankValue;
        private String rankName;
        private LocalDate date;
        private String location;
        String form, steps, power, kiap, questions, attitude, sparring, breaking;

        public TestView(){

        }

        public TestView(int testId, String form, String steps, String power, String kiap, String questions, String attitude, String sparring, String breaking) {
            this.testId = testId;
            this.form = form;
            this.steps = steps;
            this.power = power;
            this.kiap = kiap;
            this.questions = questions;
            this.attitude = attitude;
            this.sparring = sparring;
            this.breaking = breaking;
        }

        public String getLocation() {
            return location;
        }

        private void setLocation(String location) {
            this.location = location;
        }

        private int getRankValue() {
            return rankValue;
        }

        private void setRankValue(int rankValue) {
            this.rankValue = rankValue;
        }

        private void setRankName(int rankValue) { this.rankName = Main.Ranks.get(rankValue); }

        public String getRankName() {
            return rankName;
        }

        public void setDate(int year, int month, int day){
            date = LocalDate.of(year, month, day);
        }

        private void setDate(java.sql.Date date){
            this.date = date.toLocalDate();
        }

        public LocalDate getDate() {
            return date;
        }

        public int getTestId() { return testId; }

        private void setTestId(int testId) { this.testId = testId; }

        public String getForm() { return form; }

        private void setForm(String form) { this.form = form; }

        public String getSteps() { return steps; }

        private void setSteps(String steps) { this.steps = steps; }

        public String getPower() { return power; }

        private void setPower(String power) { this.power = power; }

        public String getKiap() { return kiap; }

        private void setKiap(String kiap) { this.kiap = kiap; }

        public String getQuestions() { return questions; }

        private void setQuestions(String questions) { this.questions = questions; }

        public String getAttitude() { return attitude; }

        private void setAttitude(String attitude) { this.attitude = attitude; }

        public String getSparring() { return sparring; }

        private void setSparring(String sparring) { this.sparring = sparring; }

        public String getBreaking() { return breaking; }

        private void setBreaking(String breaking) { this.breaking = breaking; }
    }
}
