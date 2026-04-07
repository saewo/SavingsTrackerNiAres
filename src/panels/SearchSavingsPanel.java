package panels;

import model.SavingsDataStore;
import model.SavingsRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panel to search for specific savings records.
 */
public class SearchSavingsPanel extends JPanel {
    private JTextField searchField;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    public SearchSavingsPanel() {
        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Search Savings Records", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Search Bar
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchBar.add(new JLabel("Search (Desc/Category):"));
        searchField = new JTextField(20);
        searchBar.add(searchField);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> performSearch());
        searchBar.add(searchBtn);
        add(searchBar, BorderLayout.NORTH);

        // Result Table
        String[] columns = {"ID", "Description", "Amount ($)", "Date", "Category"};
        tableModel = new DefaultTableModel(columns, 0);
        resultTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        add(scrollPane, BorderLayout.CENTER);

        // Layout fix
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(searchBar, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);
    }

    private void performSearch() {
        String query = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);

        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean found = false;
        for (SavingsRecord record : SavingsDataStore.getInstance().getAllRecords()) {
            if (record.getDescription().toLowerCase().contains(query) ||
                record.getCategory().toLowerCase().contains(query) ||
                record.getId().toLowerCase().contains(query)) {
                tableModel.addRow(record.toTableRow());
                found = true;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(this, "No records found matching: " + query, "No Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}