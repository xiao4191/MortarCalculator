package Mortar.DB;

import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;

public class DBSupport {

    public static Connection establishConnection() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            String url = "jdbc:mysql://localhost:3306/MortarCalculator";
            String user = "root";
            String password = "root";
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            // Handle SQLException
            throw e;
        } catch (ClassNotFoundException e) {
            // Handle ClassNotFoundException
            throw e;
        }
        return conn;
    }

    public static void executeQuery(String q) throws SQLException, ClassNotFoundException {
        Connection conn = establishConnection();
        try {
            // No need to explicitly USE the database here
            Statement queryStatement = conn.createStatement();
            queryStatement.execute(q);
        } catch (SQLException e) {
            // Handle SQLException
            throw e;
        } finally {
            // Close the connection in the finally block to ensure it's always closed
            if (conn != null) {
                conn.close();
            }
        }
    }
}