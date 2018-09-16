package application.model;

public class Attendance {

    private int id;
    private int studentId;
    private int classDateId;
    private boolean firstHour;
    private boolean secondHour;

    public Attendance(){

    }

    public Attendance(int id, int studentId, int classDateId, boolean firstHour, boolean secondHour) {
        this.id = id;
        this.studentId = studentId;
        this.classDateId = classDateId;
        this.firstHour = firstHour;
        this.secondHour = secondHour;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getClassDateId() {
        return classDateId;
    }

    public void setClassDateId(int classDateId) {
        this.classDateId = classDateId;
    }

    public boolean isFirstHour() {
        return firstHour;
    }

    public void setFirstHour(boolean firstHour) {
        this.firstHour = firstHour;
    }

    public boolean isSecondHour() {
        return secondHour;
    }

    public void setSecondHour(boolean secondHour) {
        this.secondHour = secondHour;
    }
}
