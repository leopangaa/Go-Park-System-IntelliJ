package gopark.view;

import gopark.model.ParkingSlot;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class ParkingSlotView extends JPanel {

    private JLabel availableLabel, occupiedLabel, totalLabel;
    private JButton newEntryButton;
    private JPanel carContainer, motoContainer;
    private Runnable refreshCallback;

    public ParkingSlotView(List<ParkingSlot> slots) {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 242, 245));
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // Header Section
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main Content Section
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(new Color(240, 242, 245));

        // Stats Cards Section
        JPanel statsPanel = createStatsPanel();
        mainContent.add(statsPanel, BorderLayout.NORTH);

        // Instruction Panel
        JPanel instructionPanel = createInstructionPanel();
        mainContent.add(instructionPanel, BorderLayout.CENTER);

        // Parking Status Section
        JPanel parkingStatusPanel = createParkingStatusPanel();
        mainContent.add(parkingStatusPanel, BorderLayout.SOUTH);

        add(mainContent, BorderLayout.CENTER);

        updateSlots(slots);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 242, 245));
        headerPanel.setBorder(new EmptyBorder(0, 0, 30, 0));

        // Left side - GOPARK and ParkingMaster
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(240, 242, 245));

        JLabel mainTitle = new JLabel("Parking Slot Management");
        mainTitle.setFont(new Font("Arial", Font.BOLD, 28));
        mainTitle.setForeground(new Color(44, 62, 80));

        JLabel subtitle = new JLabel("Monitor and manage parking operations");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setForeground(new Color(127, 140, 141));

        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(mainTitle);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(240, 242, 245));

        newEntryButton = new JButton("+ New Entry");
        newEntryButton.setFont(new Font("Arial", Font.BOLD, 14));
        newEntryButton.setBackground(new Color(220, 53, 69));
        newEntryButton.setForeground(Color.WHITE);
        newEntryButton.setFocusPainted(false);
        newEntryButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        newEntryButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(240, 242, 245));
        buttonPanel.add(newEntryButton);

        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(new Color(240, 242, 245));
        statsPanel.setBorder(new EmptyBorder(0, 0, 30, 0));

        availableLabel = new JLabel("33 Slots", SwingConstants.CENTER);
        occupiedLabel = new JLabel("2 Slots", SwingConstants.CENTER);
        totalLabel = new JLabel("35 Slots", SwingConstants.CENTER);

        statsPanel.add(createStatCard("Available", availableLabel));
        statsPanel.add(createStatCard("Occupied", occupiedLabel));
        statsPanel.add(createStatCard("Total Capacity", totalLabel));

        return statsPanel;
    }

    private JPanel createStatCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(25, 20, 25, 20)
        ));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Value
        valueLabel.setFont(new Font("Arial", Font.BOLD, 32));
        valueLabel.setForeground(new Color(60, 60, 60));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(valueLabel);

        card.add(contentPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createInstructionPanel() {
        JPanel instructionPanel = new JPanel(new BorderLayout());
        instructionPanel.setBackground(new Color(240, 242, 245));
        instructionPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JPanel instructionCard = new JPanel(new BorderLayout());
        instructionCard.setBackground(new Color(255, 148, 148));
        instructionCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(255, 0, 0), 1),
                new EmptyBorder(8, 15, 8, 15)
        ));

        JLabel instructionText = new JLabel("Click RED slots for exits • Use \"New Entry\" for new vehicles");
        instructionText.setFont(new Font("Arial", Font.BOLD, 11));
        instructionText.setForeground(new Color(0, 0, 0));
        instructionText.setHorizontalAlignment(SwingConstants.CENTER);

        instructionCard.add(instructionText, BorderLayout.CENTER);
        instructionPanel.add(instructionCard, BorderLayout.CENTER);

        return instructionPanel;
    }

    private JPanel createParkingStatusPanel() {
        JPanel parkingPanel = new JPanel(new BorderLayout());
        parkingPanel.setBackground(new Color(240, 242, 245));

        // Main parking content panel
        JPanel parkingContent = new JPanel(new BorderLayout());
        parkingContent.setBackground(Color.WHITE);
        parkingContent.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(25, 25, 25, 25)
        ));

        // Parking slot containers
        JPanel parkingGridPanel = createParkingSlotContainers();
        parkingContent.add(parkingGridPanel, BorderLayout.CENTER);

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
        carSection.setBorder(new EmptyBorder(0, 0, 25, 0));

        JLabel carTitle = new JLabel("Car Parking Status");
        carTitle.setFont(new Font("Arial", Font.BOLD, 16));
        carTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        carSection.add(carTitle, BorderLayout.NORTH);

        carContainer = new JPanel();
        carContainer.setLayout(new GridLayout(2, 9, 10, 10));
        carContainer.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220)),
                new EmptyBorder(15, 15, 15, 15)
        ));
        carContainer.setBackground(Color.WHITE);
        carSection.add(carContainer, BorderLayout.CENTER);

        // Motorcycle Parking Section
        JPanel motoSection = new JPanel(new BorderLayout());
        motoSection.setBackground(Color.WHITE);

        JLabel motoTitle = new JLabel("Motorcycle Parking Status");
        motoTitle.setFont(new Font("Arial", Font.BOLD, 16));
        motoTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        motoSection.add(motoTitle, BorderLayout.NORTH);

        motoContainer = new JPanel();
        motoContainer.setLayout(new GridLayout(1, 17, 10, 10));
        motoContainer.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220)),
                new EmptyBorder(15, 15, 15, 15)
        ));
        motoContainer.setBackground(Color.WHITE);
        motoSection.add(motoContainer, BorderLayout.CENTER);

        mainPanel.add(carSection);
        mainPanel.add(motoSection);

        return mainPanel;
    }

    private void buildGrid(JPanel container, String type, List<ParkingSlot> slots) {
        container.removeAll();

        if (type.equalsIgnoreCase("Car")) {
            container.setLayout(new GridLayout(2, 9, 10, 10));
        } else {
            container.setLayout(new GridLayout(1, 17, 10, 10));
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

        int totalSlotsNeeded = type.equalsIgnoreCase("Car") ? 18 : 17;
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
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            if (!"AVAILABLE".equalsIgnoreCase(slot.getStatus())) {
                JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
                new ProcessExitDialog(parent, slot.getId());
                if (refreshCallback != null) refreshCallback.run();
            } else {
                JOptionPane.showMessageDialog(this, slot.getId() + " is available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return btn;
    }

    public void updateSlots(List<ParkingSlot> slots) {
        long occupied = slots.stream().filter(s -> !"AVAILABLE".equalsIgnoreCase(s.getStatus())).count();
        long available = slots.size() - occupied;
        long total = slots.size();

        availableLabel.setText(available + " Slots");
        occupiedLabel.setText(occupied + " Slots");
        totalLabel.setText(total + " Slots");

        buildGrid(carContainer, "Car", slots);
        buildGrid(motoContainer, "Motorcycle", slots);
    }

    public void setRefreshCallback(Runnable r) {
        refreshCallback = r;
    }

    public JButton getNewEntryButton() {
        return newEntryButton;
    }
}