package application.util;

import application.model.Test;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TestDAOImpl implements TestDAO {

    int lastGeneratedId;

    @Override
    public void createTestTable() {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS test (id int primary key unique auto_increment," +
                    "type varchar(12), date date, location varchar(50), numStudents int(3))");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void insert(Test test) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO test (type, date, location, numStudents)" +
                    "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, test.getType());
            preparedStatement.setDate(2, Date.valueOf(test.getDate()));
            preparedStatement.setString(3, test.getLocation());
            preparedStatement.setInt(4, test.getNumStudents());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    test.setId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

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
    public Test selectById(int id) {
        Test test = new Test();
        Connection connection = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM test WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                test.setId(resultSet.getInt("id"));
                test.setType(resultSet.getString("type"));
                test.setDate(resultSet.getDate("date"));
                test.setLocation(resultSet.getString("location"));
                test.setNumStudents(resultSet.getInt("numStudents"));
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

        return test;
    }

    @Override
    public List<Test> selectAll() {
        List<Test> tests = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM test");

            while (resultSet.next()){
                Test test = new Test();
                test.setId(resultSet.getInt("id"));
                test.setType(resultSet.getString("type"));
                test.setDate(resultSet.getDate("date"));
                test.setLocation(resultSet.getString("location"));
                test.setNumStudents(resultSet.getInt("numStudents"));

                tests.add(test);
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

        return tests;
    }

    @Override
    public ObservableList<Test> selectAllObservable() {
        ObservableList<Test> tests = FXCollections.observableArrayList(selectAll());

        return tests;
    }

    @Override
    public void delete(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM test WHERE id = ?");
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

    @Override
    public void update(Test test, int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {//type, date, location, numStudents
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("UPDATE test SET " +
                    "type = ?, date = ?, location = ?, numStudents = ? WHERE id = ?");
            preparedStatement.setString(1, test.getType());
            preparedStatement.setDate(2, Date.valueOf(test.getDate()));
            preparedStatement.setString(3, test.getLocation());
            preparedStatement.setInt(4, test.getNumStudents());
            preparedStatement.setInt(5, id);
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

    public int getLastGeneratedId() {
        return lastGeneratedId;
    }

    public void setLastGeneratedId(int lastGeneratedId) {
        this.lastGeneratedId = lastGeneratedId;
    }
}
