package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles individual wallets under a bank.
 * Balance is derived from the sum of transactions.
 */
public class Wallet implements Serializable {
    private static final long serialVersionUID = 1L;
    private String walletName;
    private double initialBalance;
    private List<Transaction> transactions;

    public Wallet(String walletName, double initialBalance) {
        this.walletName = walletName;
        this.initialBalance = initialBalance;
        this.transactions = new ArrayList<>();
    }

    // --- Methods ---
    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setInitialBalance(double initialBalance) {
        this.initialBalance = initialBalance;
    }

    public double getBalance() {
        double currentBalance = initialBalance;
        for (Transaction t : transactions) {
            currentBalance += t.getAmount();
        }
        return currentBalance;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }
}
