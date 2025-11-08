package gopark.dao;

import gopark.model.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public static void addTransaction(String plateNumber, String vehicleType, String slotCode,
                                      Timestamp entryTime, Timestamp exitTime, String duration,
                                      double fee, String status) {
        String sql = "INSERT INTO transactions " +
                "(plate_number, vehicle_type, slot_code, entry_time, exit_time, duration, fee, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, plateNumber);
            stmt.setString(2, vehicleType);
            stmt.setString(3, slotCode);
            stmt.setTimestamp(4, entryTime);
            stmt.setTimestamp(5, exitTime);
            stmt.setString(6, duration);
            stmt.setDouble(7, fee);
            stmt.setString(8, status);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String formatReceiptNumber(int transactionId) {
        return String.format("GP-%05d", transactionId);
    }

    public static int markAsPaid(String plate, Timestamp exitTime, String duration, double fee) {
        String sql = "UPDATE transactions SET exit_time = ?, duration = ?, fee = ?, status = 'Paid' " +
                "WHERE plate_number = ? AND status = 'Parked'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setTimestamp(1, exitTime);
            stmt.setString(2, duration);
            stmt.setDouble(3, fee);
            stmt.setString(4, plate);

            int affected = stmt.executeUpdate();

            if (affected > 0) {
                String fetch = "SELECT id FROM transactions WHERE plate_number = ? ORDER BY id DESC LIMIT 1";
                PreparedStatement fetchStmt = conn.prepareStatement(fetch);
                fetchStmt.setString(1, plate);
                ResultSet rs = fetchStmt.executeQuery();

                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static List<Object[]> getAllTransactions() {
        List<Object[]> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                transactions.add(new Object[]{
                        rs.getInt("id"),
                        rs.getString("plate_number"),
                        rs.getString("vehicle_type"),
                        rs.getString("slot_code"),
                        rs.getTimestamp("entry_time"),
                        rs.getTimestamp("exit_time"),
                        rs.getString("duration"),
                        rs.getDouble("fee"),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
