package application.util;

import application.model.Transaction;
import javafx.collections.ObservableList;

import java.util.List;

public interface TransactionDAO {
    void createTransactionTable();

    void insert(Transaction transaction);

    void update(Transaction transaction, int id);

    List<Transaction> selectAllTransactions();

    ObservableList<Transaction> selectAllObservableTransactions();
}
