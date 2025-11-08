package gopark.view;

import gopark.dao.ParkingRateDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class SettingsView extends JPanel {

    private JTextField motoFirst3HoursField, motoSucceedingHourField, motoOvernightField;
    private JTextField carFirst3HoursField, carSucceedingHourField, carOvernightField;

    public SettingsView() {
        setLayout(null);
        setBackground(Color.WHITE);

        // Header
        JLabel title = new JLabel("System Settings");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBounds(250, 40, 400, 40);
        add(title);

        JLabel subtitle = new JLabel("Configure parking rates and system preferences");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 15));
        subtitle.setBounds(250, 75, 450, 20);
        add(subtitle);

        // Settings Card
        JPanel cardPanel = new JPanel(null);
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBounds(230, 120, 920, 480);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Card Header
        JLabel rateIcon = new JLabel(new ImageIcon(getClass().getResource("/gopark/assets/parking_rate_icon.png")));
        rateIcon.setBounds(25, 20, 40, 40);
        cardPanel.add(rateIcon);

        JLabel headerTitle = new JLabel("Parking Rate Configuration");
        headerTitle.setFont(new Font("Arial", Font.BOLD, 18));
        headerTitle.setBounds(75, 20, 400, 25);
        cardPanel.add(headerTitle);

        JLabel headerSub = new JLabel("Set pricing structure for parking services");
        headerSub.setFont(new Font("Arial", Font.PLAIN, 14));
        headerSub.setForeground(new Color(100, 100, 100));
        headerSub.setBounds(75, 42, 400, 20);
        cardPanel.add(headerSub);

        JSeparator line = new JSeparator();
        line.setBounds(20, 70, 880, 1);
        cardPanel.add(line);

        // Motor Section
        JLabel motoTitle = new JLabel("Motorcycle Parking");
        motoTitle.setFont(new Font("Arial", Font.BOLD, 16));
        motoTitle.setBounds(40, 85, 200, 25);
        cardPanel.add(motoTitle);

        // Motorcycle fields
        JPanel motoFirst3Panel = createRateField("First 3 hours rate (₱)", "Base fee for first 3 hours", 40, 120);
        motoFirst3HoursField = getTextFieldFromPanel(motoFirst3Panel);
        cardPanel.add(motoFirst3Panel);

        JPanel motoSucceedingPanel = createRateField("Succeeding hour rate (₱)", "Additional fee per succeeding hour", 340, 120);
        motoSucceedingHourField = getTextFieldFromPanel(motoSucceedingPanel);
        cardPanel.add(motoSucceedingPanel);

        JPanel motoOvernightPanel = createRateField("Overnight rate per day (₱)", "Fee for overnight parking", 640, 120);
        motoOvernightField = getTextFieldFromPanel(motoOvernightPanel);
        cardPanel.add(motoOvernightPanel);

        // Car Section
        JLabel carTitle = new JLabel("Car Parking");
        carTitle.setFont(new Font("Arial", Font.BOLD, 16));
        carTitle.setBounds(40, 220, 200, 25);
        cardPanel.add(carTitle);

        // Car fields
        JPanel carFirst3Panel = createRateField("First 3 hours rate (₱)", "Base fee for first 3 hours", 40, 255);
        carFirst3HoursField = getTextFieldFromPanel(carFirst3Panel);
        cardPanel.add(carFirst3Panel);

        JPanel carSucceedingPanel = createRateField("Succeeding hour rate (₱)", "Additional fee per succeeding hour", 340, 255);
        carSucceedingHourField = getTextFieldFromPanel(carSucceedingPanel);
        cardPanel.add(carSucceedingPanel);

        JPanel carOvernightPanel = createRateField("Overnight rate per day (₱)", "Fee for overnight parking", 640, 255);
        carOvernightField = getTextFieldFromPanel(carOvernightPanel);
        cardPanel.add(carOvernightPanel);

        // Save Button
        JButton saveBtn = new JButton("Save Changes");
        saveBtn.setBackground(new Color(255, 99, 99));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Arial", Font.BOLD, 15));
        saveBtn.setFocusPainted(false);
        saveBtn.setBounds(590, 400, 230, 40);
        saveBtn.addActionListener(new SaveButtonListener());
        cardPanel.add(saveBtn);

        // Reset Button
        JButton resetBtn = new JButton("Reset");
        resetBtn.setBackground(new Color(230, 230, 230));
        resetBtn.setFont(new Font("Arial", Font.BOLD, 15));
        resetBtn.setBounds(830, 400, 80, 40);
        resetBtn.addActionListener(new ResetButtonListener());
        cardPanel.add(resetBtn);

        add(cardPanel);

        loadCurrentRates();
    }

    private JPanel createRateField(String labelText, String subText, int x, int y) {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        panel.setBounds(x, y, 280, 80);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setBounds(0, 0, 280, 20);
        panel.add(label);

        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBounds(0, 25, 260, 30);
        panel.add(field);

        JLabel sub = new JLabel(subText);
        sub.setFont(new Font("Arial", Font.PLAIN, 12));
        sub.setForeground(Color.GRAY);
        sub.setBounds(0, 60, 260, 15);
        panel.add(sub);

        return panel;
    }

    // Helper method
    private JTextField getTextFieldFromPanel(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JTextField) {
                return (JTextField) comp;
            }
        }
        return null;
    }

    private void loadCurrentRates() {
        Map<String, Double> rates = ParkingRateDAO.getParkingRates();

        // Set motorcycle rates
        if (motoFirst3HoursField != null) {
            motoFirst3HoursField.setText(String.valueOf(rates.getOrDefault("Motorcycle_first_3_hours", 30.0)));
        }
        if (motoSucceedingHourField != null) {
            motoSucceedingHourField.setText(String.valueOf(rates.getOrDefault("Motorcycle_succeeding_hour", 10.0)));
        }
        if (motoOvernightField != null) {
            motoOvernightField.setText(String.valueOf(rates.getOrDefault("Motorcycle_overnight_rate", 350.0)));
        }

        // Set car rates
        if (carFirst3HoursField != null) {
            carFirst3HoursField.setText(String.valueOf(rates.getOrDefault("Car_first_3_hours", 40.0)));
        }
        if (carSucceedingHourField != null) {
            carSucceedingHourField.setText(String.valueOf(rates.getOrDefault("Car_succeeding_hour", 15.0)));
        }
        if (carOvernightField != null) {
            carOvernightField.setText(String.valueOf(rates.getOrDefault("Car_overnight_rate", 500.0)));
        }
    }

    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Get motorcycle rates
                double motoFirst3Hours = Double.parseDouble(motoFirst3HoursField.getText());
                double motoSucceedingHour = Double.parseDouble(motoSucceedingHourField.getText());
                double motoOvernight = Double.parseDouble(motoOvernightField.getText());

                // Get car rates
                double carFirst3Hours = Double.parseDouble(carFirst3HoursField.getText());
                double carSucceedingHour = Double.parseDouble(carSucceedingHourField.getText());
                double carOvernight = Double.parseDouble(carOvernightField.getText());

                // Validate positive values
                if (motoFirst3Hours < 0 || motoSucceedingHour < 0 || motoOvernight < 0 ||
                        carFirst3Hours < 0 || carSucceedingHour < 0 || carOvernight < 0) {
                    JOptionPane.showMessageDialog(SettingsView.this,
                            "Please enter positive values for all rates.", "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Save rates
                boolean motoSuccess = ParkingRateDAO.updateParkingRates("Motorcycle", motoFirst3Hours, motoSucceedingHour, motoOvernight);
                boolean carSuccess = ParkingRateDAO.updateParkingRates("Car", carFirst3Hours, carSucceedingHour, carOvernight);

                if (motoSuccess && carSuccess) {
                    JOptionPane.showMessageDialog(SettingsView.this,
                            "Parking rates updated successfully!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(SettingsView.this,
                            "Failed to update parking rates.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(SettingsView.this,
                        "Please enter valid numbers for all rates.", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ResetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadCurrentRates();
        }
    }
}