package model;

import javax.swing.*;

/**
 * Data model representing a Savings Record.
 */
public class SavingsRecord {
    private String id;
    private String description;
    private double amount;
    private String date;
    private String category;

    public SavingsRecord(String id, String description, double amount, String date, String category) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    // --- Getters ---
    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    // --- Setters ---
    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return id + " - " + description + " ($" + amount + ") - " + date + " [" + category + "]";
    }

    /**
     * Returns savings record data as an Object array, useful for JTable rows.
     */
    public Object[] toTableRow() {
        return new Object[] { id, description, amount, date, category };
    }
}