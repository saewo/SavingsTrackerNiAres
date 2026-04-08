package panels;

import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panel to Edit or Delete existing savings records.
 */
public class EditSavingsPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField descField, amountField, dateField;
    private JComboBox<String> typeBox;
    private SavingsTrackerSystem.TransactionRecord selectedRecord;

    public EditSavingsPanel() {
        setLayout(new BorderLayout());

        // --- Top: Table View ---
        String[] columns = {"Bank", "Wallet", "Description", "Amount", "Date", "Type"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Listen for row selection to populate edit fields
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                populateFields(row);
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Bottom: Edit Form ---
        JPanel editForm = new JPanel(new GridLayout(3, 4, 10, 10));
        editForm.setBorder(BorderFactory.createTitledBorder("Edit Selected Transaction"));

        descField = new JTextField();
        amountField = new JTextField();
        dateField = new JTextField();
        typeBox = new JComboBox<>(new String[]{"Income", "Expense"});

        editForm.add(new JLabel("Description:"));
        editForm.add(descField);
        editForm.add(new JLabel("Amount:"));
        editForm.add(amountField);
        editForm.add(new JLabel("Date:"));
        editForm.add(dateField);
        editForm.add(new JLabel("Type:"));
        editForm.add(typeBox);

        // Buttons
        JButton updateBtn = new JButton("Update");
        updateBtn.addActionListener(e -> updateTransaction());

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBackground(new Color(255, 100, 100));
        deleteBtn.addActionListener(e -> deleteTransaction());

        JPanel actionPanel = new JPanel();
        actionPanel.add(updateBtn);
        actionPanel.add(deleteBtn);

        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.add(editForm, BorderLayout.CENTER);
        bottomContainer.add(actionPanel, BorderLayout.SOUTH);
        add(bottomContainer, BorderLayout.SOUTH);
    }

    private void populateFields(int row) {
        // We use the helper method from SavingsTrackerSystem to find the actual objects
        java.util.List<SavingsTrackerSystem.TransactionRecord> all =
                SavingsTrackerSystem.getInstance().getAllTransactionsFlat();

        selectedRecord = all.get(row);
        descField.setText(selectedRecord.transaction.getDescription());
        amountField.setText(String.valueOf(selectedRecord.transaction.getAmount()));
        dateField.setText(selectedRecord.transaction.getDate());
        typeBox.setSelectedItem(selectedRecord.transaction.getType());
    }

    private void updateTransaction() {
        if (selectedRecord == null) return;

        try {
            selectedRecord.transaction.setDescription(descField.getText());
            selectedRecord.transaction.setAmount(Double.parseDouble(amountField.getText()));
            selectedRecord.transaction.setDate(dateField.getText());
            selectedRecord.transaction.setType((String) typeBox.getSelectedItem());

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
            refreshTable();
            clearFields();
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
<<<<<<< HEAD
        rowMapping = new ArrayList<>();
        SavingsTrackerSystem system = SavingsTrackerSystem.getInstance();
        
        for (BankAccount bank : system.getCurrentUser().getBankAccounts()) {
            for (Wallet wallet : bank.getWallets()) {
                for (Transaction t : wallet.getTransactions()) {
                    tableModel.addRow(t.toTableRow(wallet.getWalletName(), wallet.getWalletName()));
                    rowMapping.add(new SavingsTrackerSystem.TransactionRecord(bank, wallet, t));
                }
            }
=======
        for (SavingsTrackerSystem.TransactionRecord rec : SavingsTrackerSystem.getInstance().getAllTransactionsFlat()) {
            tableModel.addRow(new Object[]{
                    rec.bank.getBankName(),
                    rec.wallet.getWalletName(),
                    rec.transaction.getDescription(),
                    rec.transaction.getAmount(),
                    rec.transaction.getDate(),
                    rec.transaction.getType()
            });
>>>>>>> bdc1b8de28a85fb898f2d51432077fd71b2a94ee
        }
    }

    private void clearFields() {
        descField.setText("");
        amountField.setText("");
        dateField.setText("");
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) refreshTable();
    }
}