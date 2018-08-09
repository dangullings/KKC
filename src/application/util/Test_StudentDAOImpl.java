package application.util;

import application.model.Student;
import application.model.Test;
import application.model.Test_Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Test_StudentDAOImpl implements Test_StudentDAO{

        @Override
        public void createTest_StudentTable() {
            Connection connection = null;
            Statement statement = null;

            try {
                connection = DBUtil.getConnection();
                statement = connection.createStatement();
                statement.execute("CREATE TABLE IF NOT EXISTS test_student (id int primary key unique auto_increment," +
                        "test_id int(6), student_id int(6), score1 int(3), score2 int(3), score3 int(3))");

            }catch (Exception e) {
                e.printStackTrace();
            }finally{
                if (statement != null){
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (connection != null){
                    try {
                        connection.close();
                    } catch (SQLException e){
                        e.printStackTrace();
                    }

                }
            }
        }

    @Override
    public void insert(Test_Student test_student) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO test_student (test_id, student_id, score1, score2, score3)" +
                    "VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, test_student.getTestId());
            preparedStatement.setInt(2, test_student.getStudentId());
            preparedStatement.setInt(3, test_student.getScore1());
            preparedStatement.setInt(4, test_student.getScore2());
            preparedStatement.setInt(5, test_student.getScore3());
            preparedStatement.executeUpdate();

        } catch (Exception e){
            e.printStackTrace();
        } finally{
            if (preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<Test> selectAllStudentTests(int student_id) {
        List<Test_Student> test_studentList = new ArrayList<Test_Student>();
        List<Test> tests = new ArrayList<Test>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM test_student WHERE student_id = ?");
            preparedStatement.setInt(1, student_id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Test_Student test_student = new Test_Student();
                test_student.setId(resultSet.getInt("id"));
                test_student.setStudentId(resultSet.getInt("student_id"));
                test_student.setTestId(resultSet.getInt("test_id"));
                test_student.setScore1(resultSet.getInt("score1"));
                test_student.setScore2(resultSet.getInt("score2"));
                test_student.setScore3(resultSet.getInt("score3"));

                test_studentList.add(test_student);
            }

        } catch (Exception e){
            e.printStackTrace();
        }finally{
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        TestDAOImpl tdi = new TestDAOImpl();
        Test test;
        for (Test_Student test_student : test_studentList){
            test = tdi.selectById(test_student.getTestId());
            tests.add(test);
        }

        return tests;
    }

    @Override
    public ObservableList<Test> selectAllObservable(Student student) {
        ObservableList<Test> tests = FXCollections.observableArrayList(selectAllStudentTests(student.getId()));

        return tests;

    }

}
