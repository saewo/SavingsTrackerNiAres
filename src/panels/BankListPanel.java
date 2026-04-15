package panels;

import app.UIUtils;
import model.SavingsTrackerSystem;
import model.BankAccount;
import model.Wallet;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * Panel to view all Banks and their Wallets.
 * Features double-click on logos to edit them, direct balance editing for wallets,
 * and strict case-insensitive duplicate bank prevention.
 */
public class BankListPanel extends JPanel {
    private JPanel listPanel;

    public BankListPanel() {
        setLayout(new BorderLayout());
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(UIUtils.createPadding(30, 30, 30, 30));

        // Container Card
        JPanel card = UIUtils.createRoundedPanel(20, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(UIUtils.createPadding(30, 40, 30, 40));
        add(card, BorderLayout.CENTER);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Bank Accounts");
        title.setFont(UIUtils.FONT_TITLE);
        header.add(title, BorderLayout.WEST);

        JButton addBankBtn = UIUtils.createStyledButton("Add New Bank", UIUtils.COLOR_BUTTON_TEAL, Color.WHITE);
        addBankBtn.addActionListener(e -> addNewBank());
        header.add(addBankBtn, BorderLayout.EAST);

        card.add(header, BorderLayout.NORTH);

        // List
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        card.add(scrollPane, BorderLayout.CENTER);

        refreshList();
    }

    public void refreshList() {
        listPanel.removeAll();
        for (BankAccount bank : SavingsTrackerSystem.getInstance().getCurrentUser().getBankAccounts()) {
            listPanel.add(createBankItem(bank));
            listPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createBankItem(BankAccount bank) {
        JPanel item = UIUtils.createRoundedPanel(15, new Color(245, 247, 248));
        item.setLayout(new BorderLayout(20, 0));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        item.setBorder(UIUtils.createPadding(15, 20, 15, 20));

        // Logo with Double-Click Listener
        JLabel logoLabel = new JLabel();
        logoLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoLabel.setToolTipText("Double-click to change logo");

        logoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editBankLogo(bank);
                }
            }
        });

        try {
            File f = new File(bank.getLogoPath());
            if (f.exists()) {
                ImageIcon icon = new ImageIcon(new ImageIcon(bank.getLogoPath()).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
                logoLabel.setIcon(icon);
            } else {
                logoLabel.setText("[No Logo]");
            }
        } catch (Exception e) {
            logoLabel.setText("[Error]");
        }
        item.add(logoLabel, BorderLayout.WEST);

        // Info
        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setOpaque(false);
        JLabel name = new JLabel(bank.getBankName());
        name.setFont(UIUtils.FONT_BOLD);
        JLabel balance = new JLabel(String.format("Total: Php%.2f", bank.getTotalBalance()));
        balance.setForeground(UIUtils.COLOR_BUTTON_TEAL);
        info.add(name);
        info.add(balance);
        item.add(info, BorderLayout.CENTER);

        // Actions Panel
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        actions.setOpaque(false);

        JButton addWalletBtn = UIUtils.createStyledButton("Add Wallet", UIUtils.COLOR_BUTTON_TEAL, Color.WHITE);
        addWalletBtn.setPreferredSize(new Dimension(110, 35));
        addWalletBtn.addActionListener(e -> addNewWallet(bank));

        JButton viewWalletsBtn = UIUtils.createStyledButton("View", UIUtils.COLOR_SIDEBAR, Color.WHITE);
        viewWalletsBtn.setPreferredSize(new Dimension(80, 35));
        viewWalletsBtn.addActionListener(e -> showWallets(bank));

        // DELETE BANK BUTTON
        JButton deleteBankBtn = UIUtils.createStyledButton("Delete", new Color(220, 53, 69), Color.WHITE);
        deleteBankBtn.setPreferredSize(new Dimension(80, 35));
        deleteBankBtn.addActionListener(e -> deleteBank(bank));

        actions.add(addWalletBtn);
        actions.add(viewWalletsBtn);
        actions.add(deleteBankBtn);
        item.add(actions, BorderLayout.EAST);

        return item;
    }


    private void deleteBank(BankAccount bank) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete '" + bank.getBankName() + "'?\nThis will remove all associated wallets.",
                "Confirm Delete Bank",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            SavingsTrackerSystem.getInstance().getCurrentUser().getBankAccounts().remove(bank);
            SavingsTrackerSystem.getInstance().saveData();
            refreshList();
        }
    }
    private void editBankLogo(BankAccount bank) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select New Bank Logo");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "png", "jpg", "jpeg"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            bank.setLogoPath(selectedFile.getAbsolutePath());

            // Save to database and refresh UI
