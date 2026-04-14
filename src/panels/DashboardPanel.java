package panels;

import app.SavingsTrackerNiAres;
import app.UIUtils;
import model.SavingsTrackerSystem;
import model.User;
import model.BankAccount;
import model.Wallet;
import model.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;

/**
 * Redesigned Dashboard panel matching the sc.png design.
 * Shows bank logos + bank names and color-coded amounts. Amount is last.
 */
public class DashboardPanel extends JPanel {
    private JLabel totalBalanceLabel;
    private JLabel userNameLabel;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private SavingsTrackerNiAres mainFrame;

    public DashboardPanel(SavingsTrackerNiAres mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(UIUtils.createPadding(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);

        // --- LEFT COLUMN ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.4; gbc.weighty = 1.0;
        add(createLeftColumn(), gbc);

        // --- RIGHT COLUMN ---
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.6; gbc.weighty = 1.0;
        add(createRightColumn(), gbc);

        refreshStats();
    }

    private JPanel createLeftColumn() {
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("My Account & Balance");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(title);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Total Balance Card
        JPanel balanceCard = UIUtils.createRoundedPanel(20, UIUtils.COLOR_CARD_BALANCE);
        balanceCard.setLayout(new BorderLayout());
        balanceCard.setPreferredSize(new Dimension(0, 180));
        balanceCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        balanceCard.setBorder(UIUtils.createPadding(20, 25, 20, 25));

        JLabel balanceTitle = new JLabel("Total Balance");
        balanceTitle.setForeground(UIUtils.COLOR_TEXT_WHITE);
        balanceTitle.setFont(UIUtils.FONT_REGULAR);
        balanceCard.add(balanceTitle, BorderLayout.NORTH);

        totalBalanceLabel = new JLabel("0.00");
        totalBalanceLabel.setForeground(UIUtils.COLOR_TEXT_WHITE);
        totalBalanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        balanceCard.add(totalBalanceLabel, BorderLayout.CENTER);

        JLabel accNumber = new JLabel("**** 123-456-7890");
        accNumber.setForeground(UIUtils.COLOR_TEXT_WHITE);
        accNumber.setFont(UIUtils.FONT_SUBTITLE);
        balanceCard.add(accNumber, BorderLayout.SOUTH);

        leftPanel.add(balanceCard);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Profile Info Card
        JPanel profileCard = UIUtils.createRoundedPanel(20, UIUtils.COLOR_CARD_PROFILE);
        profileCard.setLayout(new BorderLayout());
        profileCard.setPreferredSize(new Dimension(0, 180));
        profileCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        profileCard.setBorder(UIUtils.createPadding(20, 25, 20, 25));

        JLabel profileTitle = new JLabel("Profile Info");
        profileTitle.setForeground(UIUtils.COLOR_TEXT_WHITE);
        profileTitle.setFont(UIUtils.FONT_REGULAR);
        profileCard.add(profileTitle, BorderLayout.NORTH);

        userNameLabel = new JLabel("User Name");
        userNameLabel.setForeground(UIUtils.COLOR_TEXT_WHITE);
        userNameLabel.setFont(UIUtils.FONT_TITLE);
        profileCard.add(userNameLabel, BorderLayout.CENTER);

        JLabel statusLabel = new JLabel("Activated - Silver Card");
        statusLabel.setForeground(UIUtils.COLOR_TEXT_WHITE);
        statusLabel.setFont(UIUtils.FONT_SUBTITLE);
        profileCard.add(statusLabel, BorderLayout.SOUTH);

        leftPanel.add(profileCard);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Profile Button
        JButton profileBtn = UIUtils.createStyledButton("Profile", UIUtils.COLOR_BUTTON_TEAL, UIUtils.COLOR_TEXT_WHITE);
        profileBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        profileBtn.addActionListener(e -> mainFrame.switchCard("Profile"));
        leftPanel.add(profileBtn);

        return leftPanel;
    }

    private JPanel createRightColumn() {
        JPanel rightPanel = UIUtils.createRoundedPanel(20, Color.WHITE);
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBorder(UIUtils.createPadding(25, 25, 25, 25));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Recent Transactions");
        title.setFont(UIUtils.FONT_TITLE);
        header.add(title, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);
        JButton addBtn = UIUtils.createStyledButton("Add", UIUtils.COLOR_BUTTON_TEAL, Color.WHITE);
        addBtn.addActionListener(e -> mainFrame.switchCard("Add Records"));
        JButton editBtn = UIUtils.createStyledButton("Edit", UIUtils.COLOR_BUTTON_TEAL, Color.WHITE);
        editBtn.addActionListener(e -> mainFrame.switchCard("Edit Transactions"));
        btnPanel.add(addBtn); btnPanel.add(editBtn);
        header.add(btnPanel, BorderLayout.EAST);
        rightPanel.add(header, BorderLayout.NORTH);

        // Table
        String[] columns = {"Bank", "Wallet", "Date", "Amount"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        transactionTable = new JTable(tableModel);
        transactionTable.setRowHeight(60);
        transactionTable.setFont(UIUtils.FONT_REGULAR);
        transactionTable.setGridColor(new Color(245, 245, 245));
        transactionTable.setShowVerticalLines(false);

        JTableHeader tableHeader = transactionTable.getTableHeader();
        tableHeader.setBackground(UIUtils.COLOR_ACCENT);
        tableHeader.setForeground(UIUtils.COLOR_SIDEBAR);
        tableHeader.setFont(UIUtils.FONT_BOLD);
        tableHeader.setPreferredSize(new Dimension(0, 40));

        // Bank Renderer (Logo + Name)
        transactionTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
                p.setOpaque(true);
                p.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                if (value instanceof Object[]) {
                    Object[] data = (Object[]) value;
                    if (data[0] instanceof ImageIcon) p.add(new JLabel((ImageIcon) data[0]));
                    JLabel name = new JLabel(data[1].toString());
                    name.setFont(UIUtils.FONT_BOLD);
                    p.add(name);
                } else {
                    p.add(new JLabel(value.toString()));
                }
                return p;
            }
        });

