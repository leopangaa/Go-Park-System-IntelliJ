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
    private JFrame topParent; // used to pass as parent to dialogs
    private Runnable refreshCallback;

    public ParkingSlotView(List<ParkingSlot> slots) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        JPanel left = new JPanel();
        left.setBackground(Color.WHITE);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Parking Slot Management");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        JLabel subtitle = new JLabel("Monitor and manage parking operations");
        subtitle.setForeground(Color.GRAY);
        left.add(title);
        left.add(subtitle);
        header.add(left, BorderLayout.WEST);

        newEntryButton = new JButton("+ New Entry");
        newEntryButton.setBackground(new Color(220, 53, 69));
        newEntryButton.setForeground(Color.WHITE);
        newEntryButton.setFocusPainted(false);
        header.add(newEntryButton, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // stats
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.WHITE);

        JPanel statPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statPanel.setBackground(Color.WHITE);
        statPanel.setBorder(new EmptyBorder(10, 0, 20, 0));

        availableLabel = new JLabel("0 Slots");
        occupiedLabel = new JLabel("0 Slots");
        totalLabel = new JLabel("0 Slots");

        statPanel.add(createStatCard("Available", availableLabel));
        statPanel.add(createStatCard("Occupied", occupiedLabel));
        statPanel.add(createStatCard("Total Capacity", totalLabel));

        center.add(statPanel);

        // instruction
        JPanel info = new JPanel(new BorderLayout());
        info.setBackground(new Color(255, 240, 240));
        info.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(220, 53, 69), 1),
                new EmptyBorder(10, 10, 10, 10)));
        JLabel instr = new JLabel("<html>Click a yellow/red slot to process exit/payment.<br/>Use New Entry to assign an available slot.</html>");
        instr.setForeground(new Color(100, 0, 0));
        info.add(instr, BorderLayout.CENTER);
        center.add(info);
        center.add(Box.createRigidArea(new Dimension(0, 16)));

        // car section
        JPanel carSection = new JPanel(new BorderLayout());
        carSection.setBackground(Color.WHITE);
        JLabel carTitle = new JLabel("Car Parking Status");
        carTitle.setFont(new Font("Arial", Font.BOLD, 16));
        carSection.add(carTitle, BorderLayout.NORTH);
        carContainer = new JPanel();
        carContainer.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(220, 220, 220)), new EmptyBorder(10, 10, 10, 10)));
        carSection.add(new JScrollPane(carContainer), BorderLayout.CENTER);
        center.add(carSection);
        center.add(Box.createRigidArea(new Dimension(0, 25)));

        // motorcycle section
        JPanel motoSection = new JPanel(new BorderLayout());
        motoSection.setBackground(Color.WHITE);
        JLabel motoTitle = new JLabel("Motorcycle Parking Status");
        motoTitle.setFont(new Font("Arial", Font.BOLD, 16));
        motoSection.add(motoTitle, BorderLayout.NORTH);
        motoContainer = new JPanel();
        motoContainer.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(220, 220, 220)), new EmptyBorder(10, 10, 10, 10)));
        motoSection.add(new JScrollPane(motoContainer), BorderLayout.CENTER);
        center.add(motoSection);

        add(center, BorderLayout.CENTER);

        // render initial slots
        updateSlots(slots);
    }

    public void setRefreshCallback(Runnable r) {
        refreshCallback = r;
    }

    private JPanel createStatCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(230, 230, 230), 1, true), new EmptyBorder(15, 15, 15, 15)));
        JLabel t = new JLabel(title);
        t.setForeground(Color.GRAY);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 22));
        card.add(t);
        card.add(Box.createVerticalStrut(8));
        card.add(valueLabel);
        return card;
    }

    private void buildGrid(JPanel container, String type, List<ParkingSlot> slots) {
        container.removeAll();
        container.setLayout(type.equalsIgnoreCase("Car") ? new GridLayout(2, 9, 10, 10) : new GridLayout(1, 17, 10, 10));

        // Sort slots by numeric part of ID (e.g., C1 < C2 < C10)
        slots.stream()
                .filter(s -> s.getType().equalsIgnoreCase(type))
                .sorted((a, b) -> {
                    String numA = a.getId().replaceAll("\\D+", ""); // remove letters
                    String numB = b.getId().replaceAll("\\D+", "");
                    int nA = numA.isEmpty() ? 0 : Integer.parseInt(numA);
                    int nB = numB.isEmpty() ? 0 : Integer.parseInt(numB);
                    return Integer.compare(nA, nB);
                })
                .forEach(slot -> {
                    JButton btn = new JButton(slot.getId());
                    btn.setPreferredSize(new Dimension(60, 80));
                    btn.setFocusPainted(false);
                    btn.setFont(new Font("Arial", Font.BOLD, 13));
                    btn.setForeground(Color.WHITE);
                    btn.setBackground(slot.getColor());
                    btn.setBorder(new LineBorder(slot.getColor().darker(), 2, true));

                    btn.addActionListener(e -> {
                        if (!"AVAILABLE".equalsIgnoreCase(slot.getStatus())) {
                            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
                            new ProcessExitDialog(parent, slot.getId());
                            if (refreshCallback != null) refreshCallback.run();
                        } else {
                            JOptionPane.showMessageDialog(this, slot.getId() + " is available.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        }
                    });

                    container.add(btn);
                });

        container.revalidate();
        container.repaint();
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

    // getters
    public JButton getNewEntryButton() {
        return newEntryButton;
    }
}
