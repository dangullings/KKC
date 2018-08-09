package application.util;

import application.model.Student;
import application.model.Test;
import application.model.Test_Student;
import javafx.collections.ObservableList;

import java.util.List;

public interface Test_StudentDAO {

    void createTest_StudentTable();

    void insert(Test_Student test_student);

    List<Test> selectAllStudentTests(int id);

    ObservableList<Test> selectAllObservable(Student student);
}
