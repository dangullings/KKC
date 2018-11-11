package application.util.DAO;

import application.model.Attendance;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAOImpl {

    public void createAttendanceTable(){
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS attendance (id int primary key unique auto_increment," +
                    "student_id int(6), class_date_id int(6), first_hour boolean, second_hour boolean, FOREIGN KEY (student_id) REFERENCES student(id), FOREIGN KEY (class_date_id) REFERENCES class_date(id))");

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

    public void insert(Attendance attendance) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO attendance (student_id, class_date_id, first_hour, second_hour)" +
                    "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, attendance.getStudentId());
            preparedStatement.setInt(2, attendance.getClassDateId());
            preparedStatement.setBoolean(3, attendance.isFirstHour());
            preparedStatement.setBoolean(4, attendance.isSecondHour());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    attendance.setId(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating attendance failed, no ID obtained.");
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

    public void deleteByStudentId(int studentId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM attendance WHERE student_id = ?");
            preparedStatement.setInt(1, studentId);
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

    public void deleteByClassDateId(int classDateId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM attendance WHERE class_date_id = ?");
            preparedStatement.setInt(1, classDateId);
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

    public List<Attendance> selectAllByStudentId(int studentId) {
        List<Attendance> attendances = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM attendance WHERE student_id = ?");
            preparedStatement.setInt(1, studentId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Attendance attendance = new Attendance();
                attendance.setId(resultSet.getInt("id"));
                attendance.setStudentId(resultSet.getInt("student_id"));
                attendance.setClassDateId(resultSet.getInt("class_date_id"));
                attendance.setFirstHour(resultSet.getBoolean("first_hour"));
                attendance.setSecondHour(resultSet.getBoolean("second_hour"));

                attendances.add(attendance);
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

        return attendances;
    }

    public List<Attendance> selectAllByClassDateId(int classDateId) {
        List<Attendance> attendances = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM attendance WHERE class_date_id = ?");
            preparedStatement.setInt(1, classDateId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Attendance attendance = new Attendance();
                attendance.setId(resultSet.getInt("id"));
                attendance.setStudentId(resultSet.getInt("student_id"));
                attendance.setClassDateId(resultSet.getInt("class_date_id"));
                attendance.setFirstHour(resultSet.getBoolean("first_hour"));
                attendance.setSecondHour(resultSet.getBoolean("second_hour"));

                attendances.add(attendance);
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

        return attendances;
    }

    public Attendance selectByClassDateIdAndStudentId(int classDateId, int studentId) {
        Attendance attendance = new Attendance();
        Connection connection = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM attendance WHERE class_date_id = ? AND student_id = ?");
            preparedStatement.setInt(1, classDateId);
            preparedStatement.setInt(2, studentId);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst() ) {
                attendance.setId(-1);
            }

            while (resultSet.next()){
                attendance.setId(resultSet.getInt("id"));
                attendance.setStudentId(resultSet.getInt("student_id"));
                attendance.setClassDateId(resultSet.getInt("class_date_id"));
                attendance.setFirstHour(resultSet.getBoolean("first_hour"));
                attendance.setSecondHour(resultSet.getBoolean("second_hour"));
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

        return attendance;
    }

    public Attendance selectById(int id) {
        Attendance attendance = new Attendance();
        Connection connection = null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM attendance WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                attendance.setId(resultSet.getInt("id"));
                attendance.setStudentId(resultSet.getInt("student_id"));
                attendance.setClassDateId(resultSet.getInt("class_date_id"));
                attendance.setFirstHour(resultSet.getBoolean("first_hour"));
                attendance.setSecondHour(resultSet.getBoolean("second_hour"));
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

        return attendance;
    }

    public List<Attendance> selectAll() {
        List<Attendance> attendances = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM attendance");

            while (resultSet.next()){
                Attendance attendance = new Attendance();
                attendance.setId(resultSet.getInt("id"));
                attendance.setStudentId(resultSet.getInt("student_id"));
                attendance.setClassDateId(resultSet.getInt("class_date_id"));
                attendance.setFirstHour(resultSet.getBoolean("first_hour"));
                attendance.setSecondHour(resultSet.getBoolean("second_hour"));

                attendances.add(attendance);
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

        return attendances;
    }

    public ObservableList<Attendance> selectAllObservable() {
        ObservableList<Attendance> attendances = FXCollections.observableArrayList(selectAll());

        return attendances;
    }

    public void deleteById(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM attendance WHERE id = ?");
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

    public void update(Attendance attendance, int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement("UPDATE attendance SET " +
                    "student_id = ?, class_date_id = ?, first_hour = ?, second_hour = ? WHERE id = ?");
            preparedStatement.setInt(1, attendance.getStudentId());
            preparedStatement.setInt(2, attendance.getClassDateId());
            preparedStatement.setBoolean(3, attendance.isFirstHour());
            preparedStatement.setBoolean(4, attendance.isSecondHour());
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
