package gopark.view.tabs;

import gopark.controller.RevenueController;
import gopark.dao.ParkingSlotDAO;
import gopark.dao.DBConnection;
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
    private JLabel occupancyDescriptionLabel;
    private RevenueController revenueController;
    private JPanel carContainer, motoContainer;
    private Timer refreshTimer;

    public DashboardView() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 242, 245));

        revenueController = new RevenueController(DBConnection.getConnection());

        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(new Color(240, 242, 245));
        mainContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = createHeaderPanel();
        mainContent.add(headerPanel, BorderLayout.NORTH);

        JPanel statsPanel = createStatsCardsPanel();
        mainContent.add(statsPanel, BorderLayout.CENTER);

        JPanel parkingStatusPanel = createParkingStatusPanel();
        mainContent.add(parkingStatusPanel, BorderLayout.SOUTH);

        add(mainContent, BorderLayout.CENTER);

        refreshDashboardData();
        startAutoRefresh();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 242, 245));
        headerPanel.setBorder(new EmptyBorder(0, 0, 30, 0));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(240, 242, 245));

        JLabel mainTitle = new JLabel("ParkingMaster");
        mainTitle.setFont(new Font("Arial", Font.BOLD, 28));
        mainTitle.setForeground(new Color(44, 62, 80));

        JLabel subtitle = new JLabel("Admin Portal");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setForeground(new Color(127, 140, 141));

        titlePanel.add(mainTitle);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        JLabel dashboardTitle = new JLabel("Dashboard Overview");
        dashboardTitle.setFont(new Font("Arial", Font.BOLD, 20));
        dashboardTitle.setForeground(new Color(52, 73, 94));
        dashboardTitle.setHorizontalAlignment(SwingConstants.RIGHT);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(dashboardTitle, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createStatsCardsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setBackground(new Color(240, 242, 245));
        statsPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        occupancyRateLabel = new JLabel("0%", SwingConstants.CENTER);
        availableSlotsLabel = new JLabel("0 Slots", SwingConstants.CENTER);
        todaysRevenueLabel = new JLabel("₱0.00", SwingConstants.CENTER);
        totalRevenueLabel = new JLabel("₱0.00", SwingConstants.CENTER);
        occupancyDescriptionLabel = new JLabel("0/35 slots occupied", SwingConstants.CENTER);

        statsPanel.add(createDesignStatsCard("Occupancy Rate", occupancyRateLabel, occupancyDescriptionLabel));
        statsPanel.add(createDesignStatsCard("Available Slots", availableSlotsLabel, "Remaining"));
        statsPanel.add(createDesignStatsCard("Today's Revenue", todaysRevenueLabel, "As of now"));
        statsPanel.add(createDesignStatsCard("Total Revenue", totalRevenueLabel, "This Month"));

        return statsPanel;
    }

    private JPanel createDesignStatsCard(String title, JLabel valueLabel, JLabel descriptionLabel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(25, 20, 25, 20)
        ));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // Title - properly centered
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Value - properly centered
        valueLabel.setFont(new Font("Arial", Font.BOLD, 32));
        valueLabel.setForeground(new Color(60, 60, 60));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Description - properly centered
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionLabel.setForeground(new Color(150, 150, 150));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add components with balanced vertical spacing
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 12))); // Consistent spacing
        contentPanel.add(valueLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8))); // Consistent spacing
        contentPanel.add(descriptionLabel);

        // Center the entire content panel within the card
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(Color.WHITE);
        centerWrapper.add(contentPanel);

        card.add(centerWrapper, BorderLayout.CENTER);
        return card;
    }

    private JPanel createDesignStatsCard(String title, JLabel valueLabel, String description) {
        JLabel descLabel = new JLabel(description, SwingConstants.CENTER);
        return createDesignStatsCard(title, valueLabel, descLabel);
    }

    private JPanel createParkingStatusPanel() {
        JPanel parkingPanel = new JPanel(new BorderLayout());
        parkingPanel.setBackground(new Color(240, 242, 245));

        JLabel parkingTitle = new JLabel("Parking Lot Status");
        parkingTitle.setFont(new Font("Arial", Font.BOLD, 22));
        parkingTitle.setForeground(new Color(44, 62, 80));
        parkingTitle.setBorder(new EmptyBorder(20, 0, 20, 0));

        JPanel parkingContent = new JPanel(new BorderLayout());
        parkingContent.setBackground(Color.WHITE);
        parkingContent.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(25, 25, 25, 25)
        ));

        JPanel parkingGridPanel = createParkingSlotContainers();
        parkingContent.add(parkingGridPanel, BorderLayout.CENTER);

        parkingPanel.add(parkingTitle, BorderLayout.NORTH);
        parkingPanel.add(parkingContent, BorderLayout.CENTER);

        return parkingPanel;
    }

    private JPanel createParkingSlotContainers() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        // Car Parking Section
        JPanel carSection = new JPanel(new BorderLayout());
        carSection.setBackground(Color.WHITE);
        carSection.setBorder(new EmptyBorder(0, 0, 30, 0));

        JLabel carTitle = new JLabel("Car Parking");
        carTitle.setFont(new Font("Arial", Font.BOLD, 16));
        carTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        carSection.add(carTitle, BorderLayout.NORTH);

        carContainer = new JPanel();
        carContainer.setLayout(new GridLayout(2, 9, 8, 8));
        carContainer.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220)),
                new EmptyBorder(15, 15, 15, 15)
        ));
        carContainer.setBackground(Color.WHITE);

        for (int i = 1; i <= 18; i++) {
            JPanel placeholder = new JPanel();
            placeholder.setBackground(new Color(240, 240, 240));
            placeholder.setBorder(new LineBorder(new Color(200, 200, 200)));
            placeholder.setPreferredSize(new Dimension(70, 60));
            carContainer.add(placeholder);
        }

        carSection.add(carContainer, BorderLayout.CENTER);

        // Motorcycle Parking Section
        JPanel motoSection = new JPanel(new BorderLayout());
        motoSection.setBackground(Color.WHITE);

        JLabel motoTitle = new JLabel("Motorcycle Parking");
        motoTitle.setFont(new Font("Arial", Font.BOLD, 16));
        motoTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        motoSection.add(motoTitle, BorderLayout.NORTH);

        motoContainer = new JPanel();
        motoContainer.setLayout(new GridLayout(1, 8, 8, 8));
        motoContainer.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220)),
                new EmptyBorder(15, 15, 15, 15)
        ));
        motoContainer.setBackground(Color.WHITE);

        for (int i = 1; i <= 17; i++) {
            JPanel placeholder = new JPanel();
            placeholder.setBackground(new Color(240, 240, 240));
            placeholder.setBorder(new LineBorder(new Color(200, 200, 200)));
            placeholder.setPreferredSize(new Dimension(70, 60));
            motoContainer.add(placeholder);
        }

        motoSection.add(motoContainer, BorderLayout.CENTER);

        mainPanel.add(carSection);
        mainPanel.add(motoSection);

        return mainPanel;
    }

    private void buildGrid(JPanel container, String type, List<ParkingSlot> slots) {
        container.removeAll();

        if (type.equalsIgnoreCase("Car")) {
            container.setLayout(new GridLayout(2, 9, 8, 8));
        } else {
            container.setLayout(new GridLayout(1, 8, 8, 8));
        }

        List<ParkingSlot> filteredSlots = slots.stream()
                .filter(s -> s.getType().equalsIgnoreCase(type))
                .sorted((a, b) -> {
                    String numA = a.getId().replaceAll("\\D+", "");
                    String numB = b.getId().replaceAll("\\D+", "");
                    int nA = numA.isEmpty() ? 0 : Integer.parseInt(numA);
                    int nB = numB.isEmpty() ? 0 : Integer.parseInt(numB);
                    return Integer.compare(nA, nB);
                })
                .toList();

        for (ParkingSlot slot : filteredSlots) {
            JButton btn = createSlotButton(slot);
            container.add(btn);
        }

        int totalSlotsNeeded;
        if (type.equalsIgnoreCase("Car")) {
            totalSlotsNeeded = 18;
        } else {
            totalSlotsNeeded = 17;
        }

        int emptySlots = totalSlotsNeeded - filteredSlots.size();
        for (int i = 0; i < emptySlots; i++) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setBackground(Color.WHITE);
            emptyPanel.setBorder(null);
            container.add(emptyPanel);
        }

        container.revalidate();
        container.repaint();
    }

    private JButton createSlotButton(ParkingSlot slot) {
        JButton btn = new JButton("<html><center>" + slot.getId() + "</center></html>");
        btn.setPreferredSize(new Dimension(70, 60)); // Better proportions
        btn.setMinimumSize(new Dimension(70, 60));
        btn.setMaximumSize(new Dimension(70, 60));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(slot.getColor());
        btn.setBorder(new LineBorder(slot.getColor().darker(), 2, true));
        btn.setEnabled(false);

        btn.setToolTipText(slot.getId() + " - " + slot.getStatus());

        return btn;
    }

    public void refreshDashboardData() {
        try {
            List<ParkingSlot> slots = ParkingSlotDAO.getAllSlots();

            long totalSlots = slots.size();
            long occupiedSlots = slots.stream().filter(ParkingSlot::isOccupied).count();
            long availableSlots = totalSlots - occupiedSlots;

            double occupancyRate = totalSlots > 0 ? (occupiedSlots * 100.0) / totalSlots : 0;

            double todayRevenue = revenueController.getTodayRevenue();
            double monthRevenue = revenueController.getMonthRevenue();

            occupancyRateLabel.setText(String.format("%.2f%%", occupancyRate));
            availableSlotsLabel.setText(availableSlots + " Slots");
            todaysRevenueLabel.setText(String.format("₱%,.2f", todayRevenue));
            totalRevenueLabel.setText(String.format("₱%,.2f", monthRevenue));

            occupancyDescriptionLabel.setText(occupiedSlots + "/" + totalSlots + " slots occupied");

            buildGrid(carContainer, "Car", slots);
            buildGrid(motoContainer, "Motorcycle", slots);

        } catch (Exception e) {
            System.err.println("Error refreshing dashboard data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void startAutoRefresh() {
        refreshTimer = new Timer(5000, e -> {
            refreshDashboardData();
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