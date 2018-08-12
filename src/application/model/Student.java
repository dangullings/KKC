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
import java.util.Observable;

public class Student {

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

    public int getRankValue() {
        return rankValue;
    }

    public void setRankValue(int rankValue) {
        this.rankValue = rankValue;
    }

    public String getRankName() {
        return rankName;
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
        //setRankValue(Main.Ranks.indexOf(getRankName()));
        setRankName(Main.Ranks.get(++rankValue));
    }

    /*
    @Override
    public int compareTo(Student other) {
        if (getRankValue() < other.getRankValue())
            return -1;
        else if (getRankValue() == other.getRankValue()){
            return getAge().compareTo(other.getAge());
        }
        else
            return 1;
    }

    @Override
    public int compareTo(Student o) {
        return getRankValue().compareTo(o.getRankValue());
    }

    static final Comparator<Student> BY_RANK =
            new Comparator<Student>() {

                @Override
                public int compare(Student o1, Student o2) {
                    return o1.getRankValue().compareTo(o2.getRankValue());
                }
            };
*/

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

    public class AgeCalculator {

        public int calculateAge(LocalDate birthDate, LocalDate currentDate) {
            if ((birthDate != null) && (currentDate != null)) {
                return Period.between(birthDate, currentDate).getYears();
            } else {
                return 0;
            }
        }
    }

    public class TestView{

        private int testId;
        private int rankValue;
        private String rankName;
        private LocalDate date;
        private String location;
        int form, steps, power, kiap, questions, attitude, sparring, breaking;

        public TestView(){

        }

        public TestView(int testId, int form, int steps, int power, int kiap, int questions, int attitude, int sparring, int breaking) {
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

        public void setLocation(String location) {
            this.location = location;
        }

        public int getRankValue() {
            return rankValue;
        }

        public void setRankValue(int rankValue) {
            this.rankValue = rankValue;
        }

        public void setRankName(int rankValue) {
            this.rankName = Main.Ranks.get(rankValue);
        }

        public String getRankName() {
            return rankName;
        }

        public void setDate(int year, int month, int day){
            date = LocalDate.of(year, month, day);
        }

        public void setDate(java.sql.Date date){
            this.date = date.toLocalDate();
        }

        public LocalDate getDate() {
            return date;
        }

        public int getTestId() { return testId; }

        public void setTestId(int testId) { this.testId = testId; }

        public int getForm() { return form; }

        public void setForm(int form) { this.form = form; }

        public int getSteps() { return steps; }

        public void setSteps(int steps) { this.steps = steps; }

        public int getPower() { return power; }

        public void setPower(int power) { this.power = power; }

        public int getKiap() { return kiap; }

        public void setKiap(int kiap) { this.kiap = kiap; }

        public int getQuestions() { return questions; }

        public void setQuestions(int questions) { this.questions = questions; }

        public int getAttitude() { return attitude; }

        public void setAttitude(int attitude) { this.attitude = attitude; }

        public int getSparring() { return sparring; }

        public void setSparring(int sparring) { this.sparring = sparring; }

        public int getBreaking() { return breaking; }

        public void setBreaking(int breaking) { this.breaking = breaking; }
    }
}
