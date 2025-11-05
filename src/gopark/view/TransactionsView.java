package gopark.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TransactionsView extends JPanel {

    private JTable transactionTable;
    private JTextField searchField;

    public TransactionsView() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 40, 10, 40));

        JLabel title = new JLabel("Transaction History");
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel subtitle = new JLabel("View and export all parking transactions");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(title);
        titlePanel.add(subtitle);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // Stats
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBorder(new EmptyBorder(0, 40, 20, 40));
        statsPanel.setBackground(Color.WHITE);

        statsPanel.add(createStatsCard("Total Transactions", "0 Completed Payments"));
        statsPanel.add(createStatsCard("Total Revenue", "₱0.00"));
        statsPanel.add(createStatsCard("Average Fee", "₱0.00"));

        add(statsPanel, BorderLayout.CENTER);

        // Table
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBorder(new EmptyBorder(10, 40, 40, 40));
        tableContainer.setBackground(Color.WHITE);

        // Search Bar
        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(new Color(255, 230, 230));
        tableHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel tableTitle = new JLabel("All Transactions");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 16));

        searchField = new JTextField("Search transactions..");
        searchField.setForeground(Color.GRAY);
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        tableHeader.add(tableTitle, BorderLayout.WEST);
        tableHeader.add(searchField, BorderLayout.EAST);

        // Table data
        String[] columns = {
                "Transaction ID", "Plate Number", "Vehicle", "Slot",
                "Entry Time", "Exit Time", "Duration", "Fee", "Status", "Action"
        };

        // Sample Data
        Object[][] data = {
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transactionTable = new JTable(model);
        transactionTable.setRowHeight(28);
        transactionTable.setShowGrid(false);
        transactionTable.setIntercellSpacing(new Dimension(0, 0));
        transactionTable.setFont(new Font("Arial", Font.PLAIN, 13));
        transactionTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        transactionTable.getTableHeader().setBackground(new Color(240, 240, 240));

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        tableContainer.add(tableHeader, BorderLayout.NORTH);
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        add(tableContainer, BorderLayout.SOUTH);
    }

    // Create Stat Card
    private JPanel createStatsCard(String title, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Arial", Font.PLAIN, 14));
        t.setForeground(Color.DARK_GRAY);

        JLabel v = new JLabel(value);
        v.setFont(new Font("Arial", Font.BOLD, 16));

        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        return card;
    }
}
