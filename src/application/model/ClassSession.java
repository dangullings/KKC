package application.model;

import java.time.LocalDate;

public class ClassSession {

    int id;
    String location;
    LocalDate startDate;
    LocalDate endDate;

    public ClassSession(){

    }

    public ClassSession(String location, LocalDate startDate, LocalDate endDate) {
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(java.sql.Date date){
        this.startDate = date.toLocalDate();
    }

    public void setEndDate(java.sql.Date date){
        this.endDate = date.toLocalDate();
    }
}
