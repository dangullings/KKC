package application.model;

import java.time.LocalDate;

public class Test {

    private int id;
    private String type;
    private LocalDate date;
    private String location;

    //ArrayList<Student> students;

    public Test(){

    }

    public Test(String type, LocalDate date, String location) {
        this.type = type;
        this.date = date;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
