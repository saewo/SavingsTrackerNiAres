package panels;

import model.SavingsTrackerSystem;
import model.BankAccount;
import model.Wallet;
import model.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;

/**
 * Panel to edit or delete existing savings records.
 */
public class EditSavingsPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<SavingsTrackerSystem.TransactionRecord> rowMapping;

    public EditSavingsPanel() {
        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Edit / Delete Transactions", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"Wallet", "Description", "Amount ($)", "Date", "Type"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton editBtn = new JButton("Edit Selected");
        editBtn.addActionListener(e -> editRecord());
        buttonPanel.add(editBtn);

        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.addActionListener(e -> deleteRecord());
        buttonPanel.add(deleteBtn);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshTable());
        buttonPanel.add(refreshBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Initial load
        refreshTable();
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        rowMapping = new ArrayList<>();
        SavingsTrackerSystem system = SavingsTrackerSystem.getInstance();
        
        for (BankAccount bank : system.getCurrentUser().getBankAccounts()) {
            for (Wallet wallet : bank.getWallets()) {
                for (Transaction t : wallet.getTransactions()) {
                    tableModel.addRow(t.toTableRow(wallet.getWalletName()));
                    rowMapping.add(new SavingsTrackerSystem.TransactionRecord(bank, wallet, t));
                }
            }
        }
    }

    private void editRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a record to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SavingsTrackerSystem.TransactionRecord record = rowMapping.get(selectedRow);
        Transaction t = record.transaction;

        String newDesc = JOptionPane.showInputDialog(this, "New Description:", t.getDescription());
        if (newDesc == null) return;

        String newAmountStr = JOptionPane.showInputDialog(this, "New Amount ($):", t.getAmount());
        if (newAmountStr == null) return;

        double newAmount;
        try {
            newAmount = Double.parseDouble(newAmountStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newDate = JOptionPane.showInputDialog(this, "New Date (YYYY-MM-DD):", t.getDate());
        if (newDate == null) return;

        String[] types = {"Income", "Expense"};
        String newType = (String) JOptionPane.showInputDialog(this, "New Type:", "Edit Type", 
                JOptionPane.QUESTION_MESSAGE, null, types, t.getType());
        if (newType == null) return;

        // Update balance of the wallet before updating the transaction (simple way)
        // Adjust for old transaction
        if (t.getType().equalsIgnoreCase("Expense")) {
            record.wallet.setBalance(record.wallet.getBalance() + t.getAmount());
        } else {
            record.wallet.setBalance(record.wallet.getBalance() - t.getAmount());
        }

        // Apply new values
        t.setDescription(newDesc);
        t.setAmount(newAmount);
        t.setDate(newDate);
        t.setType(newType);

        // Adjust for new transaction
        if (t.getType().equalsIgnoreCase("Expense")) {
            record.wallet.setBalance(record.wallet.getBalance() - t.getAmount());
        } else {
            record.wallet.setBalance(record.wallet.getBalance() + t.getAmount());
        }

        JOptionPane.showMessageDialog(this, "Transaction updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        refreshTable();
    }

    private void deleteRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a record to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected transaction?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            SavingsTrackerSystem.TransactionRecord record = rowMapping.get(selectedRow);
            Transaction t = record.transaction;
            
            // Adjust balance
            if (t.getType().equalsIgnoreCase("Expense")) {
                record.wallet.setBalance(record.wallet.getBalance() + t.getAmount());
            } else {
                record.wallet.setBalance(record.wallet.getBalance() - t.getAmount());
            }
            
            record.wallet.getTransactions().remove(t);
            JOptionPane.showMessageDialog(this, "Transaction deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        }
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            refreshTable();
        }
    }
}