package gopark.view;

import gopark.dao.TransactionDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TransactionsView extends JPanel {

    private JTable transactionTable;
    private DefaultTableModel model;
    private JLabel totalTransactionsLabel;
    private JLabel totalRevenueLabel;
    private JLabel averageFeeLabel;

    public TransactionsView() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // ===== Header =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Transaction History");
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel subtitle = new JLabel("View and export all parking transactions");
        subtitle.setForeground(Color.GRAY);

        titlePanel.add(title);
        titlePanel.add(subtitle);
        headerPanel.add(titlePanel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // ===== Stats Cards - Matching ParkingSlotView design =====
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(new EmptyBorder(10, 0, 20, 0));

        totalTransactionsLabel = new JLabel("0 Completed Payments");
        totalRevenueLabel = new JLabel("P0.00");
        averageFeeLabel = new JLabel("P0.00");

        statsPanel.add(createStatCard("Total Transactions", totalTransactionsLabel));
        statsPanel.add(createStatCard("Total Revenue", totalRevenueLabel));
        statsPanel.add(createStatCard("Average Fee", averageFeeLabel));

        add(statsPanel, BorderLayout.CENTER);

        // ===== Table =====
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);

        // Table Header
        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(Color.WHITE);
        tableHeader.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel tableTitle = new JLabel("All Transactions");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 16));

        JTextField searchField = new JTextField("Search transactions..");
        searchField.setForeground(Color.GRAY);
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        tableHeader.add(tableTitle, BorderLayout.WEST);
        tableHeader.add(searchField, BorderLayout.EAST);

        // ===== Define columns =====
        String[] columns = {
                "Transaction ID", "Plate Number", "Vehicle",
                "Slot", "Entry Time", "Exit Time",
                "Duration", "Fee", "Status"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transactionTable = new JTable(model);
        transactionTable.setRowHeight(35);

        transactionTable.setShowHorizontalLines(true);
        transactionTable.setShowVerticalLines(true);
        transactionTable.setGridColor(new Color(220, 220, 220));

        transactionTable.setIntercellSpacing(new Dimension(1, 1));
        transactionTable.setFont(new Font("Arial", Font.PLAIN, 13));

        transactionTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        transactionTable.getTableHeader().setBackground(new Color(240, 240, 240));
        transactionTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        transactionTable.getTableHeader().setPreferredSize(new Dimension(0, 35));

        setColumnWidths();

        transactionTable.getColumnModel().getColumn(8).setCellRenderer(new StatusCellRenderer());

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        tableContainer.add(tableHeader, BorderLayout.NORTH);
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        add(tableContainer, BorderLayout.SOUTH);

        loadTransactions();
    }

    private JPanel createStatCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.GRAY);

        valueLabel.setFont(new Font("Arial", Font.BOLD, 22));

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(valueLabel);

        return card;
    }

    private void setColumnWidths() {
        transactionTable.getColumnModel().getColumn(0).setPreferredWidth(120); // Transaction ID
        transactionTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Plate Number
        transactionTable.getColumnModel().getColumn(2).setPreferredWidth(90);  // Vehicle
        transactionTable.getColumnModel().getColumn(3).setPreferredWidth(60);  // Slot
        transactionTable.getColumnModel().getColumn(4).setPreferredWidth(150); // Entry Time
        transactionTable.getColumnModel().getColumn(5).setPreferredWidth(150); // Exit Time
        transactionTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Duration
        transactionTable.getColumnModel().getColumn(7).setPreferredWidth(70);  // Fee
        transactionTable.getColumnModel().getColumn(8).setPreferredWidth(80);  // Status

        transactionTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    public void loadTransactions() {
        model.setRowCount(0);
        List<Object[]> transactions = TransactionDAO.getAllTransactions();

        int completedPaymentsCount = 0;
        double totalRevenue = 0;

        for (Object[] row : transactions) {
            if (row[0] != null) {
                try {
                    int transactionId = Integer.parseInt(row[0].toString());
                    row[0] = formatReceiptNumber(transactionId);
                } catch (NumberFormatException e) {
                    row[0] = row[0].toString();
                }
            }

            double originalFee = 0.0;
            if (row[7] != null && row[7] instanceof Double) {
                originalFee = (Double) row[7];
            }

            if (row[7] != null && row[7] instanceof Double) {
                double fee = (Double) row[7];
                row[7] = fee > 0 ? "P" + String.format("%.2f", fee) : "P0.00";
            }

            model.addRow(row);

            if (row[8] != null && ("Completed".equalsIgnoreCase(row[8].toString()) ||
                    "Paid".equalsIgnoreCase(row[8].toString()) ||
                    "Paixed".equalsIgnoreCase(row[8].toString()))) {
                completedPaymentsCount++;
                totalRevenue += originalFee;
            }
        }

        double avgFee = completedPaymentsCount > 0 ? totalRevenue / completedPaymentsCount : 0;

        if (totalTransactionsLabel != null) {
            totalTransactionsLabel.setText(completedPaymentsCount + " Completed Payments");
        }
        if (totalRevenueLabel != null) {
            totalRevenueLabel.setText("P" + String.format("%.2f", totalRevenue));
        }
        if (averageFeeLabel != null) {
            averageFeeLabel.setText("P" + String.format("%.2f", avgFee));
        }

        setColumnWidths();
    }

    public void refreshTransactions() {
        loadTransactions();
    }

    public static String formatReceiptNumber(int transactionId) {
        return String.format("TID-%05d", transactionId);
    }

    private class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value != null) {
                String status = value.toString().toLowerCase();

                if (status.contains("parked") || status.contains("paired") || status.contains("paixed")) {
                    c.setBackground(new Color(220, 255, 220)); // Light green
                    c.setForeground(new Color(0, 100, 0)); // Dark green text
                } else if (status.contains("completed") || status.contains("paid")) {
                    c.setBackground(new Color(255, 220, 220)); // Light red
                    c.setForeground(new Color(139, 0, 0)); // Dark red text
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }

                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);

                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(c.getBackground().darker(), 1),
                        BorderFactory.createEmptyBorder(2, 5, 2, 5)
                ));
            }

            return c;
        }
    }
}