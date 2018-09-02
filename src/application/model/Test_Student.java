package application.model;

public class Test_Student {

    private int id;
    private int testId;
    private int studentId;
    private int rank;

    private String form, steps, power, kiap, questions, attitude, sparring, breaking;

    public Test_Student() {

    }

    public Test_Student(int studentId) {
        this.studentId = studentId;
        this.form = "-";
        this.steps = "-";
        this.power = "-";
        this.kiap = "-";
        this.questions = "-";
        this.attitude = "-";
        this.sparring = "-";
        this.breaking = "-";
    }

    public Test_Student(int testId, int studentId) {
        this.testId = testId;
        this.studentId = studentId;
    }

    public boolean inRange(String score){
        return ((score.equalsIgnoreCase("5")) || (score.equalsIgnoreCase("6")) || (score.equalsIgnoreCase("7")) || (score.equalsIgnoreCase("8")) || (score.equalsIgnoreCase("9")));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getRank() { return rank; }

    public void setRank(int rank) { this.rank = rank; }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getKiap() {
        return kiap;
    }

    public void setKiap(String kiap) {
        this.kiap = kiap;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getAttitude() {
        return attitude;
    }

    public void setAttitude(String attitude) {
        this.attitude = attitude;
    }

    public String getSparring() {
        return sparring;
    }

    public void setSparring(String sparring) {
        this.sparring = sparring;
    }

    public String getBreaking() {
        return breaking;
    }

    public void setBreaking(String breaking) {
        this.breaking = breaking;
    }
}
