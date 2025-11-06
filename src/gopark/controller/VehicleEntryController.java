package gopark.controller;

import gopark.model.DBConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleEntryController {

    public static boolean addVehicleEntry(String slotCode, String plateNumber, String vehicleType) {
        String insertSQL = "INSERT INTO vehicle_entries (slot_code, plate_number, vehicle_type) VALUES (?, ?, ?)";
        String updateSlotSQL = "UPDATE parking_slots SET occupied = TRUE WHERE slot_code = ?";

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
}
