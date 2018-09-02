package application.model;

import java.math.BigDecimal;

public class Item {

    private int id;
    private String name;
    private BigDecimal produceCost;
    private BigDecimal saleCost;
    private String description;

    public Item(){

    }

    public Item(String name, BigDecimal produceCost, BigDecimal saleCost, String description){
        this.name = name;
        this.produceCost = produceCost;
        this.saleCost = saleCost;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getProduceCost() {
        return produceCost;
    }

    public void setProduceCost(BigDecimal produceCost) {
        this.produceCost = produceCost;
    }

    public BigDecimal getSaleCost() {
        return saleCost;
    }

    public void setSaleCost(BigDecimal saleCost) {
        this.saleCost = saleCost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
