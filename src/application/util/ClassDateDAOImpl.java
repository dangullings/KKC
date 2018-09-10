package application.util;

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
                    "session_id int(6), date Date, second_hour boolean)");

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
            preparedStatement = connection.prepareStatement("INSERT INTO class_date (session_id, date, second_hour)" +
                    "VALUES (?, ?, ?)");
            preparedStatement.setInt(1, classDate.getSessionId());
            preparedStatement.setDate(2, Date.valueOf(classDate.getDate()));
            preparedStatement.setBoolean(3, classDate.hasSecondHour());
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

    public void delete(String inventoryId) {

    }

    public void update(Inventory inventory, int itemId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("UPDATE inventory SET " +
                    "item_id = ?, produced = ?, sold = ?, produced_cost = ?, sales_cost = ?, quantity = ? WHERE item_id = ?");
            preparedStatement.setInt(1, itemId);
            preparedStatement.setInt(2, inventory.getProduced());
            preparedStatement.setInt(3, inventory.getSold());
            preparedStatement.setBigDecimal(4, inventory.getProducedCost());
            preparedStatement.setBigDecimal(5, inventory.getSalesCost());
            preparedStatement.setInt(6, inventory.getQuantity());
            preparedStatement.setInt(7, itemId);
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
