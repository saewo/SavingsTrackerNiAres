package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

/**
 * SavingsTrackerSystem / DatabaseManager
 * Handles overall system logic and data persistence (Nigga Database).
 */
public class SavingsTrackerSystem {

    private static final SavingsTrackerSystem instance = new SavingsTrackerSystem();
    private User currentUser;
    private static final String DATA_FILE = "savings_data.ser";

    private SavingsTrackerSystem() {
        loadData();
        if (currentUser == null) {
            manageUser(new User("Ares"));
            initializeDefaultData();
            saveData();
        }
    }

    private void initializeDefaultData() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);

        // GCASH
        BankAccount gcash = new BankAccount("G-Cash", "assets/gcash.png");
        Wallet gcashWallet = new Wallet("Main Wallet", 0.0);
        gcash.addWallet(gcashWallet);
        currentUser.addBankAccount(gcash);
        gcashWallet.addTransaction(new Transaction(5254.50, "Opening Balance", formattedDate));

        // BDO
        BankAccount bdo = new BankAccount("BDO", "assets/bdo.png");
        Wallet bdoWallet = new Wallet("Main Wallet", 0.0);
        bdo.addWallet(bdoWallet);
        currentUser.addBankAccount(bdo);
        bdoWallet.addTransaction(new Transaction(1000, "Initial Deposit", formattedDate));

        // BPI
        BankAccount bpi = new BankAccount("BPI", "assets/bpi.png");
        Wallet bpiWallet = new Wallet("Main Wallet", 0.0);
        bpi.addWallet(bpiWallet);
        currentUser.addBankAccount(bpi);
        bpiWallet.addTransaction(new Transaction(2000, "Savings", formattedDate));
    }

    public static SavingsTrackerSystem getInstance() {
        return instance;
    }

    public double computeTotalAssets() {
        double total = 0;
        if (currentUser != null) {
            for (BankAccount bank : currentUser.getBankAccounts()) {
                total += bank.getTotalBalance();
            }
        }
        return total;
    }

    public void manageUser(User user) {
        this.currentUser = user;
    }

    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(currentUser);
            System.out.println("Data saved successfully to " + DATA_FILE);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public void loadData() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                currentUser = (User) ois.readObject();
                System.out.println("Data loaded successfully from " + DATA_FILE);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading data: " + e.getMessage());
            }
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public List<TransactionRecord> getAllTransactionsFlat() {
        List<TransactionRecord> all = new ArrayList<>();
        if (currentUser != null) {
            for (BankAccount bank : currentUser.getBankAccounts()) {
                for (Wallet wallet : bank.getWallets()) {
                    for (Transaction t : wallet.getTransactions()) {
                        all.add(new TransactionRecord(bank, wallet, t));
                    }
                }
            }
        }
        return all;
    }

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
