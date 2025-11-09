package gopark.view.tabs;

import gopark.controller.RevenueController;
import gopark.dao.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

        // ===== HEADER =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(25, 40, 0, 40));

        JLabel title = new JLabel("Revenue Analytics");
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel subtitle = new JLabel("Track financial performance and insights");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(Color.DARK_GRAY);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(title);
        titlePanel.add(subtitle);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // ===== STAT CARDS =====
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        statsPanel.setBorder(new EmptyBorder(20, 40, 10, 40));
        statsPanel.setBackground(Color.WHITE);

        lblTodayRevenue = new JLabel("₱0.00", SwingConstants.CENTER);
        lblMonthlyRevenue = new JLabel("₱0.00", SwingConstants.CENTER);

        statsPanel.add(createStatsCard("Today’s Revenue", lblTodayRevenue, new Color(220, 0, 0)));
        statsPanel.add(createStatsCard("This Month’s Revenue", lblMonthlyRevenue, new Color(220, 0, 0)));

        add(statsPanel, BorderLayout.CENTER);

        // ===== CHART CONTAINER =====
        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBorder(new EmptyBorder(10, 40, 40, 40));
        chartContainer.setBackground(Color.WHITE);

        JPanel chartHeader = new JPanel(new BorderLayout());
        chartHeader.setBackground(new Color(250, 250, 250));
        chartHeader.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel chartTitle = new JLabel("₱  MONTHLY REVENUE");
        chartTitle.setFont(new Font("Arial", Font.BOLD, 15));
        chartHeader.add(chartTitle, BorderLayout.WEST);

        chartContainer.add(chartHeader, BorderLayout.NORTH);

        Map<String, Integer> monthlyRevenueData = revenueController.getMonthlyRevenue();
        RevenueChart chart = new RevenueChart(monthlyRevenueData);
        chartContainer.add(chart, BorderLayout.CENTER);

        add(chartContainer, BorderLayout.SOUTH);

        refreshData();
        startAutoRefresh();
    }

    private JPanel createStatsCard(String titleText, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(25, 30, 25, 30)
        ));
        card.setPreferredSize(new Dimension(200, 100));

        ImageIcon icon = new ImageIcon(getClass().getResource("/gopark/assets/icons/revenue.png"));
        Image scaled = icon.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaled));
        iconLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        titleLabel.setForeground(Color.BLACK);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(iconLabel);
        headerPanel.add(titleLabel);

        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        valueLabel.setForeground(Color.BLACK);
        valueLabel.setHorizontalAlignment(SwingConstants.LEFT);

        card.add(headerPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(200, 200, 200), 1, true),
                        new EmptyBorder(23, 28, 23, 28)
                ));
                card.setBackground(new Color(250, 250, 250));
                card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(230, 230, 230), 1, true),
                        new EmptyBorder(25, 30, 25, 30)
                ));
                card.setBackground(Color.WHITE);
                card.setCursor(Cursor.getDefaultCursor());
                card.repaint();
            }
        });

        return card;
    }

    private void refreshData() {
        try {
            double todaysRevenue = revenueController.getTodayRevenue();
            double monthRevenue = revenueController.getMonthRevenue();

            lblTodayRevenue.setText(String.format("₱%,.2f", todaysRevenue));
            lblMonthlyRevenue.setText(String.format("₱%,.2f", monthRevenue));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    // ====== REVENUE CHART ======
    static class RevenueChart extends JPanel {
        private final Map<String, Integer> data;

        public RevenueChart(Map<String, Integer> data) {
            this.data = data;
            setPreferredSize(new Dimension(800, 400));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(230, 230, 230), 1, true),
                    new EmptyBorder(20, 20, 20, 20)
            ));
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
            int barSpacing = 10;

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
                g2.drawString(label, padding - fm.stringWidth(label) - 5, y + 4);
            }

            int x = padding + 10;
            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                int value = entry.getValue();
                int barHeight = (int) ((value / (double) maxValue) * maxBarHeight);
                int y = height - 80 - barHeight;

                g2.setColor(new Color(220, 0, 0));
                g2.fillRoundRect(x, y, barWidth - barSpacing, barHeight, 5, 5);

                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.BOLD, 11));
                String valueText = "₱" + String.format("%,d", value);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(valueText, x + (barWidth - barSpacing - fm.stringWidth(valueText)) / 2, y - 5);

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
