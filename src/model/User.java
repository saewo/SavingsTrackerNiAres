package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Person 1: User
 * Handles the main user information.
 */
public class User {
    private String name;
    private List<BankAccount> bankAccounts;

    public User(String name) {
        this.name = name;
        this.bankAccounts = new ArrayList<>();
    }

    // --- Methods ---
    public void addBankAccount(BankAccount bankAccount) {
        bankAccounts.add(bankAccount);
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
