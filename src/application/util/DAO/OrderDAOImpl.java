package application.util.DAO;

import application.model.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl {

    public void createOrderTable(){
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS orders (id int primary key unique auto_increment," +
                    "student_id int(6), firstName varchar(55), lastName varchar(55), date date, salePrice decimal(6,2), note varchar(55), complete boolean)");

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

    public void insert(Order order) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO orders (student_id, firstName, lastName, date, salePrice, note, complete)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, order.getStudentId());
            preparedStatement.setString(2, order.getFirstName());
            preparedStatement.setString(3, order.getLastName());
            preparedStatement.setDate(4, Date.valueOf(order.getDate()));
            preparedStatement.setBigDecimal(5, order.getSalePrice());
            preparedStatement.setString(6, order.getNote());
            preparedStatement.setBoolean(7, order.isComplete());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setId(generatedKeys.getInt(1));
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

    public void update(Order order, int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("UPDATE orders SET " +
                    "student_id = ?, firstName = ?, lastName = ?, date = ?, salePrice = ?, note = ?, complete = ? WHERE id = ?");
            preparedStatement.setInt(1, order.getStudentId());
            preparedStatement.setString(2, order.getFirstName());
            preparedStatement.setString(3, order.getLastName());
            preparedStatement.setDate(4, Date.valueOf(order.getDate()));
            preparedStatement.setBigDecimal(5, order.getSalePrice());
            preparedStatement.setString(6, order.getNote());
            preparedStatement.setBoolean(7, order.isComplete());
            preparedStatement.setInt(8, id);
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

    public Order selectById(int id) {
        Order order = new Order();
        Connection connection = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM orders WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                order.setId(resultSet.getInt("id"));
                order.setStudentId(resultSet.getInt("student_id"));
                order.setFirstName(resultSet.getString("firstName"));
                order.setLastName(resultSet.getString("lastName"));
                order.setDate(resultSet.getDate("date"));
                order.setSalePrice(resultSet.getBigDecimal("salePrice"));
                order.setNote(resultSet.getString("note"));
                order.setComplete(resultSet.getBoolean("complete"));
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

        return order;
    }

    public void deleteById(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM orders WHERE id = ?");
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

    private List<Order> selectAllOrders(boolean complete) {
        List<Order> orders = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM orders WHERE complete = ?");
            preparedStatement.setBoolean(1, complete);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Order order = new Order();
                order.setId(resultSet.getInt("id"));
                order.setStudentId(resultSet.getInt("student_id"));
                order.setFirstName(resultSet.getString("firstName"));
                order.setLastName(resultSet.getString("lastName"));
                order.setDate(resultSet.getDate("date"));
                order.setSalePrice(resultSet.getBigDecimal("salePrice"));
                order.setNote(resultSet.getString("note"));
                order.setComplete(resultSet.getBoolean("complete"));

                orders.add(order);
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

        return orders;
    }

    public ObservableList<Order> selectAllObservableOrders(boolean complete) {
        return FXCollections.observableArrayList(selectAllOrders(complete));
    }
}
