package application.util.DAO;

import application.model.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl {

    public void createItemTable() {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS item (id int primary key unique auto_increment," +
                    "name varchar(55), produce_cost decimal(6,2), sale_cost decimal(6,2), description varchar(55))");

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

    public void insert(Item item) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO item (name, produce_cost, sale_cost, description)" +
                    "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, item.getName());
            preparedStatement.setBigDecimal(2, item.getProduceCost());
            preparedStatement.setBigDecimal(3, item.getSaleCost());
            preparedStatement.setString(4, item.getDescription());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating item failed, no ID obtained.");
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

    public Item selectById(int id) {
        Item item = new Item();
        Connection connection = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM item WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                item.setId(resultSet.getInt("id"));
                item.setName(resultSet.getString("name"));
                item.setProduceCost(resultSet.getBigDecimal("produce_cost"));
                item.setSaleCost(resultSet.getBigDecimal("sale_cost"));
                item.setDescription(resultSet.getString("description"));
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

        return item;
    }

    public List<Item> selectAll() {
        List<Item> items = new ArrayList<Item>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM item");

            while (resultSet.next()){
                Item item = new Item();
                item.setId(resultSet.getInt("id"));
                item.setName(resultSet.getString("name"));
                item.setProduceCost(resultSet.getBigDecimal("produce_cost"));
                item.setSaleCost(resultSet.getBigDecimal("sale_cost"));
                item.setDescription(resultSet.getString("description"));

                items.add(item);
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

        return items;
    }

    public ObservableList<Item> selectAllObservable() {
        ObservableList<Item> items = FXCollections.observableArrayList(selectAll());

        return items;

    }

    public void delete(String name) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM item WHERE name = ?");
            preparedStatement.setString(1, name);
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

    public void update(Item item, int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("UPDATE item SET " +
                    "name = ?, produce_cost = ?, sale_cost = ?, description = ? WHERE id = ?");
            preparedStatement.setString(1, item.getName());
            preparedStatement.setBigDecimal(2, item.getProduceCost());
            preparedStatement.setBigDecimal(3, item.getSaleCost());
            preparedStatement.setString(4, item.getDescription());
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
