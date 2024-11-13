package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class db_connect {
    private static final String URL = "jdbc:mysql://localhost:3306/sikoper_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection connect() throws SQLException {
       Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        
        if (connection != null) {
            System.out.println("berhasil konek!");
        }
        return connection;
    }
    public static void main(String[] args) {
        try {
            connect();
        } catch (SQLException e) {
            System.out.println("gagal konek!.");
            e.printStackTrace();
        }
    }
}
