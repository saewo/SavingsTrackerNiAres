package panels;

import model.SavingsDataStore;
import model.SavingsRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panel to edit or delete existing savings records.
 */
public class EditSavingsPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public EditSavingsPanel() {
        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Edit / Delete Records", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"ID", "Description", "Amount ($)", "Date", "Category"};
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
        for (SavingsRecord record : SavingsDataStore.getInstance().getAllRecords()) {
            tableModel.addRow(record.toTableRow());
        }
    }

    private void editRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a record to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SavingsRecord record = SavingsDataStore.getInstance().getAllRecords().get(selectedRow);

        String newDesc = JOptionPane.showInputDialog(this, "New Description:", record.getDescription());
        if (newDesc == null) return;

        String newAmountStr = JOptionPane.showInputDialog(this, "New Amount ($):", record.getAmount());
        if (newAmountStr == null) return;

        double newAmount;
        try {
            newAmount = Double.parseDouble(newAmountStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newDate = JOptionPane.showInputDialog(this, "New Date (YYYY-MM-DD):", record.getDate());
        if (newDate == null) return;

        String newCat = JOptionPane.showInputDialog(this, "New Category:", record.getCategory());
        if (newCat == null) return;

        record.setDescription(newDesc);
        record.setAmount(newAmount);
        record.setDate(newDate);
        record.setCategory(newCat);

        JOptionPane.showMessageDialog(this, "Record updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        refreshTable();
    }

    private void deleteRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a record to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            SavingsDataStore.getInstance().removeRecord(selectedRow);
            JOptionPane.showMessageDialog(this, "Record deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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