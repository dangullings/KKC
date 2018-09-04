package application.util;

import application.model.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImpl {

    public void createTransactionTable(){
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS transaction (id int primary key unique auto_increment," +
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

    public void insert(Transaction transaction) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO transaction (student_id, firstName, lastName, date, salePrice, note, complete)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, transaction.getStudentId());
            preparedStatement.setString(2, transaction.getFirstName());
            preparedStatement.setString(3, transaction.getLastName());
            preparedStatement.setDate(4, Date.valueOf(transaction.getDate()));
            preparedStatement.setBigDecimal(5, transaction.getSalePrice());
            preparedStatement.setString(6, transaction.getNote());
            preparedStatement.setBoolean(7, transaction.isComplete());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    transaction.setId(generatedKeys.getInt(1));
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

    public void update(Transaction transaction, int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("UPDATE transaction SET " +
                    "student_id = ?, firstName = ?, lastName = ?, date = ?, salePrice = ?, note = ?, complete = ? WHERE id = ?");
            preparedStatement.setInt(1, transaction.getStudentId());
            preparedStatement.setString(2, transaction.getFirstName());
            preparedStatement.setString(3, transaction.getLastName());
            preparedStatement.setDate(4, Date.valueOf(transaction.getDate()));
            preparedStatement.setBigDecimal(5, transaction.getSalePrice());
            preparedStatement.setString(6, transaction.getNote());
            preparedStatement.setBoolean(7, transaction.isComplete());
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

    public Transaction selectById(int id) {
        Transaction transaction = new Transaction();
        Connection connection = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM transaction WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                transaction.setId(resultSet.getInt("id"));
                transaction.setStudentId(resultSet.getInt("student_id"));
                transaction.setFirstName(resultSet.getString("firstName"));
                transaction.setLastName(resultSet.getString("lastName"));
                transaction.setDate(resultSet.getDate("date"));
                transaction.setSalePrice(resultSet.getBigDecimal("salePrice"));
                transaction.setNote(resultSet.getString("note"));
                transaction.setComplete(resultSet.getBoolean("complete"));
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

        return transaction;
    }

    public void deleteById(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM transaction WHERE id = ?");
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

    private List<Transaction> selectAllTransactions(boolean complete) {
        List<Transaction> orders = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM transaction WHERE complete = ?");
            preparedStatement.setBoolean(1, complete);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Transaction transaction = new Transaction();
                transaction.setId(resultSet.getInt("id"));
                transaction.setStudentId(resultSet.getInt("student_id"));
                transaction.setFirstName(resultSet.getString("firstName"));
                transaction.setLastName(resultSet.getString("lastName"));
                transaction.setDate(resultSet.getDate("date"));
                transaction.setSalePrice(resultSet.getBigDecimal("salePrice"));
                transaction.setNote(resultSet.getString("note"));
                transaction.setComplete(resultSet.getBoolean("complete"));

                orders.add(transaction);
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

    public ObservableList<Transaction> selectAllObservableTransactions(boolean complete) {
        return FXCollections.observableArrayList(selectAllTransactions(complete));
    }
}
