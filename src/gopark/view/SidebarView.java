package gopark.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class SidebarView extends JPanel {

    private JButton btnDashboard, btnParking, btnTransactions, btnRevenue, btnSettings, btnLogout;

    private final Color backgroundColor = new Color(247, 247, 247);
    private final Color hoverColor = new Color(235, 235, 235);
    private final Color activeColor = new Color(220, 0, 0); // red accent for bar
    private final Color textColor = new Color(40, 40, 40);
    private final Color selectedBgColor = new Color(255, 240, 240);

    private final List<JButton> sidebarButtons = new ArrayList<>();
    private JButton activeButton;

    public SidebarView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(260, 750));
        setBackground(backgroundColor);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));

        // --- Logo Section ---
        ImageIcon icon = new ImageIcon(getClass().getResource("/gopark/assets/images/gopark_logo.png"));
        Image scaledImage = icon.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(scaledImage));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("ParkingMaster");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(50, 50, 50));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Admin Portal");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(110, 110, 110));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createRigidArea(new Dimension(0, 30)));
        add(logo);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(title);
        add(subtitle);
        add(Box.createRigidArea(new Dimension(0, 30)));

        // --- Sidebar Buttons ---
        btnDashboard = createSidebarButton("Dashboard", "/gopark/assets/icons/icon_dashboard.png");
        btnParking = createSidebarButton("Parking Slots", "/gopark/assets/icons/icon_car.png");
        btnTransactions = createSidebarButton("Transactions", "/gopark/assets/icons/icon_transactions.png");
        btnRevenue = createSidebarButton("Revenue", "/gopark/assets/icons/icon_revenue.png");
        btnSettings = createSidebarButton("Settings", "/gopark/assets/icons/icon_settings.png");
        btnLogout = createSidebarButton("Logout", "/gopark/assets/icons/icon_logout.png");

        // Add to tracking list
        sidebarButtons.add(btnDashboard);
        sidebarButtons.add(btnParking);
        sidebarButtons.add(btnTransactions);
        sidebarButtons.add(btnRevenue);
        sidebarButtons.add(btnSettings);

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
        add(Box.createRigidArea(new Dimension(0, 25)));

        // Default active button
        setActiveButton(btnDashboard);
    }

    private JButton createSidebarButton(String name, String iconPath) {
        JButton btn = new JButton("   " + name) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (this == activeButton) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(activeColor);
                    g2.fillRoundRect(0, 5, 5, getHeight() - 10, 5, 5);
                }
            }
        };

        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 48));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBackground(backgroundColor);
        btn.setForeground(textColor);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setIconTextGap(12);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        if (getClass().getResource(iconPath) != null) {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image scaledIcon = icon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(scaledIcon));
        }

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn != activeButton) btn.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn != activeButton) btn.setBackground(backgroundColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setActiveButton(btn);
            }
        });

        return btn;
    }

    public void setActiveButton(JButton selectedButton) {
        for (JButton b : sidebarButtons) {
            if (b == selectedButton) {
                b.setBackground(selectedBgColor);
                activeButton = b;
            } else {
                b.setBackground(backgroundColor);
            }
        }
        repaint();
    }

    // --- Getters ---
    public JButton getBtnDashboard() { return btnDashboard; }
    public JButton getBtnParking() { return btnParking; }
    public JButton getBtnTransactions() { return btnTransactions; }
    public JButton getBtnRevenue() { return btnRevenue; }
    public JButton getBtnSettings() { return btnSettings; }
    public JButton getBtnLogout() { return btnLogout; }
}
