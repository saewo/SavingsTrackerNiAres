package model;

/**
 * Transaction class handles income/expense records.
 */
public class Transaction {
    private String type;
    private double amount;
    private String description;
    private String date;

    public Transaction(String type, double amount, String description, String date) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    // --- Getters & Setters ---
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public String getDate() { return date; }

    public void setType(String type) { this.type = type; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(String date) { this.date = date; }

    /**
     * UPDATED: Now accepts 2 arguments to fill 6 columns.
     * Order: Bank, Wallet, Description, Amount, Date, Type
     */
    public Object[] toTableRow(String bankName, String walletName) {
        return new Object[] {
                bankName,      // Column 0
                walletName,    // Column 1
                description,   // Column 2
                amount,        // Column 3
                date,          // Column 4
                type           // Column 5
        };
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: $%.2f (%s)", type, description, amount, date);
    }
}