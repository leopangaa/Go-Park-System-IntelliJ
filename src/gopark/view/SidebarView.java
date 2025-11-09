package gopark.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SidebarView extends JPanel {

    private JButton btnDashboard, btnParking, btnTransactions, btnRevenue, btnSettings, btnLogout;
    private Color backgroundColor = new Color(245, 245, 245);
    private Color hoverColor = new Color(220, 220, 220);
    private Color activeColor = new Color(200, 200, 200);
    private Color textColor = new Color(40, 40, 40);

    public SidebarView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(260, 750));
        setBackground(backgroundColor);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));

        // --- Logo Section ---
        ImageIcon icon = new ImageIcon(getClass().getResource("/gopark/assets/images/gopark_logo.png"));
        Image scaledImage = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);

        JLabel logo = new JLabel(resizedIcon);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel title = new JLabel("ParkingMaster");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subtitle = new JLabel("Admin Portal");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(new Color(100, 100, 100));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createRigidArea(new Dimension(0, 25)));
        add(logo);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(title);
        add(subtitle);
        add(Box.createRigidArea(new Dimension(0, 25)));

        // --- Sidebar Buttons ---
        btnDashboard = createSidebarButton("Dashboard", "/gopark/assets/icons/icon_dashboard.png");
        btnParking = createSidebarButton("Parking Slots", "/gopark/assets/icons/icon_car.png");
        btnTransactions = createSidebarButton("Transactions", "/gopark/assets/icons/icon_transactions.png");
        btnRevenue = createSidebarButton("Revenue", "/gopark/assets/icons/icon_revenue.png");
        btnSettings = createSidebarButton("Settings", "/gopark/assets/icons/icon_settings.png");
        btnLogout = createSidebarButton("Logout", "/gopark/assets/icons/icon_logout.png");

        // Add buttons with spacing
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

    private JButton createSidebarButton(String name, String iconPath) {
        JButton btn = new JButton(name);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 50));
        btn.setFont(new Font("Arial", Font.PLAIN, 18));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBackground(backgroundColor);
        btn.setForeground(textColor);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setIconTextGap(12);

        // Set icon if available
        if (getClass().getResource(iconPath) != null) {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image scaledIcon = icon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(scaledIcon));
        }

        // Hover and active effects
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(backgroundColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                btn.setBackground(activeColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                btn.setBackground(hoverColor);
            }
        });

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
