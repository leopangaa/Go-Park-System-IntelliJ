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
        setBackground(new Color(240, 242, 245));
        setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.CENTER);

        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.SOUTH);

        loadTransactions();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 242, 245));
        headerPanel.setBorder(new EmptyBorder(0, 0, 30, 0));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(240, 242, 245));

        JLabel mainTitle = new JLabel("Transaction History");
        mainTitle.setFont(new Font("Arial", Font.BOLD, 28));
        mainTitle.setForeground(new Color(44, 62, 80));

        JLabel subtitle = new JLabel("View and export parking transactions");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setForeground(new Color(127, 140, 141));

        titlePanel.add(mainTitle);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        return headerPanel;
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(new Color(240, 242, 245));
        statsPanel.setBorder(new EmptyBorder(0, 0, 30, 0));

        totalTransactionsLabel = new JLabel("2 Completed Payments", SwingConstants.CENTER);
        totalRevenueLabel = new JLabel("P50.00", SwingConstants.CENTER);
        averageFeeLabel = new JLabel("P50.00", SwingConstants.CENTER);

        statsPanel.add(createStatCard("Total Transactions", totalTransactionsLabel));
        statsPanel.add(createStatCard("Total Revenue", totalRevenueLabel));
        statsPanel.add(createStatCard("Average Fee", averageFeeLabel));

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

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(240, 242, 245));

        // Table Title
        JLabel tableTitle = new JLabel("All Transactions");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 22));
        tableTitle.setForeground(new Color(44, 62, 80));
        tableTitle.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Main table content panel
        JPanel tableContent = new JPanel(new BorderLayout());
        tableContent.setBackground(Color.WHITE);
        tableContent.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(25, 25, 25, 25)
        ));

        String[] columns = {
                "Transaction ID", "Plate Number", "Vehicle", "Slot",
                "Entry Time", "Exit Time", "Duration", "Fee", "Status"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transactionTable = new JTable(model);
        transactionTable.setRowHeight(35);
        transactionTable.setFont(new Font("Arial", Font.PLAIN, 12));

        transactionTable.setShowHorizontalLines(true);
        transactionTable.setShowVerticalLines(true);
        transactionTable.setGridColor(new Color(220, 220, 220));
        transactionTable.setIntercellSpacing(new Dimension(1, 0));

        transactionTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        transactionTable.getTableHeader().setBackground(new Color(248, 249, 250));
        transactionTable.getTableHeader().setForeground(new Color(33, 37, 41));
        transactionTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(222, 226, 230)));
        transactionTable.getTableHeader().setPreferredSize(new Dimension(0, 35));

        setColumnWidths();
        transactionTable.getColumnModel().getColumn(8).setCellRenderer(new StatusCellRenderer());

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        tableContent.add(scrollPane, BorderLayout.CENTER);

        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(tableContent, BorderLayout.CENTER);

        return tablePanel;
    }

    private void setColumnWidths() {
        transactionTable.getColumnModel().getColumn(0).setPreferredWidth(150); // Transaction ID
        transactionTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Plate Number
        transactionTable.getColumnModel().getColumn(2).setPreferredWidth(90);  // Vehicle
        transactionTable.getColumnModel().getColumn(3).setPreferredWidth(60);  // Slot
        transactionTable.getColumnModel().getColumn(4).setPreferredWidth(150); // Entry Time
        transactionTable.getColumnModel().getColumn(5).setPreferredWidth(150); // Exit Time
        transactionTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Duration
        transactionTable.getColumnModel().getColumn(7).setPreferredWidth(80);  // Fee
        transactionTable.getColumnModel().getColumn(8).setPreferredWidth(100); // Status
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
            totalTransactionsLabel.setText(completedPaymentsCount + "");
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
        return String.format("TXD-%05d", transactionId);
    }

    // ORIGINAL StatusCellRenderer
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