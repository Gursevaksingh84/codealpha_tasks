package com.stocktrader.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Market {
    private final Map<String, Stock> stocks;
    private final Random random;
    private final String marketName;

    public Market(String marketName) {
        this.marketName = marketName;
        this.stocks     = new HashMap<>();
        this.random     = new Random();
        loadDefaultStocks();  // pre-load Indian stocks
    }

    private void loadDefaultStocks() {
        addStock(new Stock("TCS",        "Tata Consultancy Services",  3850.00, StockSector.TECHNOLOGY));
        addStock(new Stock("INFY",       "Infosys Ltd",                1520.00, StockSector.TECHNOLOGY));
        addStock(new Stock("WIPRO",      "Wipro Ltd",                   480.00, StockSector.TECHNOLOGY));
        addStock(new Stock("RELIANCE",   "Reliance Industries",        2920.50, StockSector.ENERGY));
        addStock(new Stock("ONGC",       "Oil & Natural Gas Corp",      265.00, StockSector.ENERGY));
        addStock(new Stock("HDFCBANK",   "HDFC Bank Ltd",              1640.75, StockSector.FINANCE));
        addStock(new Stock("ICICIBANK",  "ICICI Bank Ltd",               985.00, StockSector.FINANCE));
        addStock(new Stock("SBIBANK",    "State Bank of India",          745.00, StockSector.FINANCE));
        addStock(new Stock("TATAMOTORS", "Tata Motors Ltd",              945.00, StockSector.AUTOMOBILE));
        addStock(new Stock("MARUTI",     "Maruti Suzuki India",         10500.00, StockSector.AUTOMOBILE));
    }

    public void addStock(Stock stock) {
        stocks.put(stock.getSymbol(), stock);
    }

    // Returns null if not found — caller must check!
    public Stock getStock(String symbol) {
        return stocks.get(symbol.toUpperCase());
    }

    public boolean hasStock(String symbol) {
        return stocks.containsKey(symbol.toUpperCase());
    }

    public List<Stock> getAllStocks() {
        return new ArrayList<>(stocks.values());
    }

    public List<Stock> getStocksBySector(StockSector sector) {
        List<Stock> result = new ArrayList<>();
        for (Stock stock : stocks.values()) {
            if (stock.getSector() == sector) {
                result.add(stock);
            }
        }
        return result;
    }

    // ── Simulate market fluctuation ──────────────────────
    // Each stock price moves randomly between -3% and +3%
    public void simulatePriceUpdate() {
        System.out.println("\n--- Market price update ---");
        for (Stock stock : stocks.values()) {
            // Random % change between -3.0 and +3.0
            double changePercent = (random.nextDouble() * 6.0) - 3.0;
            double newPrice = stock.getCurrentPrice() * (1 + changePercent / 100);
            // Round to 2 decimal places
            newPrice = Math.round(newPrice * 100.0) / 100.0;
            stock.updatePrice(newPrice);
        }
        System.out.println("Prices updated for all " + stocks.size() + " stocks.");
    }


     public void displayAllStocks() {
        System.out.println("\n========================================");
        System.out.println("  " + marketName + " — LIVE MARKET DATA");
        System.out.println("========================================");
        System.out.printf("%-10s %-28s %10s %8s%n",
            "SYMBOL", "COMPANY", "PRICE", "CHANGE");
        System.out.println("----------------------------------------");

        for (Stock stock : stocks.values()) {
            String arrow    = stock.getPriceChangePercent() >= 0 ? "+" : "";
            System.out.printf("%-10s %-28s %10.2f %s%.2f%%%n",
                stock.getSymbol(),
                stock.getCompanyName(),
                stock.getCurrentPrice(),
                arrow,
                stock.getPriceChangePercent());
        }
        System.out.println("========================================");
    }

     public void displayBySector(StockSector sector) {
        List<Stock> sectorStocks = getStocksBySector(sector);
        System.out.println("\n--- " + sector + " sector ---");
        for (Stock s : sectorStocks) {
            System.out.println(s);
        }
    }

    public String getMarketName() { return marketName; }
    public int getStockCount()    { return stocks.size(); }

}