        // Amount Renderer (Last Column)
        transactionTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.RIGHT);
                label.setFont(UIUtils.FONT_BOLD);
                try {
                    double amount = Double.parseDouble(value.toString());
                    if (amount >= 0) {
                        label.setForeground(new Color(76, 175, 80));
                        label.setText("+" + String.format("%.2f", amount));
                    } else {
                        label.setForeground(new Color(244, 67, 54));
                        label.setText(String.format("%.2f", amount));
                    }
                } catch (Exception e) {}
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        JButton viewAllBtn = new JButton("Show All My Transactions");
        viewAllBtn.setFont(UIUtils.FONT_BOLD);
        viewAllBtn.setForeground(UIUtils.COLOR_BUTTON_TEAL);
        viewAllBtn.setBorderPainted(false);
        viewAllBtn.setContentAreaFilled(false);
        viewAllBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewAllBtn.addActionListener(e -> mainFrame.switchCard("Transaction"));
        
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        footer.add(viewAllBtn);
        rightPanel.add(footer, BorderLayout.SOUTH);

        return rightPanel;
    }

    public void refreshStats() {
        SavingsTrackerSystem system = SavingsTrackerSystem.getInstance();
        User user = system.getCurrentUser();
        double total = system.computeTotalAssets();

        userNameLabel.setText(user.getName());
        totalBalanceLabel.setText(String.format("%.2f", total));

        tableModel.setRowCount(0);
        java.util.List<SavingsTrackerSystem.TransactionRecord> all = system.getAllTransactionsFlat();
        int start = Math.max(0, all.size() - 10);
        for (int i = all.size() - 1; i >= start; i--) {
            SavingsTrackerSystem.TransactionRecord rec = all.get(i);
            ImageIcon icon = null;
            try {
                File imgFile = new File(rec.bank.getLogoPath());
                if (imgFile.exists()) {
                    ImageIcon original = new ImageIcon(rec.bank.getLogoPath());
                    Image img = original.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(img);
                }
            } catch (Exception e) {}

            tableModel.addRow(new Object[]{
                    new Object[]{icon, rec.bank.getBankName()},
                    rec.wallet.getWalletName(),
                    rec.transaction.getDate(),
                    rec.transaction.getAmount()
            });
        }
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) refreshStats();
    }
}
