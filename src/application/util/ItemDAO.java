package application.util;

import application.model.Item;
import javafx.collections.ObservableList;

import java.util.List;

public interface ItemDAO {

    void createItemTable();

    void insert(Item item);

    Item selectById(int id);

    List<Item> selectAll();

    ObservableList<Item> selectAllObservable();

    void delete(String itemName);

    void update(Item item, int id);
}
