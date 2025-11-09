package gopark.view.tabs;

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
        setLayout(new BorderLayout());
        setBackground(new Color(248, 248, 248));

        // ===== HEADER =====
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(248, 248, 248));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 10, 30));

        JLabel title = new JLabel("System Settings");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Configure parking rates and system preferences");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(new Color(100, 100, 100));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(title);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitle);

        add(headerPanel, BorderLayout.NORTH);

        // ===== MAIN CARD =====
        RoundedPanel cardPanel = new RoundedPanel(20, Color.WHITE);
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ==== CARD HEADER ====
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        JLabel sectionHeader = new JLabel("Parking Rate Configuration");
        sectionHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        cardPanel.add(sectionHeader, gbc);

        gbc.gridy++;
        JLabel sectionSub = new JLabel("Set pricing structure for parking services");
        sectionSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sectionSub.setForeground(new Color(120, 120, 120));
        cardPanel.add(sectionSub, gbc);

        gbc.gridy++;
        gbc.gridwidth = 3;
        cardPanel.add(new JSeparator(), gbc);

        // ==== MOTORCYCLE PARKING ====
        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel motoTitle = new JLabel("Motorcycle Parking");
        motoTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        cardPanel.add(motoTitle, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JPanel motoFirst3Panel = createRateField("First 3 hours rate (₱)", "Base fee for first 3 hours");
        motoFirst3HoursField = getTextFieldFromPanel(motoFirst3Panel);
        cardPanel.add(motoFirst3Panel, gbc);

        gbc.gridx = 1;
        JPanel motoSucceedingPanel = createRateField("Succeeding hour rate (₱)", "Additional fee per succeeding hour");
        motoSucceedingHourField = getTextFieldFromPanel(motoSucceedingPanel);
        cardPanel.add(motoSucceedingPanel, gbc);

        gbc.gridx = 2;
        JPanel motoOvernightPanel = createRateField("Overnight rate per day (₱)", "Fee for overnight parking");
        motoOvernightField = getTextFieldFromPanel(motoOvernightPanel);
        cardPanel.add(motoOvernightPanel, gbc);

        // ==== CAR PARKING ====
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel carTitle = new JLabel("Car Parking");
        carTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        cardPanel.add(carTitle, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JPanel carFirst3Panel = createRateField("First 3 hours rate (₱)", "Base fee for first 3 hours");
        carFirst3HoursField = getTextFieldFromPanel(carFirst3Panel);
        cardPanel.add(carFirst3Panel, gbc);

        gbc.gridx = 1;
        JPanel carSucceedingPanel = createRateField("Succeeding hour rate (₱)", "Additional fee per succeeding hour");
        carSucceedingHourField = getTextFieldFromPanel(carSucceedingPanel);
        cardPanel.add(carSucceedingPanel, gbc);

        gbc.gridx = 2;
        JPanel carOvernightPanel = createRateField("Overnight rate per day (₱)", "Fee for overnight parking");
        carOvernightField = getTextFieldFromPanel(carOvernightPanel);
        cardPanel.add(carOvernightPanel, gbc);

        // ===== BUTTON BAR =====
        gbc.gridy++;
        gbc.gridx = 1;
        gbc.insets = new Insets(25, 10, 0, 10);
        JButton saveBtn = new JButton("Save Changes");
        saveBtn.setBackground(new Color(255, 99, 99));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.setPreferredSize(new Dimension(180, 40));
        saveBtn.addActionListener(new SaveButtonListener());
        cardPanel.add(saveBtn, gbc);

        gbc.gridx = 2;
        JButton resetBtn = new JButton("Reset");
        resetBtn.setBackground(new Color(235, 235, 235));
        resetBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        resetBtn.setFocusPainted(false);
        resetBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetBtn.setPreferredSize(new Dimension(120, 40));
        resetBtn.addActionListener(new ResetButtonListener());
        cardPanel.add(resetBtn, gbc);

        // ===== ADD TO VIEW =====
        add(Box.createVerticalStrut(10), BorderLayout.CENTER);
        add(cardPanel, BorderLayout.CENTER);

        loadCurrentRates();
    }

    private JPanel createRateField(String labelText, String subText) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(220, 75));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        JLabel sub = new JLabel(subText);
        sub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        sub.setForeground(new Color(140, 140, 140));

        panel.add(label);
        panel.add(Box.createVerticalStrut(3));
        panel.add(field);
        panel.add(Box.createVerticalStrut(2));
        panel.add(sub);

        return panel;
    }

    private JTextField getTextFieldFromPanel(JPanel panel) {
        for (Component comp : panel.getComponents())
            if (comp instanceof JTextField) return (JTextField) comp;
        return null;
    }

    private void loadCurrentRates() {
        Map<String, Double> rates = ParkingRateDAO.getParkingRates();

        motoFirst3HoursField.setText(String.valueOf(rates.getOrDefault("Motorcycle_first_3_hours", 30.0)));
        motoSucceedingHourField.setText(String.valueOf(rates.getOrDefault("Motorcycle_succeeding_hour", 10.0)));
        motoOvernightField.setText(String.valueOf(rates.getOrDefault("Motorcycle_overnight_rate", 350.0)));

        carFirst3HoursField.setText(String.valueOf(rates.getOrDefault("Car_first_3_hours", 40.0)));
        carSucceedingHourField.setText(String.valueOf(rates.getOrDefault("Car_succeeding_hour", 15.0)));
        carOvernightField.setText(String.valueOf(rates.getOrDefault("Car_overnight_rate", 500.0)));
    }

    // ==== BUTTON ACTIONS ====
    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                double motoFirst3Hours = Double.parseDouble(motoFirst3HoursField.getText());
                double motoSucceedingHour = Double.parseDouble(motoSucceedingHourField.getText());
                double motoOvernight = Double.parseDouble(motoOvernightField.getText());

                double carFirst3Hours = Double.parseDouble(carFirst3HoursField.getText());
                double carSucceedingHour = Double.parseDouble(carSucceedingHourField.getText());
                double carOvernight = Double.parseDouble(carOvernightField.getText());

                if (motoFirst3Hours < 0 || motoSucceedingHour < 0 || motoOvernight < 0 ||
                        carFirst3Hours < 0 || carSucceedingHour < 0 || carOvernight < 0) {
                    JOptionPane.showMessageDialog(SettingsView.this,
                            "Please enter positive values.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean motoSuccess = ParkingRateDAO.updateParkingRates("Motorcycle",
                        motoFirst3Hours, motoSucceedingHour, motoOvernight);

                boolean carSuccess = ParkingRateDAO.updateParkingRates("Car",
                        carFirst3Hours, carSucceedingHour, carOvernight);

                JOptionPane.showMessageDialog(SettingsView.this,
                        motoSuccess && carSuccess ? "Parking rates updated!" : "Failed to update rates.");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(SettingsView.this,
                        "Enter valid numbers.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ResetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadCurrentRates();
        }
    }

    // ===== ROUNDED PANEL CLASS =====
    private static class RoundedPanel extends JPanel {
        private final int cornerRadius;
        private final Color backgroundColor;

        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.cornerRadius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // shadow
            g2.setColor(new Color(0, 0, 0, 25));
            g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, cornerRadius, cornerRadius);

            // main card
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, cornerRadius, cornerRadius);
            g2.dispose();
        }
    }
}
