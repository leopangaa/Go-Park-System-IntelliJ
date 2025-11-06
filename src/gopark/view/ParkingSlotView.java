package gopark.view;

import gopark.model.ParkingSlot;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ParkingSlotView extends JPanel {

    private JLabel availableLabel, occupiedLabel, totalLabel;
    private JButton newEntryButton;
    private JPanel carPanel, motorcyclePanel;

    public ParkingSlotView(List<ParkingSlot> slots) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // ===== HEADER =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(0, 0, 10, 0));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Parking Slot Management");
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel subtitle = new JLabel("Monitor and manage parking operations");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);

        titlePanel.add(title);
        titlePanel.add(subtitle);
        header.add(titlePanel, BorderLayout.WEST);

        newEntryButton = new JButton("+ New Entry");
        newEntryButton.setBackground(new Color(220, 53, 69));
        newEntryButton.setForeground(Color.WHITE);
        newEntryButton.setFont(new Font("Arial", Font.BOLD, 14));
        newEntryButton.setFocusPainted(false);
        newEntryButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        newEntryButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        header.add(newEntryButton, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ===== CENTER CONTENT =====
        JPanel mainContent = new JPanel();
        mainContent.setBackground(Color.WHITE);
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));

        // === STAT CARDS ===
        JPanel statPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statPanel.setBackground(Color.WHITE);
        statPanel.setBorder(new EmptyBorder(10, 0, 20, 0));

        availableLabel = new JLabel("0 Slots");
        occupiedLabel = new JLabel("0 Slots");
        totalLabel = new JLabel("0 Slots");

        statPanel.add(createStatCard("Available", availableLabel));
        statPanel.add(createStatCard("Occupied", occupiedLabel));
        statPanel.add(createStatCard("Total Capacity", totalLabel));

        mainContent.add(statPanel);

        // === INSTRUCTION BANNER ===
        JPanel instructionPanel = new JPanel();
        instructionPanel.setBackground(new Color(255, 240, 240));
        instructionPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 53, 69), 1),
                new EmptyBorder(10, 15, 10, 15)
        ));
        instructionPanel.setLayout(new BorderLayout());

        JLabel instructions = new JLabel(
                "Instructions: Click on any occupied slot (shown in red) to process vehicle exit and calculate parking fees. " +
                        "Use the “New Entry” button to assign vehicles to available slots."
        );
        instructions.setFont(new Font("Arial", Font.PLAIN, 13));
        instructions.setForeground(new Color(100, 0, 0));
        instructionPanel.add(instructions, BorderLayout.CENTER);
        mainContent.add(instructionPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // === CAR AND MOTORCYCLE PANELS ===
        carPanel = createSectionPanel("Car Parking Status");
        motorcyclePanel = createSectionPanel("Motorcycle Parking Status");

        mainContent.add(carPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 25)));
        mainContent.add(motorcyclePanel);

        add(mainContent, BorderLayout.CENTER);

        // Load initial data
        updateSlots(slots);
    }

    private JPanel createStatCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(20, 25, 20, 25)
        ));
        card.setPreferredSize(new Dimension(180, 100));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        titleLabel.setForeground(Color.GRAY);

        valueLabel.setFont(new Font("Arial", Font.BOLD, 22));
        valueLabel.setForeground(Color.BLACK);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(valueLabel);

        return card;
    }

    private JPanel createSectionPanel(String sectionTitle) {
        JPanel section = new JPanel();
        section.setLayout(new BorderLayout());
        section.setBackground(Color.WHITE);

        JLabel title = new JLabel(sectionTitle);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(new EmptyBorder(5, 0, 10, 0));
        section.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel();
        grid.setBackground(Color.WHITE);
        grid.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        section.add(grid, BorderLayout.CENTER);
        return section;
    }

    private void populateGrid(JPanel grid, String type, List<ParkingSlot> slots) {
        grid.removeAll();
        grid.setLayout(type.equalsIgnoreCase("Car") ?
                new GridLayout(2, 9, 10, 10) : new GridLayout(1, 17, 10, 10));

        for (ParkingSlot slot : slots) {
            if (!slot.getType().equalsIgnoreCase(type)) continue;

            JButton btn = new JButton(slot.getId());
            btn.setPreferredSize(new Dimension(60, 80));
            btn.setFocusPainted(false);
            btn.setFont(new Font("Arial", Font.BOLD, 13));
            btn.setForeground(Color.WHITE);
            btn.setBackground(slot.isOccupied() ? new Color(220, 53, 69) : new Color(40, 167, 69));
            btn.setBorder(new LineBorder(Color.WHITE, 2, true));
            grid.add(btn);
        }

        grid.revalidate();
        grid.repaint();
    }

    public void updateSlots(List<ParkingSlot> slots) {
        long occupied = slots.stream().filter(ParkingSlot::isOccupied).count();
        long available = slots.size() - occupied;
        long total = slots.size();

        availableLabel.setText(available + " Slots");
        occupiedLabel.setText(occupied + " Slots");
        totalLabel.setText(total + " Slots");

        populateGrid((JPanel) ((BorderLayout) carPanel.getLayout()).getLayoutComponent(BorderLayout.CENTER), "Car", slots);
        populateGrid((JPanel) ((BorderLayout) motorcyclePanel.getLayout()).getLayoutComponent(BorderLayout.CENTER), "Motorcycle", slots);
    }

    // === Getters ===
    public JLabel getAvailableLabel() { return availableLabel; }
    public JLabel getOccupiedLabel() { return occupiedLabel; }
    public JLabel getTotalLabel() { return totalLabel; }
    public JButton getNewEntryButton() { return newEntryButton; }
}
