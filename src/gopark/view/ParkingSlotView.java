package gopark.view;

import gopark.model.ParkingSlot;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ParkingSlotView extends JPanel {

    private JPanel carPanel;
    private JPanel motorcyclePanel;
    private JLabel availableLabel;
    private JLabel occupiedLabel;
    private JLabel totalLabel;
    private JButton newEntryButton;

    public ParkingSlotView(List<ParkingSlot> slots) {
        setLayout(null);
        setBackground(Color.WHITE);

        // Header
        JLabel title = new JLabel("Parking Slots");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBounds(40, 20, 400, 40);
        add(title);

        // Stat Card
        JPanel statCard1 = createStatCard("Available", "0");
        statCard1.setBounds(40, 80, 200, 80);
        add(statCard1);
        availableLabel = (JLabel) statCard1.getClientProperty("valueLabel");

        JPanel statCard2 = createStatCard("Occupied", "0");
        statCard2.setBounds(260, 80, 200, 80);
        add(statCard2);
        occupiedLabel = (JLabel) statCard2.getClientProperty("valueLabel");

        JPanel statCard3 = createStatCard("Total", String.valueOf(slots.size()));
        statCard3.setBounds(480, 80, 200, 80);
        add(statCard3);
        totalLabel = (JLabel) statCard3.getClientProperty("valueLabel");

        // New Entry
        newEntryButton = new JButton("+ New Entry");
        newEntryButton.setBackground(new Color(220, 53, 69));
        newEntryButton.setForeground(Color.WHITE);
        newEntryButton.setFont(new Font("Arial", Font.BOLD, 16));
        newEntryButton.setFocusPainted(false);
        newEntryButton.setBounds(1000, 90, 180, 50);
        add(newEntryButton);

        // Car Parking
        JLabel carLabel = new JLabel("Car Parking Slots");
        carLabel.setFont(new Font("Arial", Font.BOLD, 18));
        carLabel.setBounds(40, 190, 300, 30);
        add(carLabel);

        carPanel = new JPanel(new GridLayout(2, 9, 10, 10));
        carPanel.setBackground(Color.WHITE);
        JScrollPane carScroll = new JScrollPane(carPanel);
        carScroll.setBounds(40, 230, 1120, 150);
        carScroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(carScroll);

        // Motor Parking
        JLabel motoLabel = new JLabel("Motorcycle Parking Slots");
        motoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        motoLabel.setBounds(40, 400, 400, 30);
        add(motoLabel);

        motorcyclePanel = new JPanel(new GridLayout(1, 17, 10, 10));
        motorcyclePanel.setBackground(Color.WHITE);
        JScrollPane motoScroll = new JScrollPane(motorcyclePanel);
        motoScroll.setBounds(40, 440, 1120, 120);
        motoScroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(motoScroll);

        populateSlots(slots);
    }

    private JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel(null);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        titleLabel.setBounds(15, 10, 150, 20);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 22));
        valueLabel.setForeground(new Color(30, 144, 255));
        valueLabel.setBounds(15, 35, 150, 30);

        card.add(titleLabel);
        card.add(valueLabel);
        card.putClientProperty("valueLabel", valueLabel);
        return card;
    }

    private void populateSlots(List<ParkingSlot> slots) {
        carPanel.removeAll();
        motorcyclePanel.removeAll();

        for (ParkingSlot slot : slots) {
            JButton btn = new JButton(slot.getId());
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setBackground(slot.isOccupied() ? new Color(220, 53, 69) : new Color(40, 167, 69));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 12));

            if ("Car".equalsIgnoreCase(slot.getType())) {
                carPanel.add(btn);
            } else {
                motorcyclePanel.add(btn);
            }
        }

        revalidate();
        repaint();
    }

    // Default no database yet
    public ParkingSlotView() {
        this(new ArrayList<>());
    }

    // Getters for Controllers
    public JLabel getAvailableLabel() { return availableLabel; }
    public JLabel getOccupiedLabel() { return occupiedLabel; }
    public JLabel getTotalLabel() { return totalLabel; }
    public JButton getNewEntryButton() { return newEntryButton; }
}
