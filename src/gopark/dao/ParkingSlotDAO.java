package gopark.dao;

import gopark.model.DBConnection;
import gopark.model.ParkingSlot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParkingSlotDAO {

    public static List<ParkingSlot> getAllSlots() {
        List<ParkingSlot> slots = new ArrayList<>();
        String sql = "SELECT slot_code, status, type FROM parking_slots ORDER BY slot_code";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                slots.add(new ParkingSlot(
                        rs.getString("slot_code"),
                        rs.getString("status"),
                        rs.getString("type")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return slots;
    }

    public static List<String> getAvailableSlotCodes() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT slot_code FROM parking_slots WHERE status = 'AVAILABLE' ORDER BY slot_code";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(rs.getString("slot_code"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
