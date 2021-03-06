package application.model;

import java.time.LocalDate;

public class ClassDate implements Comparable<ClassDate>{

    int id;
    private int sessionId;
    private String location;
    private LocalDate date;
    private boolean secondHour;
    private String style;
    private Boolean complete;

    public ClassDate(){
        complete = false;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean hasSecondHour() {
        return secondHour;
    }

    public void setSecondHour(boolean secondHour) {
        this.secondHour = secondHour;
    }

    public void setDate(int year, int month, int day){
        date = LocalDate.of(year, month, day);
    }

    public void setDate(java.sql.Date date){
        this.date = date.toLocalDate();
    }

    public void setDate(LocalDate date){
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStyle(String style){
        this.style = style;
    }

    public String getStyle(){
        return style;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    @Override
    public int compareTo(ClassDate o) {
        if (getDate().getDayOfMonth() < o.getDate().getDayOfMonth()){
            return -1;
        }else{
            return 1;
        }
    }
}
