package application.util.DAO;

import application.Main;
import application.model.Student;
import application.model.Test;
import application.model.Test_Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Test_StudentDAOImpl {

        public void createTest_StudentTable() {
            Connection connection = null;
            Statement statement = null;

            try {
                connection = DBUtil.getConnection();
                statement = connection.createStatement();
                statement.execute("CREATE TABLE IF NOT EXISTS test_student (id int primary key unique auto_increment," +
                        "test_id int(6), student_id int(6), rank_value int(3), form varchar(2), steps varchar(2), power varchar(2), kiap varchar(2), questions varchar(2), attitude varchar(2), sparring varchar(2), breaking varchar(2), FOREIGN KEY (test_id) REFERENCES test(id), FOREIGN KEY (student_id) REFERENCES student(id))");

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

    public void insert(Test_Student test_student) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO test_student (test_id, student_id, rank_value, form, steps, power, kiap, questions, attitude, sparring, breaking)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, test_student.getTestId());
            preparedStatement.setInt(2, test_student.getStudentId());
            preparedStatement.setInt(3, test_student.getRank());
            preparedStatement.setString(4, test_student.getForm());
            preparedStatement.setString(5, test_student.getSteps());
            preparedStatement.setString(6, test_student.getPower());
            preparedStatement.setString(7, test_student.getKiap());
            preparedStatement.setString(8, test_student.getQuestions());
            preparedStatement.setString(9, test_student.getAttitude());
            preparedStatement.setString(10, test_student.getSparring());
            preparedStatement.setString(11, test_student.getBreaking());
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

    public void update(Test_Student test_student, int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("UPDATE test_student SET " +
                    "test_id = ?, student_id = ?, rank_value = ?, form = ?, steps = ?, power = ?, kiap = ?, questions = ?, attitude = ?, sparring = ?, breaking = ? WHERE id = ?");
            preparedStatement.setInt(1, test_student.getTestId());
            preparedStatement.setInt(2, test_student.getStudentId());
            preparedStatement.setInt(3, test_student.getRank());
            preparedStatement.setString(4, test_student.getForm());
            preparedStatement.setString(5, test_student.getSteps());
            preparedStatement.setString(6, test_student.getPower());
            preparedStatement.setString(7, test_student.getKiap());
            preparedStatement.setString(8, test_student.getQuestions());
            preparedStatement.setString(9, test_student.getAttitude());
            preparedStatement.setString(10, test_student.getSparring());
            preparedStatement.setString(11, test_student.getBreaking());
            preparedStatement.setInt(12, id);
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

    public void delete(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM test_student WHERE id = ?");
            preparedStatement.setInt(1, id);
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

    public List<Student> selectAllStudentsByTestId(int testId) {
        List<Student> students = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM student join test_student on student.id = test_student.student_id WHERE test_id = ?");
            preparedStatement.setInt(1, testId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Student student = new Student();
                student.setId(resultSet.getInt("id"));
                student.setFirstName(resultSet.getString("first_name"));
                student.setLastName(resultSet.getString("last_name"));
                student.setRankName(resultSet.getString("rank_value"));
                student.setClub(resultSet.getString("club"));
                student.setEmail(resultSet.getString("email"));
                student.setNumber(resultSet.getString("number"));
                student.setBirthDate(resultSet.getDate("birthdate"));
                student.setActive(resultSet.getBoolean("active"));
                student.setRankValue(Main.Ranks.indexOf(student.getRankName()));

                students.add(student);
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

        return students;
    }

    public List<Test_Student> selectAllTest_StudentsByTestId(int testId) {
        List<Test_Student> test_students = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM test_student WHERE test_id = ?");
            preparedStatement.setInt(1, testId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Test_Student test_student = new Test_Student();
                test_student.setId(resultSet.getInt("id"));
                test_student.setStudentId(resultSet.getInt("student_id"));
                test_student.setTestId(resultSet.getInt("test_id"));
                test_student.setRank(resultSet.getInt("rank_value"));
                test_student.setForm(resultSet.getString("form"));
                test_student.setSteps(resultSet.getString("steps"));
                test_student.setPower(resultSet.getString("power"));
                test_student.setKiap(resultSet.getString("kiap"));
                test_student.setQuestions(resultSet.getString("questions"));
                test_student.setAttitude(resultSet.getString("attitude"));
                test_student.setSparring(resultSet.getString("sparring"));
                test_student.setBreaking(resultSet.getString("breaking"));

                test_students.add(test_student);
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

        return test_students;
    }

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
                test_student.setRank(resultSet.getInt("rank_value"));
                test_student.setForm(resultSet.getString("form"));
                test_student.setSteps(resultSet.getString("steps"));
                test_student.setPower(resultSet.getString("power"));
                test_student.setKiap(resultSet.getString("kiap"));
                test_student.setQuestions(resultSet.getString("questions"));
                test_student.setAttitude(resultSet.getString("attitude"));
                test_student.setSparring(resultSet.getString("sparring"));
                test_student.setBreaking(resultSet.getString("breaking"));

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

    public List<Test_Student> selectAllStudentTestScores(int student_id) {
        List<Test_Student> testScores = new ArrayList<Test_Student>();
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
                test_student.setRank(resultSet.getInt("rank_value"));
                test_student.setForm(resultSet.getString("form"));
                test_student.setSteps(resultSet.getString("steps"));
                test_student.setPower(resultSet.getString("power"));
                test_student.setKiap(resultSet.getString("kiap"));
                test_student.setQuestions(resultSet.getString("questions"));
                test_student.setAttitude(resultSet.getString("attitude"));
                test_student.setSparring(resultSet.getString("sparring"));
                test_student.setBreaking(resultSet.getString("breaking"));

                testScores.add(test_student);
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

        return testScores;
    }

    public void deleteByStudentId(int studentId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM test_student WHERE student_id = ?");
            preparedStatement.setInt(1, studentId);
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

    public void deleteByTestId(int testId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM test_student WHERE test_id = ?");
            preparedStatement.setInt(1, testId);
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

    public ObservableList<Test> selectAllObservable(Student student) {
        return FXCollections.observableArrayList(selectAllStudentTests(student.getId()));
    }

    public ObservableList<Test_Student> selectAllObservableScores(Student student) {
        return FXCollections.observableArrayList(selectAllStudentTestScores(student.getId()));
    }

    public ObservableList<Student> selectAllObservableStudentsByTestId(int testId) {
        return FXCollections.observableArrayList(selectAllStudentsByTestId(testId));
    }

    public ObservableList<Test_Student> selectAllObservableTest_StudentsByTestId(int testId) {
        return FXCollections.observableArrayList(selectAllTest_StudentsByTestId(testId));
    }

}
