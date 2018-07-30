package application.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBUtil {

    static Connection connection = null;
    static String databaseName = "kkc_database";
    static String url = "jdbc:mysql://localhost:3306/" +databaseName;

    static String username = "root";
    static String password = "vikings35";

    public void main() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection(url, username, password);
        PreparedStatement ps = connection.prepareStatement("INSERT INTO student (name) VALUES (?)");
        ps.setString(1, "kai gullings");
        if (connection != null)
            System.out.println("connection successful");

        ps.executeUpdate();

        //int status = ps.executeUpdate();

        //if (status != 0) {
        //    System.out.println("Database was Connected");
        //    System.out.println("Record was inserted");
        //}
    }
}
