package gopark.view;

import javax.swing.*;
import java.awt.*;

public class SettingsView extends JPanel {

    public SettingsView() {
        setLayout(null);
        setBackground(Color.WHITE);

        // Header
        JLabel title = new JLabel("System Settings");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBounds(250, 40, 400, 40); // shifted left
        add(title);

        JLabel subtitle = new JLabel("Configure parking rates and system preferences");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 15));
        subtitle.setBounds(250, 75, 450, 20); // shifted left
        add(subtitle);

        // Settings Card
        JPanel cardPanel = new JPanel(null);
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBounds(230, 120, 920, 480); // closer to sidebar, slightly wider
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

        cardPanel.add(rateFieldSection("First 3 hours rate (₱)", "Base fee for first 3 hours", "30", 40, 120));
        cardPanel.add(rateFieldSection("Succeeding hour rate (₱)", "Additional fee per succeeding hour", "10", 340, 120));
        cardPanel.add(rateFieldSection("Overnight rate per day (₱)", "Fee for overnight parking", "350", 640, 120));

        // Car Section
        JLabel carTitle = new JLabel("Car Parking");
        carTitle.setFont(new Font("Arial", Font.BOLD, 16));
        carTitle.setBounds(40, 220, 200, 25);
        cardPanel.add(carTitle);

        cardPanel.add(rateFieldSection("First 3 hours rate (₱)", "Base fee for first 3 hours", "40", 40, 255));
        cardPanel.add(rateFieldSection("Succeeding hour rate (₱)", "Additional fee per succeeding hour", "15", 340, 255));
        cardPanel.add(rateFieldSection("Overnight rate per day (₱)", "Fee for overnight parking", "500", 640, 255));

        // Save
        JButton saveBtn = new JButton("Save Changes");
        saveBtn.setBackground(new Color(255, 99, 99));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Arial", Font.BOLD, 15));
        saveBtn.setFocusPainted(false);
        saveBtn.setBounds(590, 400, 230, 40);
        cardPanel.add(saveBtn);

        // Reset
        JButton resetBtn = new JButton("Reset");
        resetBtn.setBackground(new Color(230, 230, 230));
        resetBtn.setFont(new Font("Arial", Font.BOLD, 15));
        resetBtn.setBounds(830, 400, 80, 40);
        cardPanel.add(resetBtn);

        add(cardPanel);
    }

    private JPanel rateFieldSection(String labelText, String subText, String defaultValue, int x, int y) {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        panel.setBounds(x, y, 280, 80);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setBounds(0, 0, 280, 20);
        panel.add(label);

        JTextField field = new JTextField(defaultValue);
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
}
