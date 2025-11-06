// ParkingSlotDAO.java
package gopark.dao;

import gopark.model.DBConnection;
import gopark.model.ParkingSlot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParkingSlotDAO {

    public static List<ParkingSlot> getAllSlots() {
        List<ParkingSlot> slots = new ArrayList<>();
        String sql = "SELECT slot_code, occupied, type FROM parking_slots";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                slots.add(new ParkingSlot(
                        rs.getString("slot_code"),
                        rs.getBoolean("occupied"),
                        rs.getString("type")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return slots;
    }

    public static void markSlotOccupied(String slotCode, boolean occupied) {
        String sql = "UPDATE parking_slots SET occupied = ? WHERE slot_code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, occupied);
            stmt.setString(2, slotCode);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
