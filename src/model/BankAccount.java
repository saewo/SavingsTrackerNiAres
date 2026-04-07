package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Person 2: BankAccount
 * Handles bank details (name, logo).
 */
public class BankAccount {
    private String bankName;
    private String logoPath;
    private List<Wallet> wallets;

    public BankAccount(String bankName, String logoPath) {
        this.bankName = bankName;
        this.logoPath = logoPath;
        this.wallets = new ArrayList<>();
    }

    // --- Methods ---
    public void addWallet(Wallet wallet) {
        wallets.add(wallet);
    }

    public List<Wallet> getWallets() {
        return wallets;
    }

    public double getTotalBalance() {
        double total = 0;
        for (Wallet w : wallets) {
            total += w.getBalance();
        }
        return total;
    }

    // --- Getters and Setters ---
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getLogoPath() { return logoPath; }
    public void setLogoPath(String logoPath) { this.logoPath = logoPath; }
}
