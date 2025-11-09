package gopark.view.dialog;

import gopark.controller.VehicleEntryController;
import gopark.view.components.HoverableButton;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ProcessExitDialog extends JDialog {

    public ProcessExitDialog(JFrame parent, String slotCode) {
        super(parent, "Process Vehicle Exit", true);
        setSize(440, 440);
        setResizable(false);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(240, 240, 240));

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
        double fee = VehicleEntryController.calculateFee(entryTime, vType);

        JPanel card = new NewEntryDialog.RoundedShadowPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        JLabel title = new JLabel("→ Process Vehicle Exit");
        title.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 17));
        title.setForeground(new Color(40, 40, 40));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);

        JLabel subtitle = new JLabel("Review parking details and collect payment");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(120, 120, 120));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitle);
        card.add(Box.createRigidArea(new Dimension(0, 20)));

        // Info Panel
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(new Color(255, 245, 245));
        info.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel plateLabel = new JLabel(plate);
        plateLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        info.add(plateLabel);
        info.add(new JLabel("Slot: " + slotCode));
        info.add(new JLabel("Vehicle Type: " + vType));
        info.add(new JLabel("Entry: " + new SimpleDateFormat("MM/dd/yy, hh:mm a").format(entryTime)));
        info.add(new JLabel("Duration: " + VehicleEntryController.getDurationText(entryTime)));
        info.add(new JLabel("Total Fee: ₱ " + String.format("%.2f", fee)));

        for (Component c : info.getComponents()) {
            if (c instanceof JLabel lbl) {
                lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                lbl.setForeground(new Color(60, 60, 60));
            }
        }

        card.add(info);
        card.add(Box.createRigidArea(new Dimension(0, 25)));

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttons.setBackground(Color.WHITE);

        HoverableButton cancel = new HoverableButton(
                "Cancel",
                new Color(245, 245, 245),
                new Color(230, 230, 230),
                new Color(60, 60, 60)
        );
        cancel.setPreferredSize(new Dimension(130, 36));

        HoverableButton collect = new HoverableButton(
                "Collect Payment",
                new Color(220, 0, 0),
                new Color(240, 30, 30)
        );

        collect.setPreferredSize(new Dimension(160, 36));

        collect.addActionListener(e -> {
            boolean ok = VehicleEntryController.closeEntryAndFreeSlot(entryId, slotCode, fee);
            if (ok) {
                dispose();
                Timestamp exitTime = new Timestamp(System.currentTimeMillis());
                new PaymentReceiptDialog(parent, plate, vType, slotCode, entryTime, exitTime, fee);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to record payment.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancel.addActionListener(e -> dispose());

        buttons.add(cancel);
        buttons.add(collect);
        card.add(buttons);

        add(card);
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
