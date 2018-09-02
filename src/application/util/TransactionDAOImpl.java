package application.util;

import application.model.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImpl implements TransactionDAO {

    @Override
    public void createTransactionTable(){
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS transaction (id int primary key unique auto_increment," +
                    "student_id int(6), firstName varchar(55), lastName varchar(55), date date, salePrice decimal(6,2))");

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
    public void insert(Transaction transaction) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO transaction (student_id, firstName, lastName, date, salePrice)" +
                    "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, transaction.getStudentId());
            preparedStatement.setString(2, transaction.getFirstName());
            preparedStatement.setString(3, transaction.getLastName());
            preparedStatement.setDate(4, Date.valueOf(transaction.getDate()));
            preparedStatement.setBigDecimal(5, transaction.getSalePrice());
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

    @Override
    public void update(Transaction transaction, int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("UPDATE transaction SET " +
                    "student_id = ?, firstName = ?, lastName = ?, date = ?, salePrice = ? WHERE id = ?");
            preparedStatement.setInt(1, transaction.getStudentId());
            preparedStatement.setString(2, transaction.getFirstName());
            preparedStatement.setString(3, transaction.getLastName());
            preparedStatement.setDate(4, Date.valueOf(transaction.getDate()));
            preparedStatement.setBigDecimal(5, transaction.getSalePrice());
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

    @Override
    public List<Transaction> selectAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM transaction");

            while (resultSet.next()){
                Transaction transaction = new Transaction();
                transaction.setId(resultSet.getInt("id"));
                transaction.setStudentId(resultSet.getInt("student_id"));
                transaction.setFirstName(resultSet.getString("firstName"));
                transaction.setLastName(resultSet.getString("lastName"));
                transaction.setDate(resultSet.getDate("date"));
                transaction.setSalePrice(resultSet.getBigDecimal("salePrice"));

                transactions.add(transaction);
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

        return transactions;
    }

    @Override
    public ObservableList<Transaction> selectAllObservableTransactions() {
        return FXCollections.observableArrayList(selectAllTransactions());
    }
}
