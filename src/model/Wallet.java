package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Person 3: Wallet
 * Handles individual wallets under a bank.
 */
public class Wallet {
    private String walletName;
    private double balance;
    private List<Transaction> transactions;

    public Wallet(String walletName, double initialBalance) {
        this.walletName = walletName;
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
    }

    // --- Methods ---
    public void addTransaction(Transaction t) {
        transactions.add(t);
        // If it's an expense, subtract. If income, add.
        if (t.getType().equalsIgnoreCase("Expense")) {
            balance -= t.getAmount();
        } else {
            balance += t.getAmount();
        }
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }
}
