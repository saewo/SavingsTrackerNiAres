package panels;

import model.SavingsDataStore;
import model.SavingsRecord;

import javax.swing.*;
import java.awt.*;

/**
 * Panel for adding a new savings record.
 */
public class AddSavingsPanel extends JPanel {
    private JTextField idField;
    private JTextField descriptionField;
    private JTextField amountField;
    private JTextField dateField;
    private JTextField categoryField;

    public AddSavingsPanel() {
        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Add New Savings Record", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Transaction ID:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        idField = new JTextField(20);
        formPanel.add(idField, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        descriptionField = new JTextField(20);
        formPanel.add(descriptionField, gbc);

        // Amount
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Amount ($):"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        amountField = new JTextField(20);
        formPanel.add(amountField, gbc);

        // Date
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        dateField = new JTextField(20);
        formPanel.add(dateField, gbc);

        // Category
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        categoryField = new JTextField(20);
        formPanel.add(categoryField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JButton addBtn = new JButton("Add Record");
        addBtn.addActionListener(e -> addRecord());
        buttonPanel.add(addBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> clearFields());
        buttonPanel.add(clearBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addRecord() {
        String id = idField.getText().trim();
        String description = descriptionField.getText().trim();
        String amountText = amountField.getText().trim();
        String date = dateField.getText().trim();
        String category = categoryField.getText().trim();

        if (id.isEmpty() || description.isEmpty() || amountText.isEmpty() || date.isEmpty() || category.isEmpty()) {
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

        if (SavingsDataStore.getInstance().findById(id) != null) {
            JOptionPane.showMessageDialog(this,
                    "A record with this ID already exists.", "Duplicate ID", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SavingsDataStore.getInstance().addRecord(new SavingsRecord(id, description, amount, date, category));
        JOptionPane.showMessageDialog(this,
                "Record added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        clearFields();
    }

    private void clearFields() {
        idField.setText("");
        descriptionField.setText("");
        amountField.setText("");
        dateField.setText("");
        categoryField.setText("");
    }
}