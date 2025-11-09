package gopark.model;

import gopark.dao.DBConnection;

import java.sql.*;

public class UserModel {

    public boolean login(String username, String password) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean register(String fullname, String username, String password) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO users(full_name, username, password) VALUES (?,?,?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, fullname);
            pst.setString(2, username);
            pst.setString(3, password);

            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
