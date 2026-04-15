package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the main user information.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
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
