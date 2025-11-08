package gopark.view;

import gopark.controller.VehicleEntryController;
import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class PaymentReceiptDialog extends JDialog {

    public PaymentReceiptDialog(JFrame parent, String plate, String vType, String slotCode,
                                Timestamp entryTime, Timestamp exitTime, double totalFee) {
        super(parent, "Payment Receipt", true);

        // === BASIC SETTINGS ===
        setSize(480, 700);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        setLocationRelativeTo(parent);

        // Add soft card border
        getRootPane().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("MM/dd/yy, hh:mm a");

        JPanel main = new JPanel();
        main.setBackground(Color.WHITE);
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // === HEADER ===
        JLabel title = new JLabel("Payment Receipt", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Transaction completed successfully", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(100, 100, 100));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        main.add(title);
        main.add(subtitle);
        main.add(Box.createRigidArea(new Dimension(0, 15)));

        // === LOGO === (keep your original loading)
        JLabel logo = new JLabel(new ImageIcon(getClass().getResource("/gopark/assets/gopark_logo.png")));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Auto resize logo if too big
        ImageIcon icon = (ImageIcon) logo.getIcon();
        if (icon != null && icon.getIconWidth() > 160) {
            Image scaled = icon.getImage().getScaledInstance(160, -1, Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(scaled));
        }

        main.add(logo);
        main.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel brand = new JLabel("GoPark", SwingConstants.CENTER);
        brand.setFont(new Font("Segoe UI", Font.BOLD, 16));
        brand.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(brand);

        JLabel tagline = new JLabel("Automated Parking Management", SwingConstants.CENTER);
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tagline.setForeground(new Color(110, 110, 110));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(tagline);

        main.add(Box.createRigidArea(new Dimension(0, 18)));

        // === RECEIPT HEADER ===
        JLabel receiptLabel = new JLabel("PARKING RECEIPT", SwingConstants.CENTER);
        receiptLabel.setOpaque(true);
        receiptLabel.setBackground(new Color(220, 0, 0));
        receiptLabel.setForeground(Color.WHITE);
        receiptLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        receiptLabel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        receiptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(receiptLabel);

        main.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel receiptId = new JLabel("Receipt #" + System.currentTimeMillis(), SwingConstants.CENTER);
        receiptId.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        receiptId.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(receiptId);

        JLabel dateIssued = new JLabel(dateFormat.format(exitTime), SwingConstants.CENTER);
        dateIssued.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateIssued.setForeground(new Color(100, 100, 100));
        dateIssued.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(dateIssued);

        main.add(Box.createRigidArea(new Dimension(0, 18)));

        // === INFO SECTIONS ===
        JPanel infoCard = createInfoSection("Vehicle Information", new String[][]{
                {"Plate Number:", plate},
                {"Vehicle Type:", vType},
                {"Parking Slot:", slotCode}
        });

        JPanel durationCard = createInfoSection("Parking Duration", new String[][]{
                {"Entry:", timeFormat.format(entryTime)},
                {"Exit:", timeFormat.format(exitTime)},
                {"Total Duration:", VehicleEntryController.getDurationText(entryTime)}
        });

        JPanel paymentCard = createInfoSection("Payment Details", new String[][]{
                {"Parking Fee:", "₱" + String.format("%.2f", totalFee)},
                {"Total Amount Paid:", "₱" + String.format("%.2f", totalFee)},
                {"Status:", "Completed"}
        });

        main.add(infoCard);
        main.add(Box.createRigidArea(new Dimension(0, 8)));
        main.add(durationCard);
        main.add(Box.createRigidArea(new Dimension(0, 8)));
        main.add(paymentCard);
        main.add(Box.createRigidArea(new Dimension(0, 20)));

        // === FOOTER ===
        JLabel thanks = new JLabel("Thank you for parking with us!");
        thanks.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        thanks.setForeground(new Color(80, 80, 80));
        thanks.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(thanks);

        JLabel note = new JLabel("Drive safely and visit again", SwingConstants.CENTER);
        note.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        note.setForeground(new Color(130, 130, 130));
        note.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(note);

        main.add(Box.createRigidArea(new Dimension(0, 20)));

        // === BUTTONS ===
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setBackground(Color.WHITE);

        JButton printBtn = new JButton("Print");
        JButton downloadBtn = new JButton("Download");

        for (JButton b : new JButton[]{printBtn, downloadBtn}) {
            b.setFocusPainted(false);
            b.setBackground(new Color(245, 245, 245));
            b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            b.setPreferredSize(new Dimension(100, 32));
        }

        btnPanel.add(printBtn);
        btnPanel.add(downloadBtn);
        main.add(btnPanel);

        // === SCROLLING WITHOUT SCROLLBARS ===
        JScrollPane scrollPane = new JScrollPane(main);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER); // hides scrollbars
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // smooth scroll with mouse wheel

        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createInfoSection(String title, String[][] data) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(250, 250, 250));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel header = new JLabel(title);
        header.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        header.setForeground(new Color(60, 60, 60));
        panel.add(header);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        for (String[] item : data) {
            JLabel line = new JLabel(item[0] + " " + item[1]);
            line.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            line.setForeground(new Color(70, 70, 70));
            panel.add(line);
        }

        panel.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return panel;
    }
}
