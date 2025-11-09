package gopark.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HoverableButton extends JButton {

    private final Color baseColor;
    private final Color hoverColor;
    private final Color textColor;
    private int cornerRadius = 10; // Rounded edges

    public HoverableButton(String text, Color baseColor, Color hoverColor) {
        this(text, baseColor, hoverColor, Color.WHITE);
    }

    public HoverableButton(String text, Color baseColor, Color hoverColor, Color textColor) {
        super(text);
        this.baseColor = baseColor;
        this.hoverColor = hoverColor;
        this.textColor = textColor;

        setForeground(textColor);
        setBackground(baseColor);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setContentAreaFilled(false); // We'll custom-paint the button
        setOpaque(false);
        setPreferredSize(new Dimension(160, 40));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(baseColor);
                repaint();
            }
        });
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Button fill
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Optional subtle shadow
        g2.setColor(new Color(0, 0, 0, 30));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        // Text
        FontMetrics fm = g2.getFontMetrics();
        Rectangle r = new Rectangle(0, 0, getWidth(), getHeight());
        g2.setColor(textColor);
        g2.drawString(getText(),
                r.x + (r.width - fm.stringWidth(getText())) / 2,
                r.y + ((r.height - fm.getHeight()) / 2) + fm.getAscent());

        g2.dispose();
    }
}
