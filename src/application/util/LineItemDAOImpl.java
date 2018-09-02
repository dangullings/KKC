package application.util;

import application.model.LineItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LineItemDAOImpl implements LineItemDAO {

    @Override
    public void createLineItemTable() {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS line_item (id int primary key unique auto_increment," + // constraint line_item_pk primary key(transaction_id,item_id),
                    "transaction_id int(6), item_id int(6), quantity int(6), price decimal(6,2))");

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
    public void insert(LineItem lineItem) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO line_item (transaction_id, item_id, quantity, price)" +
                    "VALUES (?, ?, ?, ?)");
            preparedStatement.setInt(1, lineItem.getTransactionId());
            preparedStatement.setInt(2, lineItem.getItemId());
            preparedStatement.setInt(3, lineItem.getQuantity());
            preparedStatement.setBigDecimal(4, lineItem.getPrice());
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
                lineItem.setTransactionId(resultSet.getInt("transaction_id"));
                lineItem.setItemId(resultSet.getInt("item_id"));
                lineItem.setQuantity(resultSet.getInt("quantity"));
                lineItem.setPrice(resultSet.getBigDecimal("price"));

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

    @Override
    public List<LineItem> selectAllLineItemsByTransactionId(int transactionId) {
        List<LineItem> lineItems = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM line_item WHERE transaction_id = ?");
            preparedStatement.setInt(1, transactionId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                LineItem lineItem = new LineItem();
                lineItem.setId(resultSet.getInt("id"));
                lineItem.setTransactionId(resultSet.getInt("transaction_id"));
                lineItem.setItemId(resultSet.getInt("item_id"));
                lineItem.setQuantity(resultSet.getInt("quantity"));
                lineItem.setPrice(resultSet.getBigDecimal("price"));

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

    @Override
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
                lineItem.setTransactionId(resultSet.getInt("transaction_id"));
                lineItem.setItemId(resultSet.getInt("item_id"));
                lineItem.setQuantity(resultSet.getInt("quantity"));
                lineItem.setPrice(resultSet.getBigDecimal("price"));

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

    @Override
    public ObservableList<LineItem> selectAllObservableLineItems() {
        return FXCollections.observableArrayList(selectAllLineItems());
    }

    @Override
    public ObservableList<LineItem> selectAllObservableByItemId(int id) {
        return FXCollections.observableArrayList(selectAllLineItemsByItemId(id));
    }
}
