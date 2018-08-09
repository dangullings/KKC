package application.util;

import application.model.Test;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public interface TestDAO {

    void createTestTable() throws SQLException;

    void insert(Test test);

    Test selectById(int id);

    List<Test> selectAll();

    ObservableList<Test> selectAllObservable();

    void delete();

    void update(Test test, int id);
}
