package application.model;

public class DemoPointAwarded {

    int id;
    int studentId;
    String info;
    String name;
    int value;

    public DemoPointAwarded(){

    }

    public DemoPointAwarded(int studentId, String name, String info, int value) {
        this.studentId = studentId;
        this.name = name;
        this.info = info;
        this.value = value;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
