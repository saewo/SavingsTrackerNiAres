package panels;

import model.SavingsTrackerSystem;
import model.BankAccount;
import model.Wallet;
import model.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panel to search for specific savings records including Bank details.
 */
public class SearchSavingsPanel extends JPanel {
    private JTextField searchField;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    public SearchSavingsPanel() {
        setLayout(new BorderLayout());

        // --- Header Section ---
        JPanel headerPanel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Search Transactions", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        headerPanel.add(title, BorderLayout.NORTH);

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchBar.add(new JLabel("Search Bank:"));
        searchField = new JTextField(25);

        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> performSearch());

        JButton clearBtn = new JButton("Show All");
        clearBtn.addActionListener(e -> {
            searchField.setText("");
            performSearch();
        });

        searchBar.add(searchField);
        searchBar.add(searchBtn);
        searchBar.add(clearBtn);
        headerPanel.add(searchBar, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        // --- Table Section ---
        // Added "Bank" to the columns array
        String[] columns = {"Bank", "Wallet", "Description", "Amount ($)", "Date", "Type"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultTable = new JTable(tableModel);
        resultTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void performSearch() {
        String query = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);

        SavingsTrackerSystem system = SavingsTrackerSystem.getInstance();
        boolean found = false;
        for (BankAccount bank : system.getCurrentUser().getBankAccounts()) {
            for (Wallet wallet : bank.getWallets()) {
                for (Transaction t : wallet.getTransactions()) {

                    boolean matchesDesc = t.getDescription().toLowerCase().contains(query);
                    boolean matchesType = t.getType().toLowerCase().contains(query);
                    boolean matchesBank = bank.getBankName().toLowerCase().contains(query);
                    boolean Wallet = wallet.getWalletName().toLowerCase().contains(query);

                    if (query.isEmpty() || matchesDesc || matchesType || matchesBank || Wallet) {
                        // Manually constructing the row to include bank.getBankName()
                        tableModel.addRow(new Object[]{
                                bank.getBankName(),
                                wallet.getWalletName(),
                                t.getDescription(),
                                t.getAmount(),
                                t.getDate(),
                                t.getType()
                        });
                        found = true;
                    }
                }
            }
        }
        if (!found && !query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No matching records found.");
        }
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            performSearch();
        }
    }
}