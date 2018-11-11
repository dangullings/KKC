package application.util.DAO;

import application.model.DemoPointAwarded;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DemoPointAwardedDAO {

    public void createDemoPointAwardedTable(){
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS demo_point_awarded (id int primary key unique auto_increment," +
                    "student_id int(6), name char(95), info char(55), value int(6), FOREIGN KEY (student_id) REFERENCES student(id))");

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

    public void insert(DemoPointAwarded demoPointAwarded) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO demo_point_awarded (student_id, name, info, value)" +
                    "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, demoPointAwarded.getStudentId());
            preparedStatement.setString(2, demoPointAwarded.getName());
            preparedStatement.setString(3, demoPointAwarded.getInfo());
            preparedStatement.setInt(4, demoPointAwarded.getValue());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    demoPointAwarded.setId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating demo_point_awarded failed, no ID obtained.");
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

    public DemoPointAwarded selectById(int id) {
        DemoPointAwarded demoPointAwarded = new DemoPointAwarded();
        Connection connection = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM demo_point_awarded WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                demoPointAwarded.setId(resultSet.getInt("id"));
                demoPointAwarded.setStudentId(resultSet.getInt("student_id"));
                demoPointAwarded.setName(resultSet.getString("name"));
                demoPointAwarded.setInfo(resultSet.getString("info"));
                demoPointAwarded.setValue(resultSet.getInt("value"));
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

        return demoPointAwarded;
    }

    public List<DemoPointAwarded> selectAll() {
        List<DemoPointAwarded> demoPointsAwarded = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM demo_point_awarded");

            while (resultSet.next()){
                DemoPointAwarded demoPointAwarded = new DemoPointAwarded();
                demoPointAwarded.setId(resultSet.getInt("id"));
                demoPointAwarded.setStudentId(resultSet.getInt("student_id"));
                demoPointAwarded.setName(resultSet.getString("name"));
                demoPointAwarded.setInfo(resultSet.getString("info"));
                demoPointAwarded.setValue(resultSet.getInt("value"));

                demoPointsAwarded.add(demoPointAwarded);
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

        return demoPointsAwarded;
    }

    public List<DemoPointAwarded> selectAllByStudentId(int studentId) {
        List<DemoPointAwarded> demoPointsAwarded = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM demo_point_awarded WHERE student_id = ?");
            preparedStatement.setInt(1, studentId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                DemoPointAwarded demoPointAwarded = new DemoPointAwarded();
                demoPointAwarded.setId(resultSet.getInt("id"));
                demoPointAwarded.setStudentId(resultSet.getInt("student_id"));
                demoPointAwarded.setName(resultSet.getString("name"));
                demoPointAwarded.setInfo(resultSet.getString("info"));
                demoPointAwarded.setValue(resultSet.getInt("value"));

                demoPointsAwarded.add(demoPointAwarded);
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

        return demoPointsAwarded;
    }

    public ObservableList<DemoPointAwarded> selectAllObservable() {
        ObservableList<DemoPointAwarded> demoPointsAwarded = FXCollections.observableArrayList(selectAll());

        return demoPointsAwarded;
    }

    public ObservableList<DemoPointAwarded> selectAllObservableByStudentId(int studentId) {
        ObservableList<DemoPointAwarded> demoPointsAwarded = FXCollections.observableArrayList(selectAllByStudentId(studentId));

        return demoPointsAwarded;
    }

    public void deleteByDemoPointIdAndStudentId(int demoPointId, int studentId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM demo_point_awarded WHERE demo_point_id = ? AND student_id = ?");
            preparedStatement.setInt(1, demoPointId);
            preparedStatement.setInt(2, studentId);
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

    public void deleteByDemoPointId(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM demo_point_awarded WHERE demo_point_id = ?");
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

    public void deleteById(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM demo_point_awarded WHERE id = ?");
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

    public void update(DemoPointAwarded demoPointAwarded, int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("UPDATE demo_point_awarded SET " +
                    "student_id = ?, name = ?, info = ?, value = ? WHERE id = ?");
            preparedStatement.setInt(1, demoPointAwarded.getStudentId());
            preparedStatement.setString(2, demoPointAwarded.getName());
            preparedStatement.setString(3, demoPointAwarded.getInfo());
            preparedStatement.setInt(4, demoPointAwarded.getValue());
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