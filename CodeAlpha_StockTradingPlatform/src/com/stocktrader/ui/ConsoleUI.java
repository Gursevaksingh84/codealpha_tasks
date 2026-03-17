package com.stocktrader.ui;

import com.stocktrader.engine.TradingEngine;
import com.stocktrader.models.Market;
import com.stocktrader.models.Stock;
import com.stocktrader.models.User;
import java.util.Map;
import java.util.Scanner;
import com.stocktrader.engine.PortfolioTracker;

public class ConsoleUI {
    
    private final TradingEngine engine;
    private final Scanner scanner;

    public ConsoleUI(TradingEngine engine) {
        this.engine  = engine;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        printBanner();
        loginPrompt();
        mainMenu();
        System.out.println("\nThank you for using BullRun Trading. Goodbye!");
        scanner.close();
    }

    private void printBanner() {
        System.out.println("============================================");
        System.out.println("   BULLRUN — Stock Trading Platform v1.0   ");
        System.out.println("============================================");
    }

    private void loginPrompt() {
       if (engine.hasSavedGame()) {
        System.out.print("Saved portfolio found! Load it? (y/n): ");
        String choice = scanner.nextLine().trim();
        if (choice.equalsIgnoreCase("y")) {
            System.out.print("Enter your name: ");
            String name = scanner.nextLine().trim();
            engine.loginUser("U001", name);
            engine.loadGame();
            return;
        }
    }
    System.out.println("Enter your name to start trading:");
    System.out.print("> ");
    String name = scanner.nextLine().trim();
    if (name.isEmpty()) name = "Trader";
    engine.loginUser("U001", name);
    }

     
private void mainMenu() {
    boolean running = true;
    while (running) {
        System.out.println("\n============ MAIN MENU ============");
        System.out.println("  1. View market");
        System.out.println("  2. Search stock");
        System.out.println("  3. Buy shares");
        System.out.println("  4. Sell shares");
        System.out.println("  5. View my portfolio");
        System.out.println("  6. View transaction history");
        System.out.println("  7. Simulate market update");
        System.out.println("  8. Save portfolio");
        System.out.println("  9. Performance report");
        System.out.println(" 10. Exit");
        System.out.println("===================================");
        System.out.print("Choose option: ");

        String input = scanner.nextLine().trim();
        switch (input) {
            case "1"  -> engine.getMarket().displayAllStocks();
            case "2"  -> searchStock();
            case "3"  -> buyFlow();
            case "4"  -> sellFlow();
            case "5"  -> showPortfolio();
            case "6"  -> engine.getCurrentUser().printTransactionHistory();
            case "7"  -> engine.runMarketTick();
            case "8"  -> engine.saveGame();
            case "9"  -> new PortfolioTracker(
                            engine.getCurrentUser(),
                            engine.getMarket()).printReport();
            case "10" -> running = false;
            default   -> System.out.println("Invalid option. Enter 1-10.");
        }
    }
}
    private void searchStock() {
        System.out.print("Enter stock symbol (e.g. TCS): ");
        String symbol = scanner.nextLine().trim().toUpperCase();
        Stock stock = engine.getMarket().getStock(symbol);
        if (stock == null) {
            System.out.println("Stock '" + symbol + "' not found.");
        } else {
            System.out.println("\n" + stock);
            System.out.printf("52W simulation range: Rs%.2f - Rs%.2f%n",
                stock.getCurrentPrice() * 0.85,
                stock.getCurrentPrice() * 1.15);
        }
    }

    private void buyFlow() {
        System.out.print("Enter stock symbol to buy: ");
        String symbol = scanner.nextLine().trim().toUpperCase();

        Stock stock = engine.getMarket().getStock(symbol);
        if (stock == null) {
            System.out.println("Stock not found.");
            return;
        }

        System.out.printf("Current price: Rs%.2f | Your balance: Rs%.2f%n",
            stock.getCurrentPrice(),
            engine.getCurrentUser().getBalance());

        System.out.print("Enter quantity: ");
        try {
            int qty = Integer.parseInt(scanner.nextLine().trim());
            double cost = stock.getCurrentPrice() * qty;
            System.out.printf("Total cost will be: Rs%.2f. Confirm? (y/n): ", cost);
            String confirm = scanner.nextLine().trim();
            if (confirm.equalsIgnoreCase("y")) {
                engine.executeBuy(symbol, qty);
            } else {
                System.out.println("Buy cancelled.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity. Please enter a number.");
        }
    }

     private void sellFlow() {
        User user = engine.getCurrentUser();
        if (user.getPortfolio().isEmpty()) {
            System.out.println("You don't own any stocks.");
            return;
        }

        System.out.println("Your stocks:");
        for (Map.Entry<String, Integer> entry : user.getPortfolio().entrySet()) {
            Stock s = engine.getMarket().getStock(entry.getKey());
            double val = s != null ? s.getCurrentPrice() * entry.getValue() : 0;
            System.out.printf("  %-12s %d shares  |  Current value: Rs%.2f%n",
                entry.getKey(), entry.getValue(), val);
        }

        System.out.print("Enter stock symbol to sell: ");
        String symbol = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter quantity: ");
        try {
            int qty = Integer.parseInt(scanner.nextLine().trim());
            Stock stock = engine.getMarket().getStock(symbol);
            if (stock != null) {
                double earned = stock.getCurrentPrice() * qty;
                System.out.printf("You will receive: Rs%.2f. Confirm? (y/n): ", earned);
                String confirm = scanner.nextLine().trim();
                if (confirm.equalsIgnoreCase("y")) {
                    engine.executeSell(symbol, qty);
                } else {
                    System.out.println("Sell cancelled.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity.");
        }
    }

    private void showPortfolio() {
        User user = engine.getCurrentUser();
        Market market = engine.getMarket();

        System.out.println("\n========== YOUR PORTFOLIO ==========");
        System.out.printf("Trader  : %s%n", user.getName());
        System.out.printf("Cash    : Rs%.2f%n", user.getBalance());

        if (!user.getPortfolio().isEmpty()) {
            System.out.println("\nHoldings:");
            System.out.printf("%-12s %6s %10s %12s%n",
                "SYMBOL", "QTY", "PRICE", "VALUE");
            System.out.println("--------------------------------------------");

            double totalStockValue = 0;
            for (Map.Entry<String, Integer> entry : user.getPortfolio().entrySet()) {
                Stock s   = market.getStock(entry.getKey());
                int qty   = entry.getValue();
                double val = s != null ? s.getCurrentPrice() * qty : 0;
                totalStockValue += val;
                System.out.printf("%-12s %6d %10.2f %12.2f%n",
                    entry.getKey(), qty,
                    s != null ? s.getCurrentPrice() : 0, val);
            }

            System.out.println("--------------------------------------------");
            System.out.printf("%-12s %6s %10s %12.2f%n",
                "STOCKS", "", "", totalStockValue);
        }

        double total = engine.getTotalPortfolioValue();
        double pnl   = engine.getProfitLoss();
        String pnlSign = pnl >= 0 ? "+" : "";

        System.out.println("============================================");
        System.out.printf("Total Value : Rs%.2f%n", total);
        System.out.printf("P&L         : %sRs%.2f (%s%.2f%%)%n",
            pnlSign, pnl, pnlSign, (pnl / 100000.0) * 100);
        System.out.println("============================================");
    }
}
