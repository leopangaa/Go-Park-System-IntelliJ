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

        newEntryButton.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            new NewEntryDialog(parentFrame);
        });


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

        JPanel availableCard = createStatCard("Available", "20 Slots");
        availableLabel = (JLabel) availableCard.getClientProperty("valueLabel");

        JPanel occupiedCard = createStatCard("Occupied", "15 Slots");
        occupiedLabel = (JLabel) occupiedCard.getClientProperty("valueLabel");

        JPanel totalCard = createStatCard("Total Capacity", "35 Slots");
        totalLabel = (JLabel) totalCard.getClientProperty("valueLabel");

        statPanel.add(availableCard);
        statPanel.add(occupiedCard);
        statPanel.add(totalCard);
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

        // === CAR PARKING SECTION ===
        mainContent.add(createSection("Car Parking Status", "Car", slots));
        mainContent.add(Box.createRigidArea(new Dimension(0, 25)));

        // === MOTORCYCLE PARKING SECTION ===
        mainContent.add(createSection("Motorcycle Parking Status", "Motorcycle", slots));

        add(mainContent, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(20, 25, 20, 25)
        ));
        card.setPreferredSize(new Dimension(180, 100));
        card.setOpaque(true);
        card.setAlignmentY(Component.TOP_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        titleLabel.setForeground(Color.GRAY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 22));
        valueLabel.setForeground(Color.BLACK);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(valueLabel);
        card.putClientProperty("valueLabel", valueLabel);

        return card;
    }

    private JPanel createSection(String sectionTitle, String type, List<ParkingSlot> slots) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);

        JLabel title = new JLabel(sectionTitle);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(new EmptyBorder(5, 0, 10, 0));
        section.add(title);

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JPanel gridPanel;
        if (type.equalsIgnoreCase("Car")) {
            gridPanel = new JPanel(new GridLayout(2, 9, 10, 10));
        } else {
            gridPanel = new JPanel(new GridLayout(1, 17, 10, 10));
        }

        gridPanel.setBackground(Color.WHITE);
        populateSlots(type, slots, gridPanel);

        container.add(gridPanel, BorderLayout.CENTER);
        section.add(container);

        return section;
    }

    private void populateSlots(String type, List<ParkingSlot> slots, JPanel panel) {
        panel.removeAll();

        // Demo placeholder slots if empty
        List<ParkingSlot> data = slots.isEmpty() ? getSampleSlots(type) : slots;

        for (ParkingSlot slot : data) {
            if (!slot.getType().equalsIgnoreCase(type)) continue;

            JButton btn = new JButton(slot.getId());
            btn.setPreferredSize(new Dimension(60, 80));
            btn.setFocusPainted(false);
            btn.setFont(new Font("Arial", Font.BOLD, 13));
            btn.setForeground(Color.WHITE);
            btn.setBackground(slot.isOccupied() ? new Color(220, 53, 69) : new Color(40, 167, 69));
            btn.setBorder(new LineBorder(Color.WHITE, 2, true));
            panel.add(btn);
        }
    }

    // demo placeholders
    private List<ParkingSlot> getSampleSlots(String type) {
        List<ParkingSlot> list = new ArrayList<>();
        if (type.equalsIgnoreCase("Car")) {
            for (int i = 1; i <= 18; i++) {
                list.add(new ParkingSlot("C" + i, i % 3 == 0, "Car"));
            }
        } else {
            for (int i = 1; i <= 17; i++) {
                list.add(new ParkingSlot("M" + i, i % 2 == 0, "Motorcycle"));
            }
        }
        return list;
    }

    // default constructor
    public ParkingSlotView() {
        this(new ArrayList<>());
    }

    public JLabel getAvailableLabel() {
        return availableLabel;
    }

    public JLabel getOccupiedLabel() {
        return occupiedLabel;
    }

    public JLabel getTotalLabel() {
        return totalLabel;
    }

    public JButton getNewEntryButton() {
        return newEntryButton;
    }
}
