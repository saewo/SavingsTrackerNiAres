package model;

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
        // Default user setup
        manageUser(new User("Ares"));
        
        // Initial setup with a default bank and wallet for testing/startup
        BankAccount defaultBank = new BankAccount("Default Bank", "logo.png");
        Wallet defaultWallet = new Wallet("Main Wallet", 0.0);
        defaultBank.addWallet(defaultWallet);
        currentUser.addBankAccount(defaultBank);

        // Pre-load some sample data
        defaultWallet.addTransaction(new Transaction("Income", 1000.0, "Opening Balance", "2026-04-01"));
        defaultWallet.addTransaction(new Transaction("Income", 2500.0, "Salary Deposit", "2026-04-05"));
        defaultWallet.addTransaction(new Transaction("Income", 150.0, "Investment Profit", "2026-04-06"));
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
