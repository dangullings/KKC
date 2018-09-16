package application.util;

import application.model.ClassSession;
import application.model.Inventory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClassSessionDAOImpl {

    public void createClassSessionTable(){
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS class_session (id int primary key unique auto_increment," +
                    "location varchar(50), start_date Date, end_date Date)");

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

    public void insert(ClassSession classSession) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO class_session (location, start_date, end_date)" +
                    "VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, classSession.getLocation());
            preparedStatement.setDate(2, Date.valueOf(classSession.getStartDate()));
            preparedStatement.setDate(3, Date.valueOf(classSession.getEndDate()));
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    classSession.setId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating session failed, no ID obtained.");
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

    public ClassSession selectById(int id) {
        ClassSession classSession = new ClassSession();
        Connection connection = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM class_session WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                classSession.setId(resultSet.getInt("id"));
                classSession.setLocation(resultSet.getString("location"));
                classSession.setStartDate(resultSet.getDate("start_date"));
                classSession.setEndDate(resultSet.getDate("end_date"));
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

        return classSession;
    }

    public List<ClassSession> selectAll() {
        List<ClassSession> classSessions = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM class_session");

            while (resultSet.next()){
                ClassSession classSession = new ClassSession();
                classSession.setId(resultSet.getInt("id"));
                classSession.setLocation(resultSet.getString("location"));
                classSession.setStartDate(resultSet.getDate("start_date"));
                classSession.setEndDate(resultSet.getDate("end_date"));

                classSessions.add(classSession);
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

        return classSessions;
    }

    public ObservableList<ClassSession> selectAllObservable() {
        ObservableList<ClassSession> classSessions = FXCollections.observableArrayList(selectAll());

        return classSessions;
    }

    public void deleteById(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM class_session WHERE id = ?");
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

    public void update(ClassSession classSession, int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("UPDATE class_session SET " +
                    "location = ?, start_date = ?, end_date = ? WHERE id = ?");
            preparedStatement.setString(1, classSession.getLocation());
            preparedStatement.setDate(2, Date.valueOf(classSession.getStartDate()));
            preparedStatement.setDate(3, Date.valueOf(classSession.getEndDate()));
            preparedStatement.setInt(4, id);
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
