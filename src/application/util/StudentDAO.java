package application.util;

import application.model.Student;
import javafx.collections.ObservableList;

import java.util.List;

public interface StudentDAO {

    void createStudentTable();

    void insert(Student student);

    Student selectById(int id);

    List<Student> selectAllInactive();
    List<Student> selectAllActive();

    ObservableList<Student> selectAllInactiveObservable();
    ObservableList<Student> selectAllActiveObservable();

    void delete(String firstName, String lastName);

    void update(Student student, int id);
}
