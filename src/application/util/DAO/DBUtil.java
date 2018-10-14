package application.util.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBUtil {

    static String databaseName = "kkc_database";
    static String url = "jdbc:mysql://localhost:3306/" +databaseName;

    static String username = "root";
    static String password = "vikings35";

    public static Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e){
            e.printStackTrace();
        }

        return connection;
    }

    public static Connection createDatabase(){
        Connection connection = null;

        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", username, password);
            Statement statement = connection.createStatement();
            int Result = statement.executeUpdate("CREATE DATABASE IF NOT EXISTS "+databaseName);
        } catch (Exception e){
            e.printStackTrace();
        }

        return connection;
    }
}
