package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Central in-memory data store for savings records.
 */
public class SavingsDataStore {

    private static final SavingsDataStore instance = new SavingsDataStore();
    private final List<SavingsRecord> records;

    private SavingsDataStore() {
        records = new ArrayList<>();
        // Pre-load some sample data
        records.add(new SavingsRecord("TXN-001", "Opening Balance", 1000.0, "2026-04-01", "Deposit"));
        records.add(new SavingsRecord("TXN-002", "Salary Deposit", 2500.0, "2026-04-05", "Salary"));
        records.add(new SavingsRecord("TXN-003", "Investment Profit", 150.0, "2026-04-06", "Investment"));
    }

    public static SavingsDataStore getInstance() {
        return instance;
    }

    public List<SavingsRecord> getAllRecords() {
        return records;
    }

    public void addRecord(SavingsRecord record) {
        records.add(record);
    }

    public void removeRecord(int index) {
        if (index >= 0 && index < records.size()) {
            records.remove(index);
        }
    }

    public SavingsRecord findById(String id) {
        for (SavingsRecord r : records) {
            if (r.getId().equals(id)) {
                return r;
            }
        }
        return null;
    }

    public double getTotalSavings() {
        double total = 0;
        for (SavingsRecord r : records) {
            total += r.getAmount();
        }
        return total;
    }

    public int getCount() {
        return records.size();
    }
}