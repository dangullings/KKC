package application.model;

import application.util.LineItemDAOImpl;
import application.util.StudentDAOImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Transaction {

    private int id;
    private int studentId;
    private String firstName;
    private String lastName;
    private LocalDate date;
    private BigDecimal salePrice;

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

    public void setSalePrice(ArrayList<LineItem> lineItems){
        salePrice = BigDecimal.ZERO;
        for (LineItem lineItem : lineItems){
            salePrice = salePrice.add(new BigDecimal(lineItem.getPrice().toString()));
        }

        System.out.println(salePrice + "");
    }
}
