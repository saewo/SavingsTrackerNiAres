package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Person 5: SavingsTrackerSystem / DatabaseManager
 * Handles overall system logic and data management.
 */
public class SavingsTrackerSystem {

    private static final SavingsTrackerSystem instance = new SavingsTrackerSystem();
    private User currentUser;

    private SavingsTrackerSystem() {
        manageUser(new User("Ares"));
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);

        //GCASH
        BankAccount gcash = new BankAccount("G-Cash", "logo.png");
        Wallet gcashdefaultWallet = new Wallet("Main Wallet", 0.0);
        gcash.addWallet(gcashdefaultWallet);
        currentUser.addBankAccount(gcash);
        gcashdefaultWallet.addTransaction(new Transaction("Income", 0, "Opening Balance", formattedDate));

        //BDO
        BankAccount bdo = new BankAccount("BDO", "logo.png");
        Wallet bdodefaultWallet = new Wallet("Main Wallet", 0.0);
        bdo.addWallet(bdodefaultWallet);
        currentUser.addBankAccount(bdo);
        bdodefaultWallet.addTransaction(new Transaction("Income", 0, "Opening Balance", formattedDate));


        //BPI
        BankAccount BPI = new BankAccount("BPI", "logo.png");
        Wallet bpidefaultWallet = new Wallet("Main Wallet", 0.0);
        BPI.addWallet(bpidefaultWallet);
        currentUser.addBankAccount(BPI);
        bpidefaultWallet.addTransaction(new Transaction("Income", 0, "Opening Balance", formattedDate));







    }

    public static SavingsTrackerSystem getInstance() {
        return instance;
    }

    // --- Person 5 Methods ---

    public double computeTotalAssets() {
        double total = 0;
        for (BankAccount bank : currentUser.getBankAccounts()) {
            total += bank.getTotalBalance();
        }
        return total;
    }

    public void manageUser(User user) {
        this.currentUser = user;
    }

    public void saveData() {
        // Logic for saving data (e.g., to JSON or database)
        System.out.println("Data saved successfully for user: " + currentUser.getName());
    }

    public void loadData() {
        // Logic for loading data
        System.out.println("Data loaded successfully.");
    }

    // --- Helper Methods ---

    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Helper to get all transactions across all banks and wallets for display.
     */
    public List<TransactionRecord> getAllTransactionsFlat() {
        List<TransactionRecord> all = new ArrayList<>();
        for (BankAccount bank : currentUser.getBankAccounts()) {
            for (Wallet wallet : bank.getWallets()) {
                for (Transaction t : wallet.getTransactions()) {
                    all.add(new TransactionRecord(bank, wallet, t));
                }
            }
        }
        return all;
    }

    /**
     * Simple container for flat transaction display.
     */
    public static class TransactionRecord {
        public BankAccount bank;
        public Wallet wallet;
        public Transaction transaction;

        public TransactionRecord(BankAccount bank, Wallet wallet, Transaction transaction) {
            this.bank = bank;
            this.wallet = wallet;
            this.transaction = transaction;
        }
    }
}
