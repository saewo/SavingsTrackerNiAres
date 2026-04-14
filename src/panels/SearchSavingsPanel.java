package panels;

import app.UIUtils;
import model.SavingsTrackerSystem;
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
 * Redesigned Panel for searching savings records.
 * Shows Bank Logo + Name. Amount is last.
 */
public class SearchSavingsPanel extends JPanel {
    private JTextField searchField;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    public SearchSavingsPanel() {
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(UIUtils.createPadding(30, 30, 30, 30));

        // Container Card
        JPanel card = UIUtils.createRoundedPanel(20, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(UIUtils.createPadding(30, 30, 30, 30));
        add(card, BorderLayout.CENTER);

        // Header Panel
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("Search Records");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_DARK);
        header.add(title, BorderLayout.NORTH);

        JPanel searchBarPanel = new JPanel(new BorderLayout(15, 0));
        searchBarPanel.setOpaque(false);
        searchBarPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        searchField = new JTextField();
        searchField.setFont(UIUtils.FONT_REGULAR);
        searchField.setPreferredSize(new Dimension(0, 45));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(0, 15, 0, 15)
        ));
        searchBarPanel.add(searchField, BorderLayout.CENTER);

        JButton searchBtn = UIUtils.createStyledButton("Search", UIUtils.COLOR_BUTTON_TEAL, Color.WHITE);
        searchBtn.addActionListener(e -> performSearch());
        searchBarPanel.add(searchBtn, BorderLayout.EAST);

        header.add(searchBarPanel, BorderLayout.CENTER);
        card.add(header, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"Bank", "Wallet", "Description", "Date", "Amount ($)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        resultTable = new JTable(tableModel);
        resultTable.setRowHeight(60);
        resultTable.setFont(UIUtils.FONT_REGULAR);
        resultTable.setGridColor(new Color(245, 245, 245));
        resultTable.setShowVerticalLines(false);

        // Header Styling
        JTableHeader tableHeader = resultTable.getTableHeader();
        tableHeader.setBackground(UIUtils.COLOR_ACCENT);
        tableHeader.setForeground(UIUtils.COLOR_SIDEBAR);
        tableHeader.setFont(UIUtils.FONT_BOLD);
        tableHeader.setPreferredSize(new Dimension(0, 40));

        // Custom Cell Renderer for Bank (Logo + Name)
        resultTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
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

        // Custom Cell Renderer for Amount (Green/Red) - Last Column
        resultTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
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
                } catch (Exception e) {
                    label.setForeground(UIUtils.COLOR_TEXT_DARK);
                }
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        card.add(scrollPane, BorderLayout.CENTER);

        performSearch(); // Initial load
    }

    private void performSearch() {
        tableModel.setRowCount(0);
        String query = searchField.getText().toLowerCase().trim();
        SavingsTrackerSystem system = SavingsTrackerSystem.getInstance();

        for (SavingsTrackerSystem.TransactionRecord rec : system.getAllTransactionsFlat()) {
            if (query.isEmpty() || rec.transaction.getDescription().toLowerCase().contains(query) || 
                rec.wallet.getWalletName().toLowerCase().contains(query) ||
                rec.bank.getBankName().toLowerCase().contains(query)) {
                
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
                        rec.transaction.getDescription(),
                        rec.transaction.getDate(),
                        rec.transaction.getAmount()
                });
            }
        }
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) performSearch();
    }
}
