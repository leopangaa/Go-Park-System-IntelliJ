package gopark.view.dialog;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import gopark.dao.ParkingSlotDAO;
import gopark.controller.VehicleEntryController;
import gopark.view.components.HoverableButton;

public class NewEntryDialog extends JDialog {

    public NewEntryDialog(JFrame parent) {
        super(parent, "New Vehicle Entry", true);
        setSize(440, 420);
        setResizable(false);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(240, 240, 240));

        JPanel card = new RoundedShadowPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        // Title
        JLabel title = new JLabel("+ New Vehicle Entry");
        title.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 17));
        title.setForeground(new Color(40, 40, 40));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);

        JLabel subtitle = new JLabel("Assign a vehicle to an available parking slot");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(120, 120, 120));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitle);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        // Parking Slot Dropdown
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

        card.add(slotLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(slotBox);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // Plate Number
        JLabel plateLabel = new JLabel("Plate Number");
        plateLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JTextField plateField = new JTextField();
        plateField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        plateField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        card.add(plateLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(plateField);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // Vehicle Type
        JLabel typeLabel = new JLabel("Type of Vehicle");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Car", "Motorcycle"});
        typeBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        typeBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        card.add(typeLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(typeBox);
        card.add(Box.createRigidArea(new Dimension(0, 25)));

        HoverableButton confirmBtn = new HoverableButton(
                "Confirm Entry",
                new Color(220, 0, 0),
                new Color(240, 30, 30)
        );

        confirmBtn.addActionListener(e -> {
            String slot = (String) slotBox.getSelectedItem();
            String plate = plateField.getText().trim();
            String type = (String) typeBox.getSelectedItem();

            if (slot == null || "Choose an available slot".equals(slot) || plate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = VehicleEntryController.addVehicleEntry(slot, plate, type);
            if (success) {
                JOptionPane.showMessageDialog(this, "Vehicle entry recorded successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save entry.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        card.add(confirmBtn);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        add(card);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    static class RoundedShadowPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int arc = 30;

            g2.setColor(new Color(0, 0, 0, 25));
            g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, arc, arc);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 10, getHeight() - 10, arc, arc);

            g2.dispose();
        }

        @Override
        public boolean isOpaque() {
            return false;
        }
    }
}
