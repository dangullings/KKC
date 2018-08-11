package application.model;

public class Test_Student {

    int id;
    int testId;
    int studentId;
    int rank;

    int form, steps, power, kiap, questions, attitude, sparring, breaking;

    public Test_Student() {

    }

    public Test_Student(int studentId) {
        this.studentId = studentId;
    }

    public Test_Student(int testId, int studentId) {
        this.testId = testId;
        this.studentId = studentId;
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

    public int getForm() {
        return form;
    }

    public void setForm(int form) {
        this.form = form;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getKiap() {
        return kiap;
    }

    public void setKiap(int kiap) {
        this.kiap = kiap;
    }

    public int getQuestions() {
        return questions;
    }

    public void setQuestions(int questions) {
        this.questions = questions;
    }

    public int getAttitude() {
        return attitude;
    }

    public void setAttitude(int attitude) {
        this.attitude = attitude;
    }

    public int getSparring() {
        return sparring;
    }

    public void setSparring(int sparring) {
        this.sparring = sparring;
    }

    public int getBreaking() {
        return breaking;
    }

    public void setBreaking(int breaking) {
        this.breaking = breaking;
    }
}
