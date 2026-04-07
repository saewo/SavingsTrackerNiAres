package app;

import panels.*;

import javax.swing.*;
import java.awt.*;

/**
 * Main application for the Savings Tracker Ni Ares.
 */
public class SavingsTrackerNiAres extends JFrame {

    public SavingsTrackerNiAres() {
        setTitle("Savings Tracker Ni Ares");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null); // Center on screen

        // --- Tabbed Pane ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        tabbedPane.addTab("Dashboard", new DashboardPanel());
        tabbedPane.addTab("Profile", new ProfilePanel());
        tabbedPane.addTab("Add Record", new AddSavingsPanel());
        tabbedPane.addTab("View Records", new ViewSavingsPanel());
        tabbedPane.addTab("Search", new SearchSavingsPanel());
        tabbedPane.addTab("Edit / Delete", new EditSavingsPanel());

        add(tabbedPane, BorderLayout.CENTER);

        // Styling
        tabbedPane.setBackground(new Color(255, 255, 255));
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            SavingsTrackerNiAres app = new SavingsTrackerNiAres();
            app.setVisible(true);
        });
    }
}