package application.util;

import application.model.Student;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Observable;

public interface StudentDAO {

    void createStudentTable();

    void insert(Student student);

    Student selectById(int id);

    List<Student> selectAll();

    ObservableList<Student> selectAllObservable();

    void delete(String firstName, String lastName);

    void update(Student student, int id);
}
