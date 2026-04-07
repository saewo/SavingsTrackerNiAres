package panels;

import model.SavingsDataStore;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard panel displaying overview statistics of savings.
 */
public class DashboardPanel extends JPanel {
    private JLabel totalSavingsLabel;
    private JLabel totalRecordsLabel;

    public DashboardPanel() {
        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Savings Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        add(title, BorderLayout.NORTH);

        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        totalSavingsLabel = new JLabel("Total Balance: $0.00", SwingConstants.CENTER);
        totalSavingsLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        statsPanel.add(totalSavingsLabel);

        totalRecordsLabel = new JLabel("Total Transactions: 0", SwingConstants.CENTER);
        totalRecordsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        statsPanel.add(totalRecordsLabel);

        add(statsPanel, BorderLayout.CENTER);

        // Welcome text
        JLabel welcome = new JLabel("Track your savings and grow your wealth!", SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.ITALIC, 16));
        welcome.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        add(welcome, BorderLayout.SOUTH);

        // Initial update
        refreshStats();
    }

    public void refreshStats() {
        double total = SavingsDataStore.getInstance().getTotalSavings();
        int count = SavingsDataStore.getInstance().getCount();

        totalSavingsLabel.setText(String.format("Total Balance: $%.2f", total));
        totalRecordsLabel.setText("Total Transactions: " + count);
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            refreshStats();
        }
    }
}