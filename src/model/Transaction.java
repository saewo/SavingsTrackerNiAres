package model;

/**
 * Person 4: Transaction
 * Handles income/expense records.
 */
public class Transaction {
    private String type; // Income or Expense
    private double amount;
    private String description;
    private String date;

    public Transaction(String type, double amount, String description, String date) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    // --- Getters ---
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public String getDate() { return date; }

    // --- Setters ---
    public void setType(String type) { this.type = type; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(String date) { this.date = date; }

    @Override
    public String toString() {
        return String.format("[%s] %s: $%.2f (%s)", type, description, amount, date);
    }

    /**
     * Helper for JTable display.
     */
    public Object[] toTableRow(String walletName) {
        return new Object[] { walletName, description, amount, date, type };
    }
}
