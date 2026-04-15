package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a specific wallet/container of funds within a Bank Account.
 */
public class Wallet implements Serializable {
    private static final long serialVersionUID = 1L;

    private String walletName;
    private double balance;
    private List<Transaction> transactions;

    public Wallet(String walletName, double initialBalance) {
        this.walletName = walletName;
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
    }

    // --- Core Methods ---

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        // Automatically update the balance when a new transaction is added
        this.balance += transaction.getAmount();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    // --- Getters and Setters ---

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public double getBalance() {
        return balance;
    }

    // THIS IS THE METHOD THAT FIXES YOUR ERROR!
    public void setBalance(double balance) {
        this.balance = balance;
    }
}