//            SavingsTrackerSystem.getInstance().saveData();
            refreshList();
        }
    }

    private void addNewWallet(BankAccount bank) {
        String walletName = JOptionPane.showInputDialog(this, "Enter Wallet Name (e.g., Wallet 2):", "Add New Wallet", JOptionPane.QUESTION_MESSAGE);
        if (walletName != null && !walletName.trim().isEmpty()) {
            bank.addWallet(new Wallet(walletName.trim(), 0.0));
            SavingsTrackerSystem.getInstance().saveData();
            refreshList();
            JOptionPane.showMessageDialog(this, "Wallet '" + walletName + "' added successfully!");
        }
    }

    private void showWallets(BankAccount bank) {
        JFrame walletFrame = new JFrame("Wallets - " + bank.getBankName());
        walletFrame.setSize(500, 500); // Made it slightly wider to fit the new button
        walletFrame.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(UIUtils.createPadding(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        for (Wallet w : bank.getWallets()) {
            JPanel wp = new JPanel(new BorderLayout());
            wp.setBackground(new Color(240, 240, 240));
            wp.setBorder(UIUtils.createPadding(10, 15, 10, 15));
            wp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

            JLabel wl = new JLabel(w.getWalletName());
            wl.setFont(UIUtils.FONT_BOLD);
            wp.add(wl, BorderLayout.WEST);

            JLabel bl = new JLabel(String.format("Php%.2f", w.getBalance()));
            bl.setForeground(UIUtils.COLOR_BUTTON_TEAL);
            bl.setFont(UIUtils.FONT_BOLD);

            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            rightPanel.setOpaque(false);

            rightPanel.add(bl);

            // NEW EDIT AMOUNT BUTTON
            JButton editAmountBtn = UIUtils.createStyledButton("Edit", new Color(108, 117, 125), Color.WHITE);
            editAmountBtn.addActionListener(e -> {
                String newBalanceStr = JOptionPane.showInputDialog(walletFrame, "Enter new balance for " + w.getWalletName() + ":", w.getBalance());
                if (newBalanceStr != null && !newBalanceStr.trim().isEmpty()) {
                    try {
                        double newBalance = Double.parseDouble(newBalanceStr.trim());
                        w.setBalance(newBalance);
                        SavingsTrackerSystem.getInstance().saveData();
                        refreshList();

                        // Close and reopen the frame to refresh it instantly
                        walletFrame.dispose();
                        showWallets(bank);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(walletFrame, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            JButton deleteWalletBtn = UIUtils.createStyledButton("Delete", Color.RED, Color.WHITE);
            deleteWalletBtn.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                        walletFrame,
                        "Delete wallet '" + w.getWalletName() + "'?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    bank.getWallets().remove(w);
                    SavingsTrackerSystem.getInstance().saveData();
                    refreshList();

                    walletFrame.dispose();
                    showWallets(bank);
                }
            });

            rightPanel.add(editAmountBtn);
            rightPanel.add(deleteWalletBtn);
            wp.add(rightPanel, BorderLayout.EAST);

            panel.add(wp);
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        walletFrame.add(new JScrollPane(panel));
        walletFrame.setVisible(true);
    }

    private void addNewBank() {
        JTextField nameField = new JTextField();

        // Setup UI for the file browser
        JButton browseBtn = new JButton("Browse Image...");
        JLabel filePathLabel = new JLabel("assets/default.png"); // Default path
        filePathLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        filePathLabel.setForeground(Color.GRAY);

        browseBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Bank Logo");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "png", "jpg", "jpeg"));

            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                filePathLabel.setText(selectedFile.getAbsolutePath());
            }
        });

        Object[] message = {
                "Bank Name:", nameField,
                "Select Bank Logo:", browseBtn,
                filePathLabel
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Add New Bank", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String logo = filePathLabel.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Bank name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // --- STRICT CASE-INSENSITIVE VALIDATION LOGIC ---
            boolean isDuplicate = false;
            for (BankAccount existingBank : SavingsTrackerSystem.getInstance().getCurrentUser().getBankAccounts()) {
                // equalsIgnoreCase treats "bdo", "BDO", and "Bdo" as identical
                if (existingBank.getBankName().equalsIgnoreCase(name)) {
                    isDuplicate = true;
                    break;
                }
            }

            if (isDuplicate) {
                JOptionPane.showMessageDialog(null,
                        "A bank with the name '" + name + "' already exists!",
                        "Duplicate Bank",
                        JOptionPane.WARNING_MESSAGE);
                return; // Stops the code here, preventing the bank from being added
            }
            // --- END OF VALIDATION ---

            BankAccount newBank = new BankAccount(name, logo);
            newBank.addWallet(new Wallet("Main Wallet", 0.0));
            SavingsTrackerSystem.getInstance().getCurrentUser().addBankAccount(newBank);
            SavingsTrackerSystem.getInstance().saveData();
            refreshList();
        }
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) refreshList();
    }
}