package panels;

import app.UIUtils;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * Redesigned Panel to Edit or Delete existing savings records.
 * Bank is now editable. Amount is the last column with 2 decimal places.
 */
public class EditSavingsPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField descField, amountField, dateField;
    private JComboBox<BankAccount> bankBox;
    private JComboBox<Wallet> walletBox;
    private SavingsTrackerSystem.TransactionRecord selectedRecord;

    public EditSavingsPanel() {
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(UIUtils.createPadding(30, 30, 30, 30));

        // Container Card
        JPanel card = UIUtils.createRoundedPanel(20, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(UIUtils.createPadding(30, 30, 30, 30));
        add(card, BorderLayout.CENTER);

        // Title
        JLabel title = new JLabel("Edit / Delete Transactions");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_DARK);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        card.add(title, BorderLayout.NORTH);

        // --- Table View ---
        String[] columns = {"Bank", "Wallet", "Description", "Date", "Amount"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setFont(UIUtils.FONT_REGULAR);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(UIUtils.COLOR_ACCENT);
        tableHeader.setForeground(UIUtils.COLOR_SIDEBAR);
        tableHeader.setFont(UIUtils.FONT_BOLD);
        tableHeader.setPreferredSize(new Dimension(0, 40));

        // Right-align Amount column (last column) with 2 decimal places
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
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

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) populateFields(row);
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        card.add(scrollPane, BorderLayout.CENTER);

        // --- Edit Form ---
        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.setOpaque(false);
        bottomContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Using GridBagLayout for better control over the form alignment
        JPanel editForm = new JPanel(new GridBagLayout());
        editForm.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        bankBox = new JComboBox<>();
        bankBox.setBackground(Color.WHITE);
        bankBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof BankAccount) setText(((BankAccount) value).getBankName());
                return this;
            }
        });
        bankBox.addActionListener(e -> updateWalletBox());

        walletBox = new JComboBox<>();
        walletBox.setBackground(Color.WHITE);
        walletBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Wallet) setText(((Wallet) value).getWalletName());
                return this;
            }
        });

        descField = createStyledTextField();
        amountField = createStyledTextField();
        dateField = createStyledTextField();

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        editForm.add(createLabel("Bank:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.4;
        editForm.add(bankBox, gbc);
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.1;
        editForm.add(createLabel("Wallet:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.4;
        editForm.add(walletBox, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        editForm.add(createLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.4;
        editForm.add(descField, gbc);
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.1;
        editForm.add(createLabel("Date:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.4;
        editForm.add(dateField, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.1;
        editForm.add(createLabel("Amount:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.4;
        editForm.add(amountField, gbc);

        bottomContainer.add(editForm, BorderLayout.CENTER);

        // Buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        actionPanel.setOpaque(false);

        JButton deleteBtn = UIUtils.createStyledButton("Delete", new Color(244, 67, 54), Color.WHITE);
        deleteBtn.addActionListener(e -> deleteTransaction());

        JButton updateBtn = UIUtils.createStyledButton("Update", UIUtils.COLOR_BUTTON_TEAL, Color.WHITE);
        updateBtn.addActionListener(e -> updateTransaction());

        actionPanel.add(deleteBtn);
        actionPanel.add(updateBtn);
        bottomContainer.add(actionPanel, BorderLayout.SOUTH);

        card.add(bottomContainer, BorderLayout.SOUTH);

        refreshTable();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIUtils.FONT_BOLD);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(UIUtils.FONT_REGULAR);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private void updateWalletBox() {
        walletBox.removeAllItems();
        BankAccount selectedBank = (BankAccount) bankBox.getSelectedItem();
        if (selectedBank != null) {
            for (Wallet w : selectedBank.getWallets()) {
                walletBox.addItem(w);
            }
        }
    }

    private void populateFields(int row) {
        List<SavingsTrackerSystem.TransactionRecord> all = SavingsTrackerSystem.getInstance().getAllTransactionsFlat();
        if (row < all.size()) {
            selectedRecord = all.get(row);
            
            // Set Bank
            for (int i = 0; i < bankBox.getItemCount(); i++) {
                if (bankBox.getItemAt(i).getBankName().equals(selectedRecord.bank.getBankName())) {
                    bankBox.setSelectedIndex(i);
                    break;
                }
            }
            updateWalletBox();
            
            // Set Wallet
            for (int i = 0; i < walletBox.getItemCount(); i++) {
                if (walletBox.getItemAt(i).getWalletName().equals(selectedRecord.wallet.getWalletName())) {
                    walletBox.setSelectedIndex(i);
                    break;
                }
            }

            descField.setText(selectedRecord.transaction.getDescription());
            amountField.setText(String.format("%.2f", selectedRecord.transaction.getAmount()));
            dateField.setText(selectedRecord.transaction.getDate());
        }
    }

    private void updateTransaction() {
        if (selectedRecord == null) return;
        try {
            BankAccount newBank = (BankAccount) bankBox.getSelectedItem();
            Wallet newWallet = (Wallet) walletBox.getSelectedItem();

            if (newBank == null || newWallet == null) {
                JOptionPane.showMessageDialog(this, "Please select a bank and wallet.");
                return;
            }

            // Update Transaction properties
            selectedRecord.transaction.setDescription(descField.getText());
            selectedRecord.transaction.setAmount(Double.parseDouble(amountField.getText()));
            selectedRecord.transaction.setDate(dateField.getText());

            // If bank or wallet changed, move the transaction
            if (newWallet != selectedRecord.wallet) {
                selectedRecord.wallet.getTransactions().remove(selectedRecord.transaction);
                newWallet.addTransaction(selectedRecord.transaction);
            }

            SavingsTrackerSystem.getInstance().saveData();
            JOptionPane.showMessageDialog(this, "Updated successfully!");
            refreshTable();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount.");
        }
    }

    private void deleteTransaction() {
        if (selectedRecord == null) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this record?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            selectedRecord.wallet.getTransactions().remove(selectedRecord.transaction);
            selectedRecord = null;
            SavingsTrackerSystem.getInstance().saveData();
            refreshTable();
            clearFields();
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        bankBox.removeAllItems();
        List<BankAccount> banks = SavingsTrackerSystem.getInstance().getCurrentUser().getBankAccounts();
        for (BankAccount b : banks) bankBox.addItem(b);

        for (SavingsTrackerSystem.TransactionRecord rec : SavingsTrackerSystem.getInstance().getAllTransactionsFlat()) {
            tableModel.addRow(new Object[]{
                    rec.bank.getBankName(), rec.wallet.getWalletName(),
                    rec.transaction.getDescription(), rec.transaction.getDate(),
                    rec.transaction.getAmount()
            });
        }
    }

    private void clearFields() {
        descField.setText("");
        amountField.setText("");
        dateField.setText("");
        selectedRecord = null;
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) refreshTable();
    }
}
