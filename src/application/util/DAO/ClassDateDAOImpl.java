package application.util.DAO;

import application.model.ClassDate;
import application.model.Inventory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClassDateDAOImpl {

    public void createClassTable(){
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS class_date (id int primary key unique auto_increment," +
                    "session_id int(6), date Date, second_hour boolean, complete boolean, FOREIGN KEY (session_id) REFERENCES class_session(id))");

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

    public void insert(ClassDate classDate) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO class_date (session_id, date, second_hour, complete)" +
                    "VALUES (?, ?, ?, ?)");
            preparedStatement.setInt(1, classDate.getSessionId());
            preparedStatement.setDate(2, Date.valueOf(classDate.getDate()));
            preparedStatement.setBoolean(3, classDate.hasSecondHour());
            preparedStatement.setBoolean(4, classDate.getComplete());
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

    public ClassDate selectById(int id) {
        ClassDate classDate = new ClassDate();
        Connection connection = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM class_date WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                classDate.setId(resultSet.getInt("id"));
                classDate.setSessionId(resultSet.getInt("session_id"));
                classDate.setDate(resultSet.getDate("date"));
                classDate.setSecondHour(resultSet.getBoolean("second_hour"));
                classDate.setComplete(resultSet.getBoolean("complete"));
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

        return classDate;
    }

    public List<ClassDate> selectAllBySessionId(int sessionId) {
        List<ClassDate> classDates = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM class_date WHERE session_id = ?");
            preparedStatement.setInt(1, sessionId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                ClassDate classDate = new ClassDate();
                classDate.setId(resultSet.getInt("id"));
                classDate.setSessionId(resultSet.getInt("session_id"));
                classDate.setDate(resultSet.getDate("date"));
                classDate.setSecondHour(resultSet.getBoolean("second_hour"));
                classDate.setComplete(resultSet.getBoolean("complete"));

                classDates.add(classDate);
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

        return classDates;
    }

    public List<ClassDate> selectAllByDate(LocalDate startDate, LocalDate endDate) {
        List<ClassDate> classDates = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM class_date WHERE date between ? AND ?");
            preparedStatement.setDate(1, Date.valueOf(startDate));
            preparedStatement.setDate(2, Date.valueOf(endDate));
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                ClassDate classDate = new ClassDate();
                classDate.setId(resultSet.getInt("id"));
                classDate.setSessionId(resultSet.getInt("session_id"));
                classDate.setDate(resultSet.getDate("date"));
                classDate.setSecondHour(resultSet.getBoolean("second_hour"));
                classDate.setComplete(resultSet.getBoolean("complete"));

                classDates.add(classDate);
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

        return classDates;
    }

    public List<ClassDate> selectAll() {
        List<ClassDate> classDates = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM class_date");

            while (resultSet.next()){
                ClassDate classDate = new ClassDate();
                classDate.setId(resultSet.getInt("id"));
                classDate.setSessionId(resultSet.getInt("session_id"));
                classDate.setDate(resultSet.getDate("date"));
                classDate.setSecondHour(resultSet.getBoolean("second_hour"));
                classDate.setComplete(resultSet.getBoolean("complete"));

                classDates.add(classDate);
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

        return classDates;
    }

    public ObservableList<ClassDate> selectAllObservable() {
        ObservableList<ClassDate> classDates = FXCollections.observableArrayList(selectAll());

        return classDates;
    }

    public ObservableList<ClassDate> selectAllObservableBySessionId(int id) {
        ObservableList<ClassDate> classDates = FXCollections.observableArrayList(selectAllBySessionId(id));

        return classDates;
    }

    public void deleteById(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM class_date WHERE id = ?");
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

    public void update(ClassDate classDate, int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("UPDATE class_date SET " +
                    "session_id = ?, date = ?, second_hour = ?, complete = ? WHERE id = ?");
            preparedStatement.setInt(1, classDate.getSessionId());
            preparedStatement.setDate(2, Date.valueOf(classDate.getDate()));
            preparedStatement.setBoolean(3, classDate.hasSecondHour());
            preparedStatement.setBoolean(4, classDate.getComplete());
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
}
