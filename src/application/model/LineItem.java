package application.model;

import application.util.ItemDAOImpl;

import java.math.BigDecimal;

public class LineItem {

    private int id;
    private int transactionId;
    private int itemId;
    private String itemName;
    private BigDecimal price;
    private int quantity;

    public LineItem(){

    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setItemInfo(){
        //ItemDAOImpl idi = new ItemDAOImpl();

        //Item item = idi.selectById(getItemId());

        //setItemName(item.getName());
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }
}
