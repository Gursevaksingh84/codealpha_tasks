package com.stocktrader.models;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private final String userId;
    private final String name;
    private double balance;

    private final Map<String, Integer> portfolio;
    private final List<Transaction> transactionHistory;
    private static final double STARTING_BALANCE = 100000.00;

    public User(String userId, String name) {
        this.userId             = userId;
        this.name               = name;
        this.balance            = STARTING_BALANCE;
        this.portfolio          = new HashMap<>();
        this.transactionHistory = new ArrayList<>();
    }

    public String getUserId()   { return userId; }
    public String getName()     { return name; }
    public double getBalance()  { return balance; }
    public Map<String, Integer> getPortfolio() { return portfolio; }
    public List<Transaction> getTransactionHistory() { return transactionHistory; }

    public boolean buyShares(Stock stock, int quantity) {

        // Validation 1: quantity must be positive
        if (quantity <= 0) {
            System.out.println("ERROR: Quantity must be greater than 0.");
            return false;
        }

        double totalCost = stock.getCurrentPrice() * quantity;

        // Validation 2: enough balance?
        if (totalCost > balance) {
            System.out.printf("ERROR: Not enough balance. Need Rs%.2f, have Rs%.2f%n",
                totalCost, balance);
            return false;
        }

        // Deduct balance
        balance -= totalCost;

        // Update portfolio — getOrDefault returns 0 if stock not owned yet
        String symbol = stock.getSymbol();
        portfolio.put(symbol, portfolio.getOrDefault(symbol, 0) + quantity);

        // Record transaction
        Transaction tx = new Transaction(symbol, TransactionType.BUY,
                                         quantity, stock.getCurrentPrice());
        transactionHistory.add(tx);

        System.out.printf("SUCCESS: Bought %d shares of %s at Rs%.2f each. Total: Rs%.2f%n",
            quantity, symbol, stock.getCurrentPrice(), totalCost);
        System.out.printf("Remaining balance: Rs%.2f%n", balance);

        return true;
    }

    public boolean sellShares(Stock stock, int quantity) {

        // Validation 1: quantity must be positive
        if (quantity <= 0) {
            System.out.println("ERROR: Quantity must be greater than 0.");
            return false;
        }

        String symbol = stock.getSymbol();
        int owned = portfolio.getOrDefault(symbol, 0);

        // Validation 2: do we own enough shares?
        if (quantity > owned) {
            System.out.printf("ERROR: Not enough shares. Want to sell %d, own %d%n",
                quantity, owned);
            return false;
        }

        double totalEarned = stock.getCurrentPrice() * quantity;

        // Add money back
        balance += totalEarned;

        // Update portfolio
        int remaining = owned - quantity;
        if (remaining == 0) {
            portfolio.remove(symbol);  // remove stock entirely if 0 shares left
        } else {
            portfolio.put(symbol, remaining);
        }

        // Record transaction
        Transaction tx = new Transaction(symbol, TransactionType.SELL,
                                         quantity, stock.getCurrentPrice());
        transactionHistory.add(tx);

        System.out.printf("SUCCESS: Sold %d shares of %s at Rs%.2f each. Earned: Rs%.2f%n",
            quantity, symbol, stock.getCurrentPrice(), totalEarned);
        System.out.printf("New balance: Rs%.2f%n", balance);

        return true;
    }

    public void printPortfolio() {
        System.out.println("\n=== PORTFOLIO: " + name + " ===");
        System.out.printf("Cash Balance: Rs%.2f%n", balance);

        if (portfolio.isEmpty()) {
            System.out.println("No stocks owned.");
            return;
        }

        System.out.println("\nStocks owned:");
        for (Map.Entry<String, Integer> entry : portfolio.entrySet()) {
            System.out.printf("  %-12s -> %d shares%n",
                entry.getKey(), entry.getValue());
        }
    }

    public void printTransactionHistory() {
        System.out.println("\n=== TRANSACTION HISTORY: " + name + " ===");
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }
        for (Transaction tx : transactionHistory) {
            System.out.println(tx);
        }
    }

    @Override
    public String toString() {
        return String.format("User[%s] %s | Balance: Rs%.2f | Stocks: %d",
            userId, name, balance, portfolio.size());
    }

}
