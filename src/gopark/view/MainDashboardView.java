package gopark.view;

import gopark.controller.ParkingSlotController;

import javax.swing.*;
import java.awt.*;

public class MainDashboardView extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private SidebarView sidebarView;
    private TransactionsView  transactionsView;

    public MainDashboardView() {
        setTitle("GoPark - Admin Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

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

    public TransactionsView getTransactionsView() {
        return transactionsView;
    }
}
