package application.util;

import application.model.LineItem;
import javafx.collections.ObservableList;

import java.util.List;

public interface LineItemDAO {

    void createLineItemTable();

    void insert(LineItem item);

    List<LineItem> selectAllLineItems();

    List<LineItem> selectAllLineItemsByItemId(int itemId);
    List<LineItem> selectAllLineItemsByTransactionId(int transactionId);

    ObservableList<LineItem> selectAllObservableByItemId(int id);
    ObservableList<LineItem> selectAllObservableLineItems();
}
