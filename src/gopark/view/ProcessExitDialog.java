package gopark.view;

import gopark.controller.VehicleEntryController;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ProcessExitDialog extends JDialog {

    public ProcessExitDialog(JFrame parent, String slotCode) {
        super(parent, "Process Vehicle Exit", true);
        setSize(420, 420);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(250, 250, 250));

        JPanel main = new JPanel();
        main.setBackground(Color.WHITE);
        main.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        Object[] entry = VehicleEntryController.loadActiveEntryForSlot(slotCode);
        if (entry == null) {
            JOptionPane.showMessageDialog(this, "No active entry found for slot " + slotCode, "Info", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            return;
        }

        long entryId = (Long) entry[0];
        String plate = (String) entry[1];
        String vType = (String) entry[2];
        Timestamp entryTime = (Timestamp) entry[3];

        JLabel heading = new JLabel("Process Vehicle Exit", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        heading.setForeground(new Color(50, 50, 50));
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(heading);
        main.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel card = new JPanel();
        card.setBackground(new Color(252, 249, 249));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setBackground(card.getBackground());

        JLabel plateLabel = new JLabel(plate);
        plateLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        plateLabel.setForeground(Color.BLACK);

        JLabel slotLabel = new JLabel(slotCode);
        slotLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        slotLabel.setForeground(new Color(100, 100, 100));

        topRow.add(plateLabel, BorderLayout.WEST);
        topRow.add(slotLabel, BorderLayout.EAST);

        card.add(topRow);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        card.add(sep);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel typeLabel = new JLabel("Vehicle Type: " + vType);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy, hh:mm a");
        String formattedEntryTime = sdf.format(entryTime);
        JLabel entryLabel = new JLabel("Entry Time: " + formattedEntryTime);

        double fee = VehicleEntryController.calculateFee(entryTime);
        JLabel durationLabel = new JLabel("Duration: " + VehicleEntryController.getDurationText(entryTime));
        JLabel totalLabel = new JLabel("Total Fee: ₱ " + String.format("%.2f", fee));

        for (JLabel lbl : new JLabel[]{typeLabel, entryLabel, durationLabel, totalLabel}) {
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lbl.setForeground(new Color(70, 70, 70));
            lbl.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
            card.add(lbl);
        }

        main.add(card);
        main.add(Box.createRigidArea(new Dimension(0, 25)));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttons.setBackground(Color.WHITE);

        JButton cancel = new JButton("Cancel");
        cancel.setBackground(new Color(245, 245, 245));
        cancel.setForeground(Color.DARK_GRAY);
        cancel.setFocusPainted(false);
        cancel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JButton collect = new JButton("Collect Payment");
        collect.setBackground(new Color(220, 0, 0));
        collect.setForeground(Color.WHITE);
        collect.setFocusPainted(false);
        collect.setFont(new Font("Segoe UI", Font.BOLD, 13));

        cancel.setPreferredSize(new Dimension(130, 36));
        collect.setPreferredSize(new Dimension(160, 36));

        cancel.addActionListener(e -> dispose());
        collect.addActionListener(e -> {
            boolean ok = VehicleEntryController.closeEntryAndFreeSlot(entryId, slotCode, fee);
            if (ok) {
                dispose(); // close the exit dialog
                Timestamp exitTime = new Timestamp(System.currentTimeMillis());
                new PaymentReceiptDialog(parent, plate, vType, slotCode, entryTime, exitTime, fee);
            }
            else {
                JOptionPane.showMessageDialog(this, "Failed to record payment.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttons.add(cancel);
        buttons.add(collect);
        main.add(buttons);

        add(main, BorderLayout.CENTER);
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
