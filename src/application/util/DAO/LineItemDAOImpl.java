package application.util.DAO;

import application.model.LineItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LineItemDAOImpl {

    public void createLineItemTable() {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS line_item (id int primary key unique auto_increment," + // constraint line_item_pk primary key(transaction_id,item_id),
                    "order_id int(6), item_id int(6), quantity int(6), price decimal(6,2), item_name varchar(55), FOREIGN KEY (order_id) REFERENCES orders(id), FOREIGN KEY (item_id) REFERENCES item(id))");

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

    public void insert(LineItem lineItem) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO line_item (order_id, item_id, quantity, price, item_name)" +
                    "VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, lineItem.getOrderId());
            preparedStatement.setInt(2, lineItem.getItemId());
            preparedStatement.setInt(3, lineItem.getQuantity());
            preparedStatement.setBigDecimal(4, lineItem.getPrice());
            preparedStatement.setString(5, lineItem.getItemName());
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

    public void update(LineItem lineItem, int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("UPDATE line_item SET " +
                    "order_id = ?, item_id = ?, quantity = ?, price = ?, item_name = ? WHERE id = ?");
            preparedStatement.setInt(1, lineItem.getOrderId());
            preparedStatement.setInt(2, lineItem.getItemId());
            preparedStatement.setInt(3, lineItem.getQuantity());
            preparedStatement.setBigDecimal(4, lineItem.getPrice());
            preparedStatement.setString(5, lineItem.getItemName());
            preparedStatement.setInt(6, id);
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
            preparedStatement = connection.prepareStatement("DELETE FROM line_item WHERE id = ?");
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

    public List<LineItem> selectAllLineItemsByItemId(int itemId) {
        List<LineItem> lineItems = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM line_item WHERE item_id = ?");
            preparedStatement.setInt(1, itemId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                LineItem lineItem = new LineItem();
                lineItem.setId(resultSet.getInt("id"));
                lineItem.setOrderId(resultSet.getInt("order_id"));
                lineItem.setItemId(resultSet.getInt("item_id"));
                lineItem.setQuantity(resultSet.getInt("quantity"));
                lineItem.setPrice(resultSet.getBigDecimal("price"));
                lineItem.setItemName(resultSet.getString("item_name"));

                lineItem.setItemInfo();

                lineItems.add(lineItem);
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

        return lineItems;
    }

    public List<LineItem> selectAllLineItemsByItemIdComplete(int itemId, boolean isComplete) {
        List<LineItem> lineItems = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM line_item join orders on line_item.order_id = orders.id WHERE item_id = ? AND orders.complete = ?");
            preparedStatement.setInt(1, itemId);
            preparedStatement.setBoolean(2, isComplete);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                LineItem lineItem = new LineItem();
                lineItem.setId(resultSet.getInt("id"));
                lineItem.setOrderId(resultSet.getInt("order_id"));
                lineItem.setItemId(resultSet.getInt("item_id"));
                lineItem.setQuantity(resultSet.getInt("quantity"));
                lineItem.setPrice(resultSet.getBigDecimal("price"));
                lineItem.setItemName(resultSet.getString("item_name"));

                lineItem.setItemInfo();

                lineItems.add(lineItem);
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

        return lineItems;
    }

    public List<LineItem> selectAllLineItemsByOrderId(int orderId) {
        List<LineItem> lineItems = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM line_item WHERE order_id = ?");
            preparedStatement.setInt(1, orderId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                LineItem lineItem = new LineItem();
                lineItem.setId(resultSet.getInt("id"));
                lineItem.setOrderId(resultSet.getInt("order_id"));
                lineItem.setItemId(resultSet.getInt("item_id"));
                lineItem.setQuantity(resultSet.getInt("quantity"));
                lineItem.setPrice(resultSet.getBigDecimal("price"));
                lineItem.setItemName(resultSet.getString("item_name"));

                lineItem.setItemInfo();

                lineItems.add(lineItem);
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

        return lineItems;
    }

    public List<LineItem> selectAllLineItems() {
        List<LineItem> lineItems = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM line_item");

            while (resultSet.next()){
                LineItem lineItem = new LineItem();
                lineItem.setId(resultSet.getInt("id"));
                lineItem.setOrderId(resultSet.getInt("order_id"));
                lineItem.setItemId(resultSet.getInt("item_id"));
                lineItem.setQuantity(resultSet.getInt("quantity"));
                lineItem.setPrice(resultSet.getBigDecimal("price"));
                lineItem.setItemName(resultSet.getString("item_name"));

                lineItem.setItemInfo();

                lineItems.add(lineItem);
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

        return lineItems;
    }

    public ObservableList<LineItem> selectAllObservableLineItems() {
        return FXCollections.observableArrayList(selectAllLineItems());
    }

    public ObservableList<LineItem> selectAllObservableByItemId(int id) {
        return FXCollections.observableArrayList(selectAllLineItemsByItemId(id));
    }

    public ObservableList<LineItem> selectAllObservableByItemIdComplete(int id, boolean isComplete) {
        return FXCollections.observableArrayList(selectAllLineItemsByItemIdComplete(id, isComplete));
    }

    public ObservableList<LineItem> selectAllObservableByOrderId(int id) {
        return FXCollections.observableArrayList(selectAllLineItemsByOrderId(id));
    }
}
