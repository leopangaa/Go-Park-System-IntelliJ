package gopark.view;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import gopark.controller.VehicleEntryController;
import gopark.model.DBConnection;

import static gopark.controller.VehicleEntryController.addVehicleEntry;

public class NewEntryDialog extends JDialog {

    public NewEntryDialog(JFrame parent) {
        super(parent, "New Vehicle Entry", true); // modal dialog
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(420, 380);
        setResizable(false);
        setLayout(new BorderLayout());

        // === MAIN PANEL ===
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40)); // top, left, bottom, right

        // === TITLE ===
        JLabel title = new JLabel("+ New Vehicle Entry", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Assign a vehicle to an available parking slot", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(120, 120, 120));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(subtitle);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        // === SELECT PARKING SLOT ===
        JLabel slotLabel = new JLabel("Select Parking Slot");
        slotLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JComboBox<String> slotBox = new JComboBox<>(new String[]{"Choose an available slot"});
        slotBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        slotBox.setBackground(Color.WHITE);
        slotBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        loadAvailableSlots(slotBox);

        panel.add(slotLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(slotBox);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // === PLATE NUMBER ===
        JLabel plateLabel = new JLabel("Plate Number");
        plateLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JTextField plateField = new JTextField();
        plateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        plateField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        plateField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        panel.add(plateLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(plateField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // === TYPE OF VEHICLE ===
        JLabel typeLabel = new JLabel("Type of Vehicle");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Car", "Motorcycle"});
        typeBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        typeBox.setBackground(Color.WHITE);
        typeBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        panel.add(typeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(typeBox);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        // === CONFIRM BUTTON ===
        JButton confirmBtn = new JButton("Confirm Entry");
        confirmBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setBackground(new Color(220, 53, 69)); // red button
        confirmBtn.setFocusPainted(false);
        confirmBtn.setBorderPainted(false);
        confirmBtn.setPreferredSize(new Dimension(180, 40));
        confirmBtn.setMaximumSize(new Dimension(180, 40));

        confirmBtn.addActionListener(e -> {
            String slot = (String) slotBox.getSelectedItem();
            String plate = plateField.getText().trim();
            String type = (String) typeBox.getSelectedItem();

            if (slot.equals("Choose an available slot") || plate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = addVehicleEntry(slot, plate, type);

            if (success) {
                JOptionPane.showMessageDialog(this, "Vehicle entry recorded successfully!");
                dispose(); // close dialog
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save entry.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        panel.add(confirmBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        add(panel, BorderLayout.CENTER);

        // === CENTER THE DIALOG ===
        setLocationRelativeTo(parent);

        setVisible(true);
    }

    private void loadAvailableSlots(JComboBox<String> slotBox) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT slot_code FROM parking_slots WHERE occupied = FALSE");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                slotBox.addItem(rs.getString("slot_code"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
