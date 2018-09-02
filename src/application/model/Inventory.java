package application.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

public class Inventory {

    private ItemView itemView;

    int itemId;
    int produced;
    int sold;
    BigDecimal salesCost;
    BigDecimal producedCost;
    int quantity;

    public Inventory (){
        itemView = new ItemView();
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getProduced() {
        return produced;
    }

    public void setProduced(int produced) {
        this.produced = produced;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public BigDecimal getSalesCost() {
        return salesCost;
    }

    public void setSalesCost(BigDecimal salesCost) {
        this.salesCost = salesCost;
    }

    public BigDecimal getProducedCost() {
        return producedCost;
    }

    public void setProducedCost(BigDecimal producedCost) {
        this.producedCost = producedCost;
    }

    public void sellItem(int qty, BigDecimal saleCost){
        if (qty > quantity){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Item will need " + (qty - quantity) + " more produced.");
            Optional<ButtonType> action = alert.showAndWait();

            if (action.get() == ButtonType.OK){
            }

            quantity = 0;
        }else{
            quantity -= qty;
        }

        sold += qty;
        salesCost = salesCost.add(new BigDecimal(saleCost.toString()));
    }

    public void addItem(Item item, int qty){
        setItemId(item.getId());
        setProduced(qty);
        setQuantity(qty);
        setSold(0);
        setProducedCost(item.getProduceCost().multiply(BigDecimal.valueOf(qty)));
        setSalesCost(BigDecimal.ZERO);
    }

    public void updateItem(Item item, int oldQty, int newQty){
        int updatedQty = 0;

        updatedQty = (newQty - oldQty);

        setProduced(getProduced() + updatedQty);
        setQuantity(newQty);

        //salePrice = salePrice.add(new BigDecimal(lineItem.getPrice().toString()));

        //producedCost = producedCost.add(new BigDecimal(item.getProduceCost().multiply(BigDecimal.valueOf(newQty)).toString()));
        //setProducedCost(newProduceCost.add(item.getProduceCost().multiply(BigDecimal.valueOf(newQty))));
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Inventory.ItemView getItemView(){
        return itemView;
    }

    public void setItemViews(Item item, Inventory inventory){
        ItemView itemView = new ItemView();

        itemView.setId(item.getId());
        itemView.setName(item.getName());
        itemView.setProduced(inventory.getProduced());
        itemView.setSold(inventory.getSold());
        itemView.setQuantity(inventory.getQuantity());
        itemView.setDescription(item.getDescription());

        this.itemView = itemView;
    }

    public class ItemView{

        int id;
        String name, description;
        int produced, sold, quantity;

        public ItemView(){

        }

        public ItemView(int id, String name, int produced, int sold, int quantity, String description){
            this.id = id;
            this.name = name;
            this.produced = produced;
            this.sold = sold;
            this.quantity = quantity;
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

        public int getProduced() {
            return produced;
        }

        public void setProduced(int produced) {
            this.produced = produced;
        }

        public int getSold() {
            return sold;
        }

        public void setSold(int sold) {
            this.sold = sold;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
