package panels;

import app.SavingsTrackerNiAres;
import app.UIUtils;
import model.SavingsTrackerSystem;
import model.User;
import model.BankAccount;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Redesigned Profile panel for user info and bank summary.
 * Allows editing the user's name. Shows Bank Logos.
 */
public class ProfilePanel extends JPanel {
    private JLabel nameLabel;
    private JPanel banksPanel;
    private SavingsTrackerNiAres mainFrame;

    public ProfilePanel(SavingsTrackerNiAres mainFrame) {
        this.mainFrame = mainFrame;
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
        
        JLabel title = new JLabel("User Profile");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_DARK);
        header.add(title, BorderLayout.NORTH);

        JPanel nameEditPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 20));
        nameEditPanel.setOpaque(false);
        
        nameLabel = new JLabel("Name: ");
        nameLabel.setFont(UIUtils.FONT_BOLD);
        nameLabel.setForeground(UIUtils.COLOR_TEXT_DARK);
        nameEditPanel.add(nameLabel);
        
        JButton editNameBtn = UIUtils.createStyledButton("Edit Name", UIUtils.COLOR_BUTTON_TEAL, Color.WHITE);
        editNameBtn.setPreferredSize(new Dimension(120, 30));
        editNameBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        editNameBtn.addActionListener(e -> editName());
        nameEditPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        nameEditPanel.add(editNameBtn);
        
        header.add(nameEditPanel, BorderLayout.CENTER);
        card.add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        
        JLabel subtitle = new JLabel("Your Bank Accounts");
        subtitle.setFont(UIUtils.FONT_BOLD);
        subtitle.setForeground(UIUtils.COLOR_TEXT_DARK);
        subtitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        content.add(subtitle, BorderLayout.NORTH);

        banksPanel = new JPanel();
        banksPanel.setLayout(new BoxLayout(banksPanel, BoxLayout.Y_AXIS));
        banksPanel.setOpaque(false);
        
        JScrollPane scrollPane = new JScrollPane(banksPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        content.add(scrollPane, BorderLayout.CENTER);
        
        card.add(content, BorderLayout.CENTER);

        refreshBanksList();
    }

    private void editName() {
        User user = SavingsTrackerSystem.getInstance().getCurrentUser();
        String newName = JOptionPane.showInputDialog(this, "Enter new name:", user.getName());
        if (newName != null && !newName.trim().isEmpty()) {
            user.setName(newName.trim());
            SavingsTrackerSystem.getInstance().saveData();
            refreshBanksList();
            if (mainFrame != null) {
                mainFrame.refreshHeader();
            }
        }
    }

    public void refreshBanksList() {
        banksPanel.removeAll();
        SavingsTrackerSystem system = SavingsTrackerSystem.getInstance();
        User user = system.getCurrentUser();
        nameLabel.setText("Name: " + user.getName());

        for (BankAccount bank : user.getBankAccounts()) {
            JPanel bankCard = UIUtils.createRoundedPanel(15, new Color(245, 247, 248));
            bankCard.setLayout(new BorderLayout(15, 0));
            bankCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
            bankCard.setBorder(UIUtils.createPadding(15, 20, 15, 20));

            // Bank Info (Logo + Name)
            JPanel bankInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
            bankInfo.setOpaque(false);

            File logoFile = new File(bank.getLogoPath());
            if (logoFile.exists()) {
                ImageIcon icon = new ImageIcon(new ImageIcon(bank.getLogoPath()).getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH));
                bankInfo.add(new JLabel(icon));
            }

            JLabel name = new JLabel(bank.getBankName());
            name.setFont(UIUtils.FONT_BOLD);
            bankInfo.add(name);
            
            bankCard.add(bankInfo, BorderLayout.WEST);

            JLabel balance = new JLabel(String.format("Php%.2f", bank.getTotalBalance()));
            balance.setFont(UIUtils.FONT_BOLD);
            balance.setForeground(UIUtils.COLOR_BUTTON_TEAL);
            bankCard.add(balance, BorderLayout.EAST);

            banksPanel.add(bankCard);
            banksPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        banksPanel.revalidate();
        banksPanel.repaint();
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) refreshBanksList();
    }
}
