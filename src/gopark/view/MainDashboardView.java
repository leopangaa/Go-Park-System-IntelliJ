package gopark.view;

import gopark.controller.ParkingSlotController;
import gopark.view.tabs.DashboardView;
import gopark.view.tabs.RevenueView;
import gopark.view.tabs.SettingsView;
import gopark.view.tabs.TransactionsView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainDashboardView extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private SidebarView sidebarView;
    private TransactionsView transactionsView;
    private JPanel topBarPanel;

    public MainDashboardView() {
        setTitle("GoPark - Admin Portal");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);

        createTopBar();

        // Sidebar
        sidebarView = new SidebarView();
        add(sidebarView, BorderLayout.WEST);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new DashboardView(), "Dashboard");

        ParkingSlotController parkingController = new ParkingSlotController();
        mainPanel.add(parkingController.getView(), "Parking");

        transactionsView = new TransactionsView();
        mainPanel.add(transactionsView, "Transactions");

        mainPanel.add(new RevenueView(), "Revenue");
        mainPanel.add(new SettingsView(), "Settings");

        add(mainPanel, BorderLayout.CENTER);

        cardLayout.show(mainPanel, "Dashboard");
    }

    private void createTopBar() {
        topBarPanel = new JPanel(new BorderLayout());
        topBarPanel.setBackground(Color.WHITE);
        topBarPanel.setPreferredSize(new Dimension(getWidth(), 30));
        topBarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));

        JLabel titleLabel = new JLabel("  GoPark - Admin Portal");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(new Color(80, 80, 80));
        topBarPanel.add(titleLabel, BorderLayout.WEST);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setPreferredSize(new Dimension(70, 30));

        JButton minimizeButton = new JButton("−");
        minimizeButton.setPreferredSize(new Dimension(30, 25));
        minimizeButton.setBackground(Color.WHITE);
        minimizeButton.setForeground(Color.DARK_GRAY);
        minimizeButton.setFont(new Font("Arial", Font.BOLD, 14));
        minimizeButton.setBorder(BorderFactory.createEmptyBorder());
        minimizeButton.setFocusPainted(false);
        minimizeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        minimizeButton.addActionListener(e -> setState(JFrame.ICONIFIED));

        minimizeButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                minimizeButton.setBackground(new Color(240, 240, 240));
            }
            public void mouseExited(MouseEvent e) {
                minimizeButton.setBackground(Color.WHITE);
            }
        });

        JButton exitButton = new JButton("×");
        exitButton.setPreferredSize(new Dimension(30, 25));
        exitButton.setBackground(Color.WHITE);
        exitButton.setForeground(Color.DARK_GRAY);
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setBorder(BorderFactory.createEmptyBorder());
        exitButton.setFocusPainted(false);
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    MainDashboardView.this,
                    "Are you sure you want to exit GoPark?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        exitButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                exitButton.setBackground(new Color(200, 0, 0));
                exitButton.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent e) {
                exitButton.setBackground(Color.WHITE);
                exitButton.setForeground(Color.DARK_GRAY);
            }
        });

        controlPanel.add(minimizeButton);
        controlPanel.add(exitButton);
        topBarPanel.add(controlPanel, BorderLayout.EAST);

        add(topBarPanel, BorderLayout.NORTH);
    }

    public SidebarView getSidebarView() {
        return sidebarView;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public TransactionsView getTransactionsView() {
        return transactionsView;
    }
}