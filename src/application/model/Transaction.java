package application.model;

import application.util.StudentDAOImpl;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class Transaction {

    private int id;
    private int studentId;
    private String firstName;
    private String lastName;
    private LocalDate date;
    private BigDecimal salePrice;
    private String note;

    public Transaction(){

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

    public void setDate(java.sql.Date date){
        this.date = date.toLocalDate();
    }

    public void setDate(LocalDate date){
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNote() { return note; }

    public void setNote(String note) { this.note = note; }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public void setStudentInfo(){
        StudentDAOImpl sdi = new StudentDAOImpl();

        Student student = sdi.selectById(getStudentId());

        setFirstName(student.getFirstName());
        setLastName(student.getLastName());
    }

    public void setSalePrice(ObservableList<LineItem> lineItems){
        salePrice = BigDecimal.ZERO;
        for (LineItem lineItem : lineItems){
            salePrice = salePrice.add(new BigDecimal(lineItem.getPrice().toString()));
        }
    }
}
