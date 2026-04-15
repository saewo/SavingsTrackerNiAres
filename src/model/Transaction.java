package model;

import java.io.Serializable;

/**
 * Transaction model representing a financial record.
 * Type (Income/Expense) is determined by the sign of the amount.
 */
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private double amount; // Positive for Income, Negative for Expense
    private String description;
    private String date;

    public Transaction(double amount, String description, String date) {
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    // --- Getters ---
    public String getType() { return amount >= 0 ? "Income" : "Expense"; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public String getDate() { return date; }

    // --- Setters ---
    public void setAmount(double amount) { this.amount = amount; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(String date) { this.date = date; }

    @Override
    public String toString() {
        return String.format("[%s] %s: Php%.2f (%s)", getType(), description, amount, date);
    }

    /**
     * Helper for JTable display.
     */
    public Object[] toTableRow(String walletName) {
        return new Object[] { walletName, description, amount, date };
    }
}
