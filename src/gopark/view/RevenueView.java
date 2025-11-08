package gopark.view;

import gopark.controller.RevenueController;
import gopark.model.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class RevenueView extends JPanel {

    private JLabel lblTodayRevenue;
    private JLabel lblMonthlyRevenue;
    private RevenueController revenueController;
    private Timer refreshTimer;

    public RevenueView() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        revenueController = new RevenueController(DBConnection.getConnection());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 40, 10, 40));

        JLabel title = new JLabel("Revenue Analytics");
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel subtitle = new JLabel("Track financial performance and insights");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(title);
        titlePanel.add(subtitle);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        statsPanel.setBorder(new EmptyBorder(10, 40, 20, 40));
        statsPanel.setBackground(Color.WHITE);

        lblTodayRevenue = new JLabel("₱0.00");
        lblMonthlyRevenue = new JLabel("₱0.00");

        statsPanel.add(createStatsCard("Today's Revenue", lblTodayRevenue));
        statsPanel.add(createStatsCard("This Month's Revenue", lblMonthlyRevenue));

        add(statsPanel, BorderLayout.CENTER);

        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshPanel.setBackground(Color.WHITE);
        refreshPanel.setBorder(new EmptyBorder(0, 40, 10, 40));

        JButton refreshButton = new JButton("Refresh Data");
        refreshButton.setBackground(new Color(70, 130, 180));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 12));
        refreshButton.addActionListener(e -> refreshData());

        refreshPanel.add(refreshButton);
        add(refreshPanel, BorderLayout.SOUTH);

        refreshData();

        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBorder(new EmptyBorder(10, 40, 40, 40));
        chartContainer.setBackground(Color.WHITE);

        JPanel chartHeader = new JPanel(new BorderLayout());
        chartHeader.setBackground(new Color(245, 245, 245));
        chartHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel chartTitle = new JLabel("₱ MONTHLY REVENUE");
        chartTitle.setFont(new Font("Arial", Font.BOLD, 16));
        chartHeader.add(chartTitle, BorderLayout.WEST);

        chartContainer.add(chartHeader, BorderLayout.NORTH);

        Map<String, Integer> monthlyRevenueData = revenueController.getMonthlyRevenue();
        RevenueChart chart = new RevenueChart(monthlyRevenueData);
        chartContainer.add(chart, BorderLayout.CENTER);

        add(chartContainer, BorderLayout.SOUTH);

        startAutoRefresh();
    }

    private void refreshData() {
        try {
            // Update stat cards
            double todaysRevenue = revenueController.getTodayRevenue();
            double monthRevenue = revenueController.getMonthRevenue();

            lblTodayRevenue.setText(String.format("₱%.2f", todaysRevenue));
            lblMonthlyRevenue.setText(String.format("₱%.2f", monthRevenue));

            Map<String, Integer> monthlyRevenueData = revenueController.getMonthlyRevenue();
            refreshChart(monthlyRevenueData);

            showRefreshFeedback();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error refreshing revenue data: " + e.getMessage(),
                    "Refresh Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshChart(Map<String, Integer> newData) {
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                Component[] subComps = panel.getComponents();
                for (Component subComp : subComps) {
                    if (subComp instanceof RevenueChart) {
                        panel.remove(subComp);
                        RevenueChart newChart = new RevenueChart(newData);
                        panel.add(newChart, BorderLayout.CENTER);
                        panel.revalidate();
                        panel.repaint();
                        return;
                    }
                }
            }
        }
    }

    private void showRefreshFeedback() {
        lblTodayRevenue.setForeground(Color.BLUE);
        lblMonthlyRevenue.setForeground(Color.BLUE);

        Timer feedbackTimer = new Timer(500, e -> {
            lblTodayRevenue.setForeground(Color.BLACK);
            lblMonthlyRevenue.setForeground(Color.BLACK);
        });
        feedbackTimer.setRepeats(false);
        feedbackTimer.start();
    }

    private void startAutoRefresh() {
        refreshTimer = new Timer(30000, e -> {
            refreshData();
            System.out.println("Auto-refreshed revenue data at: " + new java.util.Date());
        });
        refreshTimer.start();
    }

    public void stopAutoRefresh() {
        if (refreshTimer != null && refreshTimer.isRunning()) {
            refreshTimer.stop();
        }
    }

    private JPanel createStatsCard(String label, JLabel valueLabel) {
        JPanel card = new JPanel(new GridLayout(1, 2));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(20, 20, 20, 20)
        ));

        ImageIcon revenueIcon = new ImageIcon(getClass().getResource("/gopark/assets/revenue.png"));
        Image scaled = revenueIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel icon = new JLabel(new ImageIcon(scaled));

        JLabel title = new JLabel(label);
        title.setFont(new Font("Arial", Font.BOLD, 16));

        valueLabel.setFont(new Font("Arial", Font.BOLD, 18));
        valueLabel.setForeground(new Color(0, 100, 0));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(title);
        textPanel.add(valueLabel);

        card.add(icon);
        card.add(textPanel);

        return card;
    }

    static class RevenueChart extends JPanel {
        private final Map<String, Integer> data;

        public RevenueChart(Map<String, Integer> data) {
            this.data = data;
            setPreferredSize(new Dimension(800, 400));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int padding = 60;
            int maxBarHeight = height - 150;

            if (data.isEmpty()) {
                g2.setColor(Color.GRAY);
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                String message = "No revenue data available";
                FontMetrics fm = g2.getFontMetrics();
                int messageWidth = fm.stringWidth(message);
                g2.drawString(message, (width - messageWidth) / 2, height / 2);
                return;
            }

            int maxValue = data.values().stream().max(Integer::compareTo).orElse(1);
            int barWidth = Math.max(30, (width - (2 * padding)) / data.size());

            g2.setColor(Color.GRAY);
            g2.drawLine(padding, height - 80, width - padding, height - 80);
            g2.drawLine(padding, height - 80, padding, padding);

            g2.setFont(new Font("Arial", Font.PLAIN, 11));
            int ySteps = 5;
            for (int i = 0; i <= ySteps; i++) {
                int y = height - 80 - (i * maxBarHeight / ySteps);
                int value = (i * maxValue / ySteps);
                String label = "₱" + value;
                FontMetrics fm = g2.getFontMetrics();
                int labelWidth = fm.stringWidth(label);
                g2.drawString(label, padding - labelWidth - 5, y + 4);
                g2.drawLine(padding - 3, y, padding, y);
            }

            int x = padding + 10;
            int barSpacing = 10;

            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                int value = entry.getValue();
                int barHeight = (int) ((value / (double) maxValue) * maxBarHeight);
                int y = height - 80 - barHeight;

                g2.setColor(Color.RED);
                g2.fillRect(x, y, barWidth - barSpacing, barHeight);

                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.BOLD, 11));
                String valueText = "₱" + value;
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(valueText);
                g2.drawString(valueText, x + (barWidth - barSpacing - textWidth) / 2, y - 5);

                g2.setFont(new Font("Arial", Font.PLAIN, 12));
                g2.drawString(entry.getKey(), x + (barWidth - barSpacing) / 2 - 10, height - 60);

                x += barWidth;
            }

            g2.setFont(new Font("Arial", Font.BOLD, 13));
            g2.drawString("Month", width / 2 - 20, height - 30);

            g2.rotate(-Math.PI / 2);
            g2.drawString("Revenue (₱)", -height / 2 - 30, padding - 30);
            g2.rotate(Math.PI / 2);
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        stopAutoRefresh();
    }
}