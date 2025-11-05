package gopark.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class DashboardView extends JPanel {

    public DashboardView() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 40, 10, 40));

        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        headerPanel.add(title, BorderLayout.NORTH);

        JLabel subtitle = new JLabel("Monitor parking activity and performance metrics.");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        headerPanel.add(subtitle, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(245, 245, 245));
        content.setBorder(new EmptyBorder(10, 40, 40, 40));

        // Stat Card
        JPanel topCardsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        topCardsPanel.setOpaque(false);

        topCardsPanel.add(createStatsCard("Occupancy Rate", "0%", "0 / 36 Slots"));
        topCardsPanel.add(createStatsCard("Available Slots", "36", "Remaining"));
        topCardsPanel.add(createStatsCard("Today's Revenue", "₱0.00", "As of now"));
        topCardsPanel.add(createStatsCard("Total Revenue", "₱0.00", "This Month"));

        content.add(topCardsPanel);
        content.add(Box.createRigidArea(new Dimension(0, 25)));

        // Parking Status
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BorderLayout());
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        statusPanel.setPreferredSize(new Dimension(900, 400));

        JLabel statusTitle = new JLabel("Parking Lot Status");
        statusTitle.setFont(new Font("Arial", Font.BOLD, 18));
        statusPanel.add(statusTitle, BorderLayout.NORTH);

        JLabel placeholder = new JLabel("Parking Slot Overview", SwingConstants.CENTER);
        placeholder.setFont(new Font("Arial", Font.PLAIN, 14));
        placeholder.setForeground(Color.GRAY);
        statusPanel.add(placeholder, BorderLayout.CENTER);

        content.add(statusPanel);

        add(content, BorderLayout.CENTER);
    }

    // Create Stats Card
    private JPanel createStatsCard(String title, String value, String description) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setPreferredSize(new Dimension(200, 100));

        // Stats Card Content
        JPanel textPanel = new JPanel();
        textPanel.setBackground(Color.WHITE);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 13));
        lblTitle.setForeground(Color.GRAY);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.BOLD, 22));
        lblValue.setForeground(new Color(33, 33, 33));

        JLabel lblDesc = new JLabel(description);
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDesc.setForeground(Color.GRAY);

        textPanel.add(lblTitle);
        textPanel.add(lblValue);
        textPanel.add(lblDesc);

        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }
}
