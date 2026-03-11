package com.stocktrader.engine;

import com.stocktrader.models.Market;
import com.stocktrader.models.Stock;
import com.stocktrader.models.User;

public class TradingEngine {

    private final Market market;
    private User currentUser;

    public TradingEngine(Market market) {
        this.market = market;
    }

     public void loginUser(String userId, String name) {
        this.currentUser = new User(userId, name);
        System.out.println("Welcome, " + name + "! Balance: Rs" +
            String.format("%.2f", currentUser.getBalance()));
    }

    public User getCurrentUser() { return currentUser; }
    public Market getMarket()    { return market; }

    // ── Buy ──────────────────────────────────────────────
    public boolean executeBuy(String symbol, int quantity) {
        if (!isLoggedIn()) return false;

        Stock stock = market.getStock(symbol);
        if (stock == null) {
            System.out.println("ERROR: Stock '" + symbol + "' not found.");
            return false;
        }
        return currentUser.buyShares(stock, quantity);
    }

    // ── Sell ─────────────────────────────────────────────
    public boolean executeSell(String symbol, int quantity) {
        if (!isLoggedIn()) return false;

        Stock stock = market.getStock(symbol);
        if (stock == null) {
            System.out.println("ERROR: Stock '" + symbol + "' not found.");
            return false;
        }
        return currentUser.sellShares(stock, quantity);
    }

    public void runMarketTick() {
        market.simulatePriceUpdate();
    }

     // ── Portfolio value ──────────────────────────────────
    // Total value = cash + current market value of all shares
    public double getTotalPortfolioValue() {
        if (!isLoggedIn()) return 0;

        double stockValue = 0;
        for (var entry : currentUser.getPortfolio().entrySet()) {
            Stock stock = market.getStock(entry.getKey());
            if (stock != null) {
                stockValue += stock.getCurrentPrice() * entry.getValue();
            }
        }
        return currentUser.getBalance() + stockValue;
    }

    // ── P&L (Profit and Loss) ────────────────────────────
    public double getProfitLoss() {
        if (!isLoggedIn()) return 0;
        return getTotalPortfolioValue() - 100000.00; // starting balance
    }

    // ── Helper ───────────────────────────────────────────
    private boolean isLoggedIn() {
        if (currentUser == null) {
            System.out.println("ERROR: No user logged in.");
            return false;
        }
        return true;
    }

}