package gopark.controller;

import gopark.dao.ParkingSlotDAO;
import gopark.model.DBConnection;
import gopark.model.ParkingSlot;
import gopark.view.ParkingSlotView;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ParkingSlotController {
    private List<ParkingSlot> slots;
    private ParkingSlotView view;

    public ParkingSlotController() {
        loadSlots();
        view = new ParkingSlotView(slots);
        updateStats();

        view.getNewEntryButton().addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(view);

            // Show the modal dialog (this blocks until user closes it)
            new gopark.view.NewEntryDialog(parent);

            // After dialog closes, reload the slots
            reloadSlotsFromDatabase();
        });

    }

    private void loadSlots() {
        slots = ParkingSlotDAO.getAllSlots();
    }

    private void reloadSlotsFromDatabase() {
        List<ParkingSlot> updatedSlots = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT slot_code, type, occupied FROM parking_slots";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String code = rs.getString("slot_code");
                boolean occupied = rs.getBoolean("occupied");
                String type = rs.getString("type");
                updatedSlots.add(new ParkingSlot(code, occupied, type));
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ✅ update in-memory list
        this.slots = updatedSlots;

        // ✅ refresh UI
        view.updateSlots(slots);
        updateStats();
    }

    private void updateStats() {
        long occupied = slots.stream().filter(ParkingSlot::isOccupied).count();
        long available = slots.size() - occupied;
        long total = slots.size();

        view.getAvailableLabel().setText("Available: " + available + " Slots");
        view.getOccupiedLabel().setText("Occupied: " + occupied + " Slots");
        view.getTotalLabel().setText("Total Capacity: " + total + " Slots");
    }

    public JPanel getView() {
        return view;
    }
}
