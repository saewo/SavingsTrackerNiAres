package panels;

import model.BankAccount;
import model.SavingsTrackerSystem;
import model.User;
import model.Wallet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Profile panel for managing user profile and bank accounts.
 */
public class ProfilePanel extends JPanel {
    private JTextField nameField;
    private JPanel banksListPanel;
    private SavingsTrackerSystem system;

    public ProfilePanel() {
        this.system = SavingsTrackerSystem.getInstance();
        setLayout(new BorderLayout());
        setBackground(new Color(240, 242, 245));

        // --- Header ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(24, 119, 242));
        header.setPreferredSize(new Dimension(0, 80));
        
        JLabel titleLabel = new JLabel("User Profile & Accounts", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.add(titleLabel, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // --- Main Content ---
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(20, 40, 20, 40));

        // 1. User Name Section
        content.add(createSectionTitle("Edit Profile"));
        
        JPanel profileCard = createCard();
        profileCard.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        
        profileCard.add(new JLabel("Display Name:"));
        nameField = new JTextField(system.getCurrentUser().getName(), 15);
        profileCard.add(nameField);
        
        JButton updateNameBtn = createStyledButton("Update Name", new Color(24, 119, 242));
        updateNameBtn.addActionListener(e -> updateUserName());
        profileCard.add(updateNameBtn);
        
        content.add(profileCard);
        content.add(Box.createRigidArea(new Dimension(0, 30)));

        // 2. Bank Accounts Section
        JPanel bankHeader = new JPanel(new BorderLayout());
        bankHeader.setOpaque(false);
        bankHeader.add(createSectionTitle("Manage Bank Accounts"), BorderLayout.WEST);
        
        JButton addBankBtn = createStyledButton("+ Add Bank", new Color(66, 183, 42));
        addBankBtn.addActionListener(e -> showAddBankDialog());
        bankHeader.add(addBankBtn, BorderLayout.EAST);
        
        content.add(bankHeader);
        content.add(Box.createRigidArea(new Dimension(0, 10)));

        banksListPanel = new JPanel();
        banksListPanel.setLayout(new BoxLayout(banksListPanel, BoxLayout.Y_AXIS));
        banksListPanel.setOpaque(false);
        refreshBanksList();
        
        JScrollPane scrollPane = new JScrollPane(banksListPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        content.add(scrollPane);

        add(content, BorderLayout.CENTER);
    }

    private JLabel createSectionTitle(String title) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(50, 50, 50));
        label.setBorder(new EmptyBorder(0, 0, 10, 0));
        return label;
    }

    private JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        return card;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 15, 8, 15));
        return btn;
    }

    private void updateUserName() {
        String newName = nameField.getText().trim();
        if (newName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty.");
            return;
        }
        system.getCurrentUser().setName(newName);
        JOptionPane.showMessageDialog(this, "Profile updated!");
    }

    private void refreshBanksList() {
        banksListPanel.removeAll();
        List<BankAccount> banks = system.getCurrentUser().getBankAccounts();
        for (BankAccount bank : banks) {
            banksListPanel.add(createBankItem(bank));
            banksListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        banksListPanel.revalidate();
        banksListPanel.repaint();
    }

    private JPanel createBankItem(BankAccount bank) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(15, 20, 15, 20)
        ));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel nameLabel = new JLabel(bank.getBankName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        item.add(nameLabel, BorderLayout.WEST);

        double balance = bank.getTotalBalance();
        JLabel balanceLabel = new JLabel(String.format("Total: $%.2f", balance));
        balanceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        balanceLabel.setForeground(new Color(100, 100, 100));
        item.add(balanceLabel, BorderLayout.CENTER);

        JButton addWalletBtn = createStyledButton("+ Wallet", new Color(100, 100, 100));
        addWalletBtn.addActionListener(e -> showAddWalletDialog(bank));
        item.add(addWalletBtn, BorderLayout.EAST);

        return item;
    }

    private void showAddBankDialog() {
        String name = JOptionPane.showInputDialog(this, "Enter Bank Name:");
        if (name != null && !name.trim().isEmpty()) {
            BankAccount newBank = new BankAccount(name.trim(), "default_logo.png");
            // Automatically add a default wallet to new bank
            newBank.addWallet(new Wallet("Main", 0.0));
            system.getCurrentUser().addBankAccount(newBank);
            refreshBanksList();
        }
    }

    private void showAddWalletDialog(BankAccount bank) {
        String name = JOptionPane.showInputDialog(this, "Enter Wallet Name for " + bank.getBankName() + ":");
        if (name != null && !name.trim().isEmpty()) {
            bank.addWallet(new Wallet(name.trim(), 0.0));
            refreshBanksList();
        }
    }
}
