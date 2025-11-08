package gopark.view;

import javax.swing.*;
import java.awt.*;

public class SidebarView extends JPanel {

    private JButton btnDashboard, btnParking, btnTransactions, btnRevenue, btnSettings, btnLogout;

    public SidebarView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(230, 650));
        setBackground(new Color(245, 245, 245));

        ImageIcon icon = new ImageIcon(getClass().getResource("/gopark/assets/gopark_logo.png"));
        Image scaledImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);
        JLabel logoLabel = new JLabel(resizedIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createRigidArea(new Dimension(0, 20)));
        add(logoLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));

        // Buttons
        btnDashboard = createSidebarButton("Dashboard");
        btnParking = createSidebarButton("Parking Slots");
        btnTransactions = createSidebarButton("Transactions");
        btnRevenue = createSidebarButton("Revenue");
        btnSettings = createSidebarButton("Settings");
        btnLogout = createSidebarButton("Logout");

        add(btnDashboard);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(btnParking);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(btnTransactions);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(btnRevenue);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(btnSettings);

        add(Box.createVerticalGlue());
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(btnLogout);
        add(Box.createRigidArea(new Dimension(0, 20)));
    }

    private JButton createSidebarButton(String name) {
        JButton btn = new JButton(name);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 45));
        btn.setBackground(new Color(230, 230, 230));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        return btn;
    }

    // Getters
    public JButton getBtnDashboard() { return btnDashboard; }
    public JButton getBtnParking() { return btnParking; }
    public JButton getBtnTransactions() { return btnTransactions; }
    public JButton getBtnRevenue() { return btnRevenue; }
    public JButton getBtnSettings() { return btnSettings; }
    public JButton getBtnLogout() { return btnLogout; }
}
