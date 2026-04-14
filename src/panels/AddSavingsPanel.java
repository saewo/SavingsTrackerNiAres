package panels;

import app.UIUtils;
import model.SavingsTrackerSystem;
import model.Transaction;
import model.BankAccount;
import model.Wallet;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Redesigned Panel for adding a new savings record.
 * Uses positive/negative numbers for income/expense.
 */
public class AddSavingsPanel extends JPanel {
    private JTextField descriptionField;
    private JTextField amountField;
    private JTextField dateField;
    private JComboBox<BankAccount> bankComboBox;
    private JComboBox<Wallet> walletComboBox;
    private ProfilePanel profilePanel;

    public AddSavingsPanel(ProfilePanel profilePanel) {
        this.profilePanel = profilePanel;
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(UIUtils.createPadding(30, 30, 30, 30));

        // Container Card
        JPanel card = UIUtils.createRoundedPanel(20, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(UIUtils.createPadding(30, 40, 30, 40));
        add(card, BorderLayout.CENTER);

        // Title
        JLabel title = new JLabel("Add New Transaction");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_DARK);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        card.add(title, BorderLayout.NORTH);

        JLabel hint = new JLabel("(Note: Use negative values for expenses, e.g., -500)");
        hint.setFont(UIUtils.FONT_SUBTITLE);
        hint.setForeground(UIUtils.COLOR_TEXT_MUTED);
        hint.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(hint, BorderLayout.SOUTH);
        card.add(headerPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // --- Bank selection ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(createLabel("Bank:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        bankComboBox = new JComboBox<>();
        styleComboBox(bankComboBox);
        bankComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof BankAccount) setText(((BankAccount) value).getBankName());
                return this;
            }
        });
        bankComboBox.addActionListener(e -> updateWallets());
        formPanel.add(bankComboBox, gbc);

        // --- Wallet selection ---
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(createLabel("Wallet:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        walletComboBox = new JComboBox<>();
        styleComboBox(walletComboBox);
        walletComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Wallet) setText(((Wallet) value).getWalletName());
                return this;
            }
        });
        formPanel.add(walletComboBox, gbc);

        // --- Description ---
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(createLabel("Description:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        descriptionField = createStyledTextField();
        formPanel.add(descriptionField, gbc);

        // --- Amount ---
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        formPanel.add(createLabel("Amount:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1.0;
        amountField = createStyledTextField();
        formPanel.add(amountField, gbc);

        // --- Date ---
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        formPanel.add(createLabel("Date (YYYY-MM-DD):"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 5; gbc.weightx = 1.0;
        dateField = createStyledTextField();
        dateField.setText(java.time.LocalDate.now().toString());
        formPanel.add(dateField, gbc);

        card.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton clearBtn = UIUtils.createStyledButton("Clear", Color.LIGHT_GRAY, UIUtils.COLOR_TEXT_DARK);
        clearBtn.addActionListener(e -> clearFields());
        buttonPanel.add(clearBtn);

        JButton addBtn = UIUtils.createStyledButton("Add Record", UIUtils.COLOR_BUTTON_TEAL, Color.WHITE);
        addBtn.addActionListener(e -> addRecord());
        buttonPanel.add(addBtn);

        card.add(buttonPanel, BorderLayout.SOUTH);
        
        refreshData();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIUtils.FONT_BOLD);
        label.setForeground(UIUtils.COLOR_TEXT_DARK);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(UIUtils.FONT_REGULAR);
        field.setPreferredSize(new Dimension(0, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        return field;
    }

    private void styleComboBox(JComboBox<?> box) {
        box.setFont(UIUtils.FONT_REGULAR);
        box.setPreferredSize(new Dimension(0, 40));
        box.setBackground(Color.WHITE);
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
        if (aFlag) refreshData();
    }

    private void addRecord() {
        Wallet selectedWallet = (Wallet) walletComboBox.getSelectedItem();
        if (selectedWallet == null) {
            JOptionPane.showMessageDialog(this, "Please select a bank and wallet.");
            return;
        }

        String description = descriptionField.getText().trim();
        String amountText = amountField.getText().trim();
        String date = dateField.getText().trim();

        if (description.isEmpty() || amountText.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            selectedWallet.addTransaction(new Transaction(amount, description, date));
            if (profilePanel != null) profilePanel.refreshBanksList();
            
            JOptionPane.showMessageDialog(this, "Transaction added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Amount must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        descriptionField.setText("");
        amountField.setText("");
        dateField.setText(java.time.LocalDate.now().toString());
    }
}
