package application.util;

import application.Main;
import application.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl {

    public void createStudentTable() {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS student (id int primary key unique auto_increment," +
                    "first_name varchar(55), last_name varchar(55), rank varchar(55), club varchar(55), email varchar(55), number varchar(15), birthdate date, active boolean)");

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

    public void insert(Student student) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO student (first_name, last_name, rank, club, email, number, birthdate, active)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setString(3, student.getRankName());
            preparedStatement.setString(4, student.getClub());
            preparedStatement.setString(5, student.getEmail());
            preparedStatement.setString(6, student.getNumber());
            preparedStatement.setDate(7, Date.valueOf(student.getBirthDate()));
            preparedStatement.setBoolean(8, student.getActive());
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

    public Student selectById(int id) {
        Student student = new Student();
        Connection connection = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM student WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                student.setId(resultSet.getInt("id"));
                student.setFirstName(resultSet.getString("first_name"));
                student.setLastName(resultSet.getString("last_name"));
                student.setRankName(resultSet.getString("rank"));
                student.setClub(resultSet.getString("club"));
                student.setEmail(resultSet.getString("email"));
                student.setNumber(resultSet.getString("number"));
                student.setBirthDate(resultSet.getDate("birthdate"));
                student.setActive(resultSet.getBoolean("active"));
                student.setRankValue(Main.Ranks.indexOf(student.getRankName()));
                student.setRankNameRounded();
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

        return student;
    }

    public List<Student> selectAllInactive() {
        List<Student> students = new ArrayList<Student>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM student WHERE active = 0");

            while (resultSet.next()){
                Student student = new Student();
                student.setId(resultSet.getInt("id"));
                student.setFirstName(resultSet.getString("first_name"));
                student.setLastName(resultSet.getString("last_name"));
                student.setRankName(resultSet.getString("rank"));
                student.setClub(resultSet.getString("club"));
                student.setEmail(resultSet.getString("email"));
                student.setNumber(resultSet.getString("number"));
                student.setBirthDate(resultSet.getDate("birthdate"));
                student.setActive(resultSet.getBoolean("active"));
                student.setRankValue(Main.Ranks.indexOf(student.getRankName()));
                student.setRankNameRounded();

                students.add(student);
            }

        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

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
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return students;
    }

    public List<Student> selectAllActive() {
        List<Student> students = new ArrayList<Student>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM student WHERE active = 1");

            while (resultSet.next()){
                Student student = new Student();
                student.setId(resultSet.getInt("id"));
                student.setFirstName(resultSet.getString("first_name"));
                student.setLastName(resultSet.getString("last_name"));
                student.setRankName(resultSet.getString("rank"));
                student.setClub(resultSet.getString("club"));
                student.setEmail(resultSet.getString("email"));
                student.setNumber(resultSet.getString("number"));
                student.setBirthDate(resultSet.getDate("birthdate"));
                student.setActive(resultSet.getBoolean("active"));
                student.setRankValue(Main.Ranks.indexOf(student.getRankName()));
                student.setRankNameRounded();

                students.add(student);
            }

        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

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
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return students;
    }

    public ObservableList<Student> selectAllInactiveObservable() {
        return FXCollections.observableArrayList(selectAllInactive());

    }

    public ObservableList<Student> selectAllActiveObservable() {
        return FXCollections.observableArrayList(selectAllActive());

    }

    public void delete(String firstName, String lastName) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM student WHERE first_name = ? AND last_name = ?");
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
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

    public void update(Student student, int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("UPDATE student SET " +
                    "first_name = ?, last_name = ?, rank = ?, club = ?, email = ?, number = ?, birthdate = ?, active = ? WHERE id = ?");
            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setString(3, student.getRankName());
            preparedStatement.setString(4, student.getClub());
            preparedStatement.setString(5, student.getEmail());
            preparedStatement.setString(6, student.getNumber());
            preparedStatement.setDate(7, Date.valueOf(student.getBirthDate()));
            preparedStatement.setBoolean(8, student.getActive());
            preparedStatement.setInt(9, id);
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
}
