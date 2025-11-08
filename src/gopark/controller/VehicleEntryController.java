package gopark.controller;

import gopark.model.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.sql.Timestamp;

public class VehicleEntryController {

    public static boolean addVehicleEntry(String slotCode, String plateNumber, String vehicleType) {
        String insertSQL = "INSERT INTO vehicle_entries (slot_code, plate_number, vehicle_type, entry_time) VALUES (?, ?, ?, NOW())";
        String updateSlotSQL = "UPDATE parking_slots SET status = 'HOLD' WHERE slot_code = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertSQL);
             PreparedStatement updateStmt = conn.prepareStatement(updateSlotSQL)) {

            conn.setAutoCommit(false);

            insertStmt.setString(1, slotCode);
            insertStmt.setString(2, plateNumber);
            insertStmt.setString(3, vehicleType);
            insertStmt.executeUpdate();

            updateStmt.setString(1, slotCode);
            updateStmt.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Object[] loadActiveEntryForSlot(String slotCode) {
        String sql = "SELECT id, plate_number, vehicle_type, entry_time FROM vehicle_entries " +
                "WHERE slot_code = ? AND exit_time IS NULL ORDER BY entry_time DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, slotCode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{
                            rs.getLong("id"),
                            rs.getString("plate_number"),
                            rs.getString("vehicle_type"),
                            rs.getTimestamp("entry_time")
                    };
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean closeEntryAndFreeSlot(long entryId, String slotCode, double fee) {
        String updateEntry = "UPDATE vehicle_entries SET exit_time = NOW(), total_fee = ? WHERE id = ?";
        String updateSlot = "UPDATE parking_slots SET status = 'AVAILABLE' WHERE slot_code = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement p1 = conn.prepareStatement(updateEntry);
             PreparedStatement p2 = conn.prepareStatement(updateSlot)) {

            conn.setAutoCommit(false);

            p1.setDouble(1, fee);
            p1.setLong(2, entryId);
            p1.executeUpdate();

            p2.setString(1, slotCode);
            p2.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static double calculateFee(java.sql.Timestamp entryTime) {
        if (entryTime == null) return 0.0;
        LocalDateTime entry = entryTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();
        long minutes = Duration.between(entry, now).toMinutes();
        long hours = Math.max(1, (minutes + 59) / 60); // round up, minimum 1 hour
        double ratePerHour = 15.0; // example
        return hours * ratePerHour;
    }

    public static String getDurationText(Timestamp entryTime) {
        if (entryTime == null) return "-";

        long millis = System.currentTimeMillis() - entryTime.getTime();
        long minutes = millis / (1000 * 60);
        long hours = minutes / 60;
        long remMinutes = minutes % 60;

        if (hours > 0) {
            return String.format("%d hour%s %d min%s",
                    hours, (hours > 1 ? "s" : ""),
                    remMinutes, (remMinutes > 1 ? "s" : ""));
        } else {
            return String.format("%d minute%s", remMinutes, (remMinutes > 1 ? "s" : ""));
        }
    }

}
