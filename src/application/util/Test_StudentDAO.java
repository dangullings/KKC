package application.util;

import application.model.Student;
import application.model.Test;
import application.model.Test_Student;
import javafx.collections.ObservableList;

import java.util.List;

public interface Test_StudentDAO {

    void createTest_StudentTable();

    void insert(Test_Student test_student);

    void deleteByStudentId(int studentId);

    List<Test> selectAllStudentTests(int id);
    List<Test_Student> selectAllStudentTestScores(int id);

    ObservableList<Test> selectAllObservable(Student student);
    ObservableList<Test_Student> selectAllObservableScores(Student student);
}
