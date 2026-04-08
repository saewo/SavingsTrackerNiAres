package panels;

import model.SavingsTrackerSystem;
import model.Transaction;
import model.BankAccount;
import model.Wallet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel for adding a new savings record.
 */
public class AddSavingsPanel extends JPanel{
    private JTextField descriptionField;
    private JTextField amountField;
    private JTextField dateField;
    private JComboBox<String> typeComboBox;
    private JComboBox<BankAccount> bankComboBox;
    private JComboBox<Wallet> walletComboBox;
    private ProfilePanel profilePanel;
    public AddSavingsPanel(ProfilePanel profilePanel) {
        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Add New Transaction", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Bank selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Bank:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        bankComboBox = new JComboBox<>();
        bankComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof BankAccount) {
                    setText(((BankAccount) value).getBankName());
                }
                return this;
            }
        });
        bankComboBox.addActionListener(e -> updateWallets());
        formPanel.add(bankComboBox, gbc);

        // Wallet selection
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Wallet:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        walletComboBox = new JComboBox<>();
        walletComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Wallet) {
                    setText(((Wallet) value).getWalletName());
                }
                return this;
            }
        });
        formPanel.add(walletComboBox, gbc);

        // Type
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        typeComboBox = new JComboBox<>(new String[]{"Income", "Expense"});
        formPanel.add(typeComboBox, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        descriptionField = new JTextField(20);
        formPanel.add(descriptionField, gbc);

        // Amount
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Amount ($):"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        amountField = new JTextField(20);
        formPanel.add(amountField, gbc);

        // Date
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        dateField = new JTextField(20);
        dateField.setText("2026-04-07"); // Default to today
        formPanel.add(dateField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JButton addBtn = new JButton("Add Record");
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRecord();
                profilePanel.refreshBanksList();
            }
        });
        buttonPanel.add(addBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> clearFields());
        buttonPanel.add(clearBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // QoL
        descriptionField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                amountField.requestFocus();
            }
        });

        amountField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                descriptionField.requestFocus();
                addBtn.doClick();
            }
        });
        
        refreshData();
    }

    private void updateWallets() {
        walletComboBox.removeAllItems();
        BankAccount selectedBank = (BankAccount) bankComboBox.getSelectedItem();
        if (selectedBank != null) {
            for (Wallet w : selectedBank.getWallets()) {
                walletComboBox.addItem(w);
            }
        }
    }

    private void refreshData() {
        bankComboBox.removeAllItems();
        List<BankAccount> banks = SavingsTrackerSystem.getInstance().getCurrentUser().getBankAccounts();
        for (BankAccount b : banks) {
            bankComboBox.addItem(b);
        }
        updateWallets();
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            refreshData();
        }
    }

    private void addRecord() {
        Wallet selectedWallet = (Wallet) walletComboBox.getSelectedItem();
        if (selectedWallet == null) {
            JOptionPane.showMessageDialog(this, "Please select a bank and wallet.");
            return;
        }

        String type = (String) typeComboBox.getSelectedItem();
        String description = descriptionField.getText().trim();
        String amountText = amountField.getText().trim();
        String date = dateField.getText().trim();

        if (description.isEmpty() || amountText.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Amount must be a valid number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Transaction transaction = new Transaction(type, amount, description, date);

        if (type .equals("Expense")) {
            if (amount > selectedWallet.getBalance()) {
                JOptionPane.showMessageDialog(this,
                        "You do not have enough balance for this transaction.", "Error", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                return;
            }
        }

        selectedWallet.addTransaction(transaction);
        if (profilePanel != null) {
            profilePanel.refreshBanksList();
        }

        JOptionPane.showMessageDialog(this,
                "Transaction added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        clearFields();

    }

    private void clearFields() {
        descriptionField.setText("");
        amountField.setText("");
        dateField.setText("2026-04-07");
        typeComboBox.setSelectedIndex(0);
    }
}