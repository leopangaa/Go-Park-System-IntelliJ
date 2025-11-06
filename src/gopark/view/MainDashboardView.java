package gopark.view;

import gopark.controller.ParkingSlotController;

import javax.swing.*;
import java.awt.*;

public class MainDashboardView extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private SidebarView sidebarView;

    public MainDashboardView() {
        setTitle("GoPark - Admin Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar
        sidebarView = new SidebarView();
        add(sidebarView, BorderLayout.WEST);

        // Main content area
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add each view
        mainPanel.add(new DashboardView(), "Dashboard");

        // use controller to get the fully-initialized parking view
        ParkingSlotController parkingController = new ParkingSlotController();
        mainPanel.add(parkingController.getView(), "Parking");

        mainPanel.add(new TransactionsView(), "Transactions");
        mainPanel.add(new RevenueView(), "Revenue");
        mainPanel.add(new SettingsView(), "Settings");

        add(mainPanel, BorderLayout.CENTER);

        // Default panel
        cardLayout.show(mainPanel, "Dashboard");
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
}
