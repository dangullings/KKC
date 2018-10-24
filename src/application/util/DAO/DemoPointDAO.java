package application.util.DAO;

import application.model.DemoPoint;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DemoPointDAO {

    public void createDemoPointTable(){
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS demo_point (id int primary key unique auto_increment," +
                    "name char(65), value int(3), category int(3), modifiable boolean)");

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

    public void insert(DemoPoint demoPoint) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO demo_point (name, value, category, modifiable)" +
                    "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, demoPoint.getName());
            preparedStatement.setInt(2, demoPoint.getValue());
            preparedStatement.setInt(3, demoPoint.getCategory());
            preparedStatement.setBoolean(4, demoPoint.isModifiable());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    demoPoint.setId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating demo_point failed, no ID obtained.");
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

    public DemoPoint selectById(int id) {
        DemoPoint demoPoint = new DemoPoint();
        Connection connection = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM demo_point WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()){
                demoPoint.setId(-1);
            }else{
                while (resultSet.next()) {
                    demoPoint.setId(resultSet.getInt("id"));
                    demoPoint.setName(resultSet.getString("name"));
                    demoPoint.setValue(resultSet.getInt("value"));
                    demoPoint.setCategory(resultSet.getInt("category"));
                    demoPoint.setModifiable(resultSet.getBoolean("modifiable"));
                }
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

        return demoPoint;
    }

    public List<DemoPoint> selectAll() {
        List<DemoPoint> demoPoints = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM demo_point");

            while (resultSet.next()){
                DemoPoint demoPoint = new DemoPoint();
                demoPoint.setId(resultSet.getInt("id"));
                demoPoint.setName(resultSet.getString("name"));
                demoPoint.setValue(resultSet.getInt("value"));
                demoPoint.setCategory(resultSet.getInt("category"));
                demoPoint.setModifiable(resultSet.getBoolean("modifiable"));

                demoPoints.add(demoPoint);
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

        return demoPoints;
    }

    public List<DemoPoint> selectAllByCategory(int category) {
        List<DemoPoint> demoPoints = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM demo_point WHERE category = ?");
            preparedStatement.setInt(1, category);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                DemoPoint demoPoint = new DemoPoint();
                demoPoint.setId(resultSet.getInt("id"));
                demoPoint.setName(resultSet.getString("name"));
                demoPoint.setValue(resultSet.getInt("value"));
                demoPoint.setCategory(resultSet.getInt("category"));
                demoPoint.setModifiable(resultSet.getBoolean("modifiable"));

                demoPoints.add(demoPoint);
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

        return demoPoints;
    }

    public ObservableList<DemoPoint> selectAllObservable() {
        ObservableList<DemoPoint> demoPoints = FXCollections.observableArrayList(selectAll());

        return demoPoints;
    }

    public ObservableList<DemoPoint> selectAllObservableByCategory(int category) {
        ObservableList<DemoPoint> demoPoints = FXCollections.observableArrayList(selectAllByCategory(category));

        return demoPoints;
    }

    public void deleteById(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM demo_point WHERE id = ?");
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

    public void update(DemoPoint demoPoint, int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {//type, date, location, numStudents
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("UPDATE demo_point SET " +
                    "name = ?, value = ?, category = ?, modifiable = ? WHERE id = ?");
            preparedStatement.setString(1, demoPoint.getName());
            preparedStatement.setInt(2, demoPoint.getValue());
            preparedStatement.setInt(3, demoPoint.getCategory());
            preparedStatement.setBoolean(4, demoPoint.isModifiable());
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
