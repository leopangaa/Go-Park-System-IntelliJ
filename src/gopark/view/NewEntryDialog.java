package gopark.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import gopark.controller.VehicleEntryController;
import gopark.dao.ParkingSlotDAO;

import static gopark.controller.VehicleEntryController.addVehicleEntry;

public class NewEntryDialog extends JDialog {

    public NewEntryDialog(JFrame parent) {
        super(parent, "New Vehicle Entry", true); // modal
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(420, 380);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        JLabel title = new JLabel("+ New Vehicle Entry", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel slotLabel = new JLabel("Select Parking Slot");
        slotLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JComboBox<String> slotBox = new JComboBox<>();
        slotBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        slotBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        List<String> available = ParkingSlotDAO.getAvailableSlotCodes();

        available.sort((a, b) -> {
            String prefixA = a.replaceAll("\\d", "");
            String prefixB = b.replaceAll("\\d", "");
            int numA = Integer.parseInt(a.replaceAll("\\D", ""));
            int numB = Integer.parseInt(b.replaceAll("\\D", ""));

            int cmp = prefixA.compareTo(prefixB);
            return (cmp == 0) ? Integer.compare(numA, numB) : cmp;
        });

        slotBox.addItem("Choose an available slot");
        for (String s : available) slotBox.addItem(s);


        panel.add(slotLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(slotBox);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel plateLabel = new JLabel("Plate Number");
        plateLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JTextField plateField = new JTextField();
        plateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        panel.add(plateLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(plateField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel typeLabel = new JLabel("Type of Vehicle");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Car", "Motorcycle"});
        typeBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        panel.add(typeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(typeBox);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton confirmBtn = new JButton("Confirm Entry");
        confirmBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setBackground(new Color(220, 53, 69));
        confirmBtn.setFocusPainted(false);

        confirmBtn.addActionListener(e -> {
            String slot = (String) slotBox.getSelectedItem();
            String plate = plateField.getText().trim();
            String type = (String) typeBox.getSelectedItem();

            if (slot == null || "Choose an available slot".equals(slot) || plate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = addVehicleEntry(slot, plate, type);
            if (success) {
                JOptionPane.showMessageDialog(this, "Vehicle entry recorded successfully!");
                dispose(); // close the modal dialog
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save entry.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(confirmBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        add(panel, BorderLayout.CENTER);
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
