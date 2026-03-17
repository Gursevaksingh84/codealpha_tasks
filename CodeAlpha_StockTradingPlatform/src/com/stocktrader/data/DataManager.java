package com.stocktrader.data;

import com.stocktrader.models.Stock;
import com.stocktrader.models.Transaction;
import com.stocktrader.models.TransactionType;
import com.stocktrader.models.User;
import com.stocktrader.models.Market;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Map;

public class DataManager {

    // ── File paths ────────────────────────────────────────
    private static final String SAVE_DIR      = "data/";
    private static final String PORTFOLIO_FILE = SAVE_DIR + "portfolio.txt";

    // ── Save user portfolio ───────────────────────────────
    public static void savePortfolio(User user) {
        // Create data/ folder if it doesn't exist
        new File(SAVE_DIR).mkdirs();

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(PORTFOLIO_FILE))) {

            // Line 1: user info
            writer.write("USER|" + user.getUserId() + "|" +
                         user.getName() + "|" +
                         user.getBalance());
            writer.newLine();

            // Holdings
            for (Map.Entry<String, Integer> entry :
                    user.getPortfolio().entrySet()) {
                writer.write("HOLDING|" + entry.getKey() +
                             "|" + entry.getValue());
                writer.newLine();
            }

            // Transactions
            for (Transaction tx : user.getTransactionHistory()) {
                writer.write("TRANSACTION|" +
                    tx.getType()          + "|" +
                    tx.getStockSymbol()   + "|" +
                    tx.getQuantity()      + "|" +
                    tx.getPricePerShare() + "|" +
                    tx.getTimestamp());
                writer.newLine();
            }

            System.out.println("Portfolio saved to " + PORTFOLIO_FILE);

        } catch (IOException e) {
            System.out.println("ERROR saving portfolio: " + e.getMessage());
        }
    }

    // ── Load user portfolio ───────────────────────────────
    // Returns a User object restored from file, or null if no save found
    public static User loadPortfolio(Market market) {
        File file = new File(PORTFOLIO_FILE);
        if (!file.exists()) {
            System.out.println("No saved portfolio found. Starting fresh.");
            return null;
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(PORTFOLIO_FILE))) {

            User user = null;
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                switch (parts[0]) {

                    case "USER" -> {
                        // USER|U001|Gursevak|61500.00
                        String userId  = parts[1];
                        String name    = parts[2];
                        double balance = Double.parseDouble(parts[3]);
                        user = new User(userId, name);
                        user.setBalance(balance);  // we'll add this method
                        System.out.println("Loaded user: " + name +
                            " | Balance: Rs" + String.format("%.2f", balance));
                    }

                    case "HOLDING" -> {
                        // HOLDING|TCS|10
                        if (user != null) {
                            String symbol = parts[1];
                            int qty       = Integer.parseInt(parts[2]);
                            user.getPortfolio().put(symbol, qty);
                        }
                    }

                    case "TRANSACTION" -> {
                        // TRANSACTION|BUY|TCS|10|3850.00|2026-03-11T18:05:00
                        if (user != null) {
                            TransactionType type = TransactionType.valueOf(parts[1]);
                            String symbol   = parts[2];
                            int qty         = Integer.parseInt(parts[3]);
                            double price    = Double.parseDouble(parts[4]);
                            Transaction tx  = new Transaction(symbol, type, qty, price);
                            user.getTransactionHistory().add(tx);
                        }
                    }
                }
            }

            System.out.println("Portfolio loaded successfully.");
            return user;

        } catch (IOException e) {
            System.out.println("ERROR loading portfolio: " + e.getMessage());
            return null;
        }
    }

    // ── Check if save exists ──────────────────────────────
    public static boolean saveExists() {
        return new File(PORTFOLIO_FILE).exists();
    }
}