package application.model;

public class Item {

    private int id;
    private String name;
    private int quantity;
    private int cost;
    private int sale;

    public Item(){

    }

    public Item(String name, int quantity, int cost, int sale){
        this.name = name;
        this.quantity = quantity;
        this.cost = cost;
        this.sale = sale;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }
}
