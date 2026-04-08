package panels;

import model.SavingsTrackerSystem;
import model.BankAccount;
import model.Wallet;
import model.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class ViewSavingsPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public ViewSavingsPanel() {
        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("All Transactions", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Table setup - 6 Columns total
        String[] columns = {"Bank", "Wallet", "Description", "Amount ($)", "Date", "Type"};
        tableModel = new DefaultTableModel(columns, 0);

        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                if (!isRowSelected(row)) {
                    // FIXED: Use else-if so colors don't overwrite each other
                    if (row == 0) {
                        c.setBackground(new Color(173, 216, 230)); // Light Blue
                    } else if (row % 2 == 0) {
                        c.setBackground(Color.GREEN);
                    } else {
                        c.setBackground(Color.RED);
                    }
                }
                return c;
            }
        };

        table.setRowSorter(new TableRowSorter<>(tableModel));
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Refresh button
        JButton refreshBtn = new JButton("Refresh Table");
        refreshBtn.addActionListener(e -> refreshTable());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        SavingsTrackerSystem system = SavingsTrackerSystem.getInstance();

        if (system.getCurrentUser() != null) {
            for (BankAccount bank : system.getCurrentUser().getBankAccounts()) {
                for (Wallet wallet : bank.getWallets()) {
                    for (Transaction t : wallet.getTransactions()) {
                        // FIXED: Passing TWO arguments here
                        tableModel.addRow(t.toTableRow(bank.getBankName(), wallet.getWalletName()));
                    }
                }
            }
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