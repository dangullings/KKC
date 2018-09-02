package application.util;

import application.model.Inventory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAOImpl implements InventoryDAO {

    @Override
    public void createInventoryTable(){
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS inventory (constraint inventory_pk primary key(item_id)," +
                    "item_id int(6), produced int(6), sold int(6), produced_cost decimal(6,2), sales_cost decimal(6,2), quantity int(6))");

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
    public void insert(Inventory inventory) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO inventory (item_id, produced, sold, produced_cost, sales_cost, quantity)" +
                    "VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, inventory.getItemId());
            preparedStatement.setInt(2, inventory.getProduced());
            preparedStatement.setInt(3, inventory.getSold());
            preparedStatement.setBigDecimal(4, inventory.getProducedCost());
            preparedStatement.setBigDecimal(5, inventory.getSalesCost());
            preparedStatement.setInt(6, inventory.getQuantity());
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
    public Inventory selectById(int id) {
        Inventory inventory = new Inventory();
        Connection connection = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM inventory WHERE item_id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                inventory.setItemId(resultSet.getInt("item_id"));
                inventory.setProduced(resultSet.getInt("produced"));
                inventory.setSold(resultSet.getInt("sold"));
                inventory.setProducedCost(resultSet.getBigDecimal("produced_cost"));
                inventory.setSalesCost(resultSet.getBigDecimal("sales_cost"));
                inventory.setQuantity(resultSet.getInt("quantity"));
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

        return inventory;
    }

    @Override
    public List<Inventory> selectAll() {
        List<Inventory> inventories = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM inventory");

            while (resultSet.next()){
                Inventory inventory = new Inventory();
                inventory.setItemId(resultSet.getInt("item_id"));
                inventory.setProduced(resultSet.getInt("produced"));
                inventory.setSold(resultSet.getInt("sold"));
                inventory.setProducedCost(resultSet.getBigDecimal("produced_cost"));
                inventory.setSalesCost(resultSet.getBigDecimal("sales_cost"));
                inventory.setQuantity(resultSet.getInt("quantity"));

                inventories.add(inventory);
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

        return inventories;
    }

    @Override
    public ObservableList<Inventory> selectAllObservable() {
        ObservableList<Inventory> inventories = FXCollections.observableArrayList(selectAll());

        return inventories;
    }

    @Override
    public void delete(String inventoryId) {

    }

    @Override
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
