package panels;

import model.SavingsTrackerSystem;
import model.User;
import model.BankAccount;
import model.Wallet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Dashboard panel displaying overview statistics of savings.
 */
public class DashboardPanel extends JPanel {
    private JLabel totalSavingsLabel;
    private JLabel totalRecordsLabel;
    private JLabel userNameLabel;

    public DashboardPanel(JTabbedPane tabbedPane) {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 242, 245));

        // --- Header ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(255, 182, 193));
        header.setPreferredSize(new Dimension(0, 100));
        
        JLabel titleLabel = new JLabel("Your Savings Dashboard", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.add(titleLabel, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // --- Stats Panel ---
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(30, 50, 30, 50));

        // User Greeting
        userNameLabel = new JLabel("Welcome back, Ares!", SwingConstants.LEFT);
        userNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        userNameLabel.setForeground(new Color(50, 50, 50));
        userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(userNameLabel);
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        // Balance Card
        JPanel balanceCard = createStatCard(new Color(255, 255, 255));
        totalSavingsLabel = new JLabel("Total Assets: $0.00", SwingConstants.CENTER);
        totalSavingsLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        totalSavingsLabel.setForeground(new Color(24, 119, 242));
        balanceCard.add(totalSavingsLabel);
        content.add(balanceCard);
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        // Transactions Card
        JPanel recordsCard = createStatCard(new Color(255, 255, 255));
        totalRecordsLabel = new JLabel("Total Transactions: 0", SwingConstants.CENTER);
        totalRecordsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        recordsCard.add(totalRecordsLabel);
        content.add(recordsCard);
        totalRecordsLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tabbedPane.setSelectedIndex(3); // switch to Tab 2
            }
        });
        add(content, BorderLayout.CENTER);

        // Footer
        JLabel welcome = new JLabel("Manage your money across all your bank accounts", SwingConstants.CENTER);
        welcome.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        welcome.setForeground(new Color(100, 100, 100));
        welcome.setBorder(new EmptyBorder(0, 0, 30, 0));
        add(welcome, BorderLayout.SOUTH);

        refreshStats();
    }

    private JPanel createStatCard(Color bg) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(bg);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(30, 30, 30, 30)
        ));
        card.setMaximumSize(new Dimension(600, 150));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        return card;
    }

    public void refreshStats() {
        SavingsTrackerSystem system = SavingsTrackerSystem.getInstance();
        double total = system.computeTotalAssets();
        User user = system.getCurrentUser();
        
        int count = 0;
        for (BankAccount bank : user.getBankAccounts()) {
            for (Wallet wallet : bank.getWallets()) {
                count += wallet.getTransactions().size();
            }
        }

        userNameLabel.setText("Welcome back, " + user.getName() + "!");
        totalSavingsLabel.setText(String.format("Total Assets: $%.2f", total));
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
