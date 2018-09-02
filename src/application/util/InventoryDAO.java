package application.util;

import application.model.Inventory;
import javafx.collections.ObservableList;

import java.util.List;

public interface InventoryDAO {

    void createInventoryTable();

    void insert(Inventory item);

    Inventory selectById(int id);

    List<Inventory> selectAll();

    ObservableList<Inventory> selectAllObservable();

    void delete(String inventoryId);

    void update(Inventory inventory, int id);
}
