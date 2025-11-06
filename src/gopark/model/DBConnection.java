package gopark.model;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBConnection {
    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/gopark_db";
            String user = "leopanga";
            String pass = "earlpanga0801";

            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            System.out.println("DB Error: " + e.getMessage());
            return null;
        }
    }
}
