package application.util;

import application.model.Student;
import application.model.Test;
import application.model.Test_Student;
import javafx.collections.ObservableList;

import java.util.List;

public interface Test_StudentDAO {

    void createTest_StudentTable();

    void insert(Test_Student test_student);
    void update(Test_Student test_student, int id);
    void deleteByStudentId(int studentId);
    void deleteByTestId(int studentId);
    void delete(int id);

    List<Student> selectAllStudentsByTestId(int id);
    List<Test> selectAllStudentTests(int id);
    List<Test_Student> selectAllStudentTestScores(int id);
    List<Test_Student> selectAllTest_StudentsByTestId(int id);

    ObservableList<Test> selectAllObservable(Student student);
    ObservableList<Test_Student> selectAllObservableScores(Student student);
    ObservableList<Student> selectAllObservableStudentsByTestId(int testId);
    ObservableList<Test_Student> selectAllObservableTest_StudentsByTestId(int testId);
}
