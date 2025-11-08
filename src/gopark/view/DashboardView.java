package gopark.view;

import gopark.controller.RevenueController;
import gopark.dao.ParkingSlotDAO;
import gopark.dao.TransactionDAO;
import gopark.model.DBConnection;
import gopark.model.ParkingSlot;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class DashboardView extends JPanel {

    private JLabel occupancyRateLabel;
    private JLabel availableSlotsLabel;
    private JLabel todaysRevenueLabel;
    private JLabel totalRevenueLabel;
    private JLabel occupancyDescriptionLabel; // Added to track the description label
    private RevenueController revenueController;
    private JPanel carContainer, motoContainer;
    private Timer refreshTimer;

    public DashboardView() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        revenueController = new RevenueController(DBConnection.getConnection());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 40, 10, 40));

        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        headerPanel.add(title);

        JLabel subtitle = new JLabel("Real-time parking facility monitoring and analytics");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        headerPanel.add(subtitle);

        add(headerPanel, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(245, 245, 245));
        content.setBorder(new EmptyBorder(10, 40, 20, 40));

        // Stat Cards - Matching the design exactly
        JPanel topCardsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        topCardsPanel.setOpaque(false);
        topCardsPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        occupancyRateLabel = new JLabel("0%", SwingConstants.CENTER);
        availableSlotsLabel = new JLabel("0 Slots", SwingConstants.CENTER);
        todaysRevenueLabel = new JLabel("₱0.00", SwingConstants.CENTER);
        totalRevenueLabel = new JLabel("₱0.00", SwingConstants.CENTER);
        occupancyDescriptionLabel = new JLabel("0/0 slots occupied"); // Initialize description label

        topCardsPanel.add(createStatsCard("Occupancy Rate", occupancyRateLabel, occupancyDescriptionLabel));
        topCardsPanel.add(createStatsCard("Available Slots", availableSlotsLabel, "Remaining"));
        topCardsPanel.add(createStatsCard("Today's Revenue", todaysRevenueLabel, "As of now"));
        topCardsPanel.add(createStatsCard("Total Revenue", totalRevenueLabel, "This Month"));

        content.add(topCardsPanel);

        // Parking Status Section
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel statusTitle = new JLabel("Parking Lot Status");
        statusTitle.setFont(new Font("Arial", Font.BOLD, 18));
        statusPanel.add(statusTitle, BorderLayout.NORTH);

        // Create parking slot containers (matching ParkingSlotView layout)
        JPanel parkingGridPanel = createParkingSlotContainers();
        statusPanel.add(parkingGridPanel, BorderLayout.CENTER);

        content.add(statusPanel);

        add(content, BorderLayout.CENTER);

        // Load initial data
        refreshDashboardData();

        // Start auto-refresh for real-time updates
        startAutoRefresh();
    }

    // Create Stats Card - Updated to match design
    private JPanel createStatsCard(String title, JLabel valueLabel, JLabel descriptionLabel) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // Stats Card Content
        JPanel textPanel = new JPanel();
        textPanel.setBackground(Color.WHITE);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitle.setForeground(new Color(80, 80, 80));

        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(new Color(33, 33, 33));

        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descriptionLabel.setForeground(Color.GRAY);

        textPanel.add(lblTitle);
        textPanel.add(Box.createVerticalStrut(8));
        textPanel.add(valueLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(descriptionLabel);

        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    // Overloaded method for string descriptions
    private JPanel createStatsCard(String title, JLabel valueLabel, String description) {
        JLabel descLabel = new JLabel(description);
        return createStatsCard(title, valueLabel, descLabel);
    }

    // Create parking slot containers matching ParkingSlotView layout
    private JPanel createParkingSlotContainers() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        // Car Parking Section - matches ParkingSlotView exactly
        JPanel carSection = new JPanel(new BorderLayout());
        carSection.setBackground(Color.WHITE);
        carSection.setBorder(new EmptyBorder(0, 0, 25, 0));

        JLabel carTitle = new JLabel("Car Parking");
        carTitle.setFont(new Font("Arial", Font.BOLD, 16));
        carSection.add(carTitle, BorderLayout.NORTH);

        carContainer = new JPanel();
        carContainer.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220)),
                new EmptyBorder(10, 10, 10, 10)
        ));
        carContainer.setBackground(Color.WHITE);
        carSection.add(carContainer, BorderLayout.CENTER);

        // Motorcycle Parking Section - matches ParkingSlotView exactly
        JPanel motoSection = new JPanel(new BorderLayout());
        motoSection.setBackground(Color.WHITE);

        JLabel motoTitle = new JLabel("Motorcycle Parking");
        motoTitle.setFont(new Font("Arial", Font.BOLD, 16));
        motoSection.add(motoTitle, BorderLayout.NORTH);

        motoContainer = new JPanel();
        motoContainer.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220)),
                new EmptyBorder(10, 10, 10, 10)
        ));
        motoContainer.setBackground(Color.WHITE);
        motoSection.add(motoContainer, BorderLayout.CENTER);

        mainPanel.add(carSection);
        mainPanel.add(motoSection);

        return mainPanel;
    }

    // Build grid matching ParkingSlotView's buildGrid method
    private void buildGrid(JPanel container, String type, List<ParkingSlot> slots) {
        container.removeAll();

        // Use exact same grid layouts as ParkingSlotView but with correct slot counts
        if (type.equalsIgnoreCase("Car")) {
            // Car: 2 rows of 9 slots (C1-C18)
            container.setLayout(new GridLayout(2, 9, 10, 10));
        } else {
            // Motorcycle: 2 rows for 17 slots (M1-M17)
            container.setLayout(new GridLayout(1, 9, 10, 10)); // 2 rows × 9 columns = 18 slots, but we'll only add 17
        }

        // Sort slots by numeric part of ID (same logic as ParkingSlotView)
        List<ParkingSlot> filteredSlots = slots.stream()
                .filter(s -> s.getType().equalsIgnoreCase(type))
                .sorted((a, b) -> {
                    String numA = a.getId().replaceAll("\\D+", ""); // remove letters
                    String numB = b.getId().replaceAll("\\D+", "");
                    int nA = numA.isEmpty() ? 0 : Integer.parseInt(numA);
                    int nB = numB.isEmpty() ? 0 : Integer.parseInt(numB);
                    return Integer.compare(nA, nB);
                })
                .toList();

        // Add slots to container
        for (ParkingSlot slot : filteredSlots) {
            JButton btn = createSlotButton(slot);
            container.add(btn);
        }

        // Fill remaining spaces with empty panels to maintain grid structure
        int totalSlotsNeeded = type.equalsIgnoreCase("Car") ? 18 : 18; // Both use 2×9 grid
        int emptySlots = totalSlotsNeeded - filteredSlots.size();
        for (int i = 0; i < emptySlots; i++) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setBackground(Color.WHITE);
            emptyPanel.setPreferredSize(new Dimension(60, 80));
            container.add(emptyPanel);
        }

        container.revalidate();
        container.repaint();
    }

    private JButton createSlotButton(ParkingSlot slot) {
        JButton btn = new JButton(slot.getId());
        btn.setPreferredSize(new Dimension(60, 80));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(slot.getColor());
        btn.setBorder(new LineBorder(slot.getColor().darker(), 2, true));
        btn.setEnabled(false); // Read-only in dashboard

        // Tooltip to show status
        btn.setToolTipText(slot.getId() + " - " + slot.getStatus());

        return btn;
    }

    // Refresh all dashboard data - SYNCED WITH ALL OTHER VIEWS
    public void refreshDashboardData() {
        try {
            // Get parking slot data (same source as ParkingSlotView)
            List<ParkingSlot> slots = ParkingSlotDAO.getAllSlots();

            // Calculate statistics - REAL-TIME OCCUPANCY RATE
            long totalSlots = slots.size();
            long occupiedSlots = slots.stream().filter(ParkingSlot::isOccupied).count();
            long availableSlots = totalSlots - occupiedSlots;

            // Calculate occupancy rate with 2 decimal precision
            double occupancyRate = totalSlots > 0 ? (occupiedSlots * 100.0) / totalSlots : 0;

            // Get revenue data (same source as RevenueView)
            double todayRevenue = revenueController.getTodayRevenue();
            double monthRevenue = revenueController.getMonthRevenue();

            // Update labels with real-time data
            occupancyRateLabel.setText(String.format("%.2f%%", occupancyRate));
            availableSlotsLabel.setText(availableSlots + " Slots");
            todaysRevenueLabel.setText(String.format("₱%.2f", todayRevenue));
            totalRevenueLabel.setText(String.format("₱%.2f", monthRevenue));

            // Update the description for occupancy rate with actual numbers
            occupancyDescriptionLabel.setText(occupiedSlots + "/" + totalSlots + " slots occupied");

            // Update parking slot grids
            buildGrid(carContainer, "Car", slots);
            buildGrid(motoContainer, "Motorcycle", slots);

        } catch (Exception e) {
            System.err.println("Error refreshing dashboard data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Auto-refresh for real-time updates (matches RevenueView functionality)
    private void startAutoRefresh() {
        refreshTimer = new Timer(5000, e -> { // Refresh every 5 seconds
            refreshDashboardData();
            System.out.println("Auto-refreshed dashboard data at: " + new java.util.Date());
        });
        refreshTimer.start();
    }

    public void stopAutoRefresh() {
        if (refreshTimer != null && refreshTimer.isRunning()) {
            refreshTimer.stop();
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        stopAutoRefresh();
    }
}