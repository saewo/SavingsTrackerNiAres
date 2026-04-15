    package app;

    import panels.*;
    import model.SavingsTrackerSystem;
    import model.User;

    import javax.swing.*;
    import java.awt.*;
    import java.io.File;
    import java.util.HashMap;
    import java.util.Map;

    /**
     * Main application for the Savings Tracker Ni Ares.
     * Fixed sidebar alignment and added icon support.
     */
    public class SavingsTrackerNiAres extends JFrame {

        private CardLayout cardLayout;
        private JPanel mainContent;
        private JPanel sidebar;
        private JLabel welcomeHeaderLabel;
        private String currentCard = "Dashboard";
        private Map<String, String> iconMap;

        public SavingsTrackerNiAres() {
            try {
                // For absolute paths on your hard drive, don't use getResource
                String path = "C:/SavingsTrackerNiAres/assets/logo.png";
                ImageIcon img = new ImageIcon(path);

                // Check if the image actually loaded
                if (img.getImageLoadStatus() == java.awt.MediaTracker.COMPLETE) {
                    this.setIconImage(img.getImage());
                } else {
                    System.err.println("Logo file not found at: " + path);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            setTitle("Savings Tracker Ni Ares");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1250, 850);
            setMinimumSize(new Dimension(1100, 750));
            setLocationRelativeTo(null);

            // Initialize Icon Map
            iconMap = new HashMap<>();
            iconMap.put("Dashboard", "assets/dashboard_icon.png");
            iconMap.put("Banks", "assets/banks_icon.png");
            iconMap.put("Transaction", "assets/transaction_icon.png");
            iconMap.put("Add Records", "assets/add_icon.png");
            iconMap.put("Search", "assets/search_icon.png");
            iconMap.put("Profile", "assets/profile_icon.png");
            iconMap.put("Edit Transactions", "assets/edit_icon.png");

            // Add shutdown hook to save data
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                SavingsTrackerSystem.getInstance().saveData();
            }));

            // Main Container
            JPanel container = new JPanel(new BorderLayout());
            container.setBackground(UIUtils.COLOR_BACKGROUND);
            add(container);

            // 1. Sidebar
            sidebar = createSidebar();
            container.add(sidebar, BorderLayout.WEST);

            // 2. Right Side
            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.setOpaque(false);
            container.add(rightPanel, BorderLayout.CENTER);

            // Header
            JPanel header = createHeader();
            rightPanel.add(header, BorderLayout.NORTH);

            // Content Area
            cardLayout = new CardLayout();
            mainContent = new JPanel(cardLayout);
            mainContent.setOpaque(false);
            mainContent.setBorder(UIUtils.createPadding(20, 20, 20, 20));

            // Initialize Panels
            ProfilePanel profilePanel = new ProfilePanel(this);
            mainContent.add(new DashboardPanel(this), "Dashboard");
            mainContent.add(new BankListPanel(), "Banks");
            mainContent.add(new ViewSavingsPanel(), "Transaction");
            mainContent.add(new AddSavingsPanel(profilePanel), "Add Records");
            mainContent.add(new SearchSavingsPanel(), "Search");
            mainContent.add(profilePanel, "Profile");
            mainContent.add(new EditSavingsPanel(), "Edit Transactions");

            rightPanel.add(mainContent, BorderLayout.CENTER);
        }

        private JPanel createSidebar() {
            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(260, 0));
            panel.setBackground(UIUtils.COLOR_SIDEBAR);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(UIUtils.createPadding(20, 0, 20, 0));

            // Logo + Title Header
            JPanel logoTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
            logoTitlePanel.setOpaque(false);
            logoTitlePanel.setMaximumSize(new Dimension(260, 60));
            logoTitlePanel.setBorder(UIUtils.createPadding(10, 20, 40, 10));

            File logoFile = new File("assets/logo.png");
            if (logoFile.exists()) {
                ImageIcon logoIcon = new ImageIcon(new ImageIcon("assets/logo.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
                logoTitlePanel.add(new JLabel(logoIcon));
            }

            JLabel title = new JLabel("ARES SVTracker");
            title.setForeground(UIUtils.COLOR_ACCENT);
            title.setFont(new Font("Segoe UI", Font.BOLD, 18));
            logoTitlePanel.add(title);

            panel.add(logoTitlePanel);

            String[] items = {"Dashboard", "Banks", "Transaction", "Add Records", "Search", "Profile", "Edit Transactions"};
            for (String item : items) {
                panel.add(createSidebarButton(item));
                panel.add(Box.createRigidArea(new Dimension(0, 5)));
            }

            return panel;
        }

        private JButton createSidebarButton(String text) {
            JButton btn = new JButton(text);
            btn.setMaximumSize(new Dimension(260, 55));
            btn.setPreferredSize(new Dimension(260, 55));
            btn.setFont(UIUtils.FONT_BOLD);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Color logic
            boolean isActive = text.equals(currentCard);
            btn.setForeground(isActive ? UIUtils.COLOR_SIDEBAR : UIUtils.COLOR_TEXT_WHITE);
            btn.setBackground(isActive ? UIUtils.COLOR_ACCENT : UIUtils.COLOR_SIDEBAR);

            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setIconTextGap(15);
            btn.setBorder(UIUtils.createPadding(0, 25, 0, 0));

            // Icon logic
            String iconPath = iconMap.get(text);
            if (iconPath != null && new File(iconPath).exists()) {
                ImageIcon icon = new ImageIcon(new ImageIcon(iconPath).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
                btn.setIcon(icon);
            }

            btn.addActionListener(e -> switchCard(text));
            return btn;
        }

        private JPanel createHeader() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setPreferredSize(new Dimension(0, 100));
            panel.setBackground(UIUtils.COLOR_HEADER);
            panel.setBorder(UIUtils.createPadding(0, 30, 0, 30));

            User user = SavingsTrackerSystem.getInstance().getCurrentUser();
            JPanel welcomePanel = new JPanel(new GridLayout(2, 1));
            welcomePanel.setOpaque(false);

            welcomeHeaderLabel = new JLabel("Welcome back, " + user.getName());
            welcomeHeaderLabel.setForeground(Color.WHITE);
            welcomeHeaderLabel.setFont(UIUtils.FONT_TITLE);

            JLabel sl = new JLabel("AST is here as your personal money tracker.");
            sl.setForeground(Color.WHITE);
            sl.setFont(UIUtils.FONT_SUBTITLE);

            welcomePanel.add(welcomeHeaderLabel);
            welcomePanel.add(sl);
            panel.add(welcomePanel, BorderLayout.WEST);

            return panel;
        }

        public void refreshHeader() {
            User user = SavingsTrackerSystem.getInstance().getCurrentUser();
            welcomeHeaderLabel.setText("Welcome back " + user.getName());
            for (Component c : mainContent.getComponents()) {
                if (c.isVisible()) {
                    c.setVisible(true);
                }
            }
        }

        public void switchCard(String cardName) {
            currentCard = cardName;
            cardLayout.show(mainContent, cardName);
            for (Component c : sidebar.getComponents()) {
                if (c instanceof JButton) {
                    JButton b = (JButton) c;
                    boolean isActive = b.getText().equals(cardName);
                    b.setBackground(isActive ? UIUtils.COLOR_ACCENT : UIUtils.COLOR_SIDEBAR);
                    b.setForeground(isActive ? UIUtils.COLOR_SIDEBAR : UIUtils.COLOR_TEXT_WHITE);
                }
            }
        }

        public static void main(String[] args) {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
            SwingUtilities.invokeLater(() -> {
                new SavingsTrackerNiAres().setVisible(true);
            });
        }
    }
