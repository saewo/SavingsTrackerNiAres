package app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Utility class for UI constants and custom component helpers.
 */
public class UIUtils {

    // Colors from the screenshot
    public static final Color COLOR_SIDEBAR = new Color(0, 77, 64);      // #004D40 (Dark Teal)
    public static final Color COLOR_HEADER = new Color(0, 77, 64);       // #004D40
    public static final Color COLOR_ACCENT = new Color(29, 233, 182);    // #1DE9B6 (Bright Aqua)
    public static final Color COLOR_BACKGROUND = new Color(245, 247, 248); // #F5F7F8 (Light Grey)
    
    public static final Color COLOR_CARD_BALANCE = new Color(67, 160, 71); // #43A047 (Vibrant Green)
    public static final Color COLOR_CARD_PROFILE = new Color(124, 179, 66); // #7CB342 (Light Green)
    public static final Color COLOR_BUTTON_TEAL = new Color(0, 137, 122);   // #00897B (Teal)
    
    public static final Color COLOR_TEXT_WHITE = Color.WHITE;
    public static final Color COLOR_TEXT_DARK = new Color(51, 51, 51);    // #333333
    public static final Color COLOR_TEXT_MUTED = new Color(158, 158, 158); // #9E9E9E

    // Fonts
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 12);

    /**
     * Creates a rounded JPanel with a specific background color.
     */
    public static JPanel createRoundedPanel(int radius, Color backgroundColor) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(backgroundColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        return panel;
    }

    /**
     * Creates a styled JButton with rounded corners.
     */
    public static JButton createStyledButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFont(FONT_BOLD);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Basic rounded button simulation
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(button.getModel().isPressed() ? bg.darker() : bg);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 15, 15);
                g2.dispose();
                super.paint(g, c);
            }
        });
        
        return button;
    }

    /**
     * Helper to create an empty border for spacing.
     */
    public static EmptyBorder createPadding(int top, int left, int bottom, int right) {
        return new EmptyBorder(top, left, bottom, right);
    }
}
