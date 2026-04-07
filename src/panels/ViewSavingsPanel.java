package panels;

import model.SavingsDataStore;
import model.SavingsRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panel to view all savings records in a table.
 */
public class ViewSavingsPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public ViewSavingsPanel() {
        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Savings History", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"ID", "Description", "Amount ($)", "Date", "Category"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Refresh button
        JButton refreshBtn = new JButton("Refresh Table");
        refreshBtn.addActionListener(e -> refreshTable());
        JPanel buttonPanel = new JPanel();
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

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            refreshTable();
        }
    }
}