package com.stocktrader.models;

public class Stock {
    private final String symbol;
    private final String companyName;
    private final StockSector sector;
    private double currentPrice;
    private double previousPrice;

    public Stock(String symbol, String companyName, double initialPrice, StockSector sector) {
        this.symbol        = symbol.toUpperCase();
        this.companyName   = companyName;
        this.currentPrice  = initialPrice;
        this.previousPrice = initialPrice;
        this.sector        = sector;

    }

    public String getSymbol()      { return symbol; }
    public String getCompanyName() { return companyName; }
    public StockSector getSector() { return sector; }
    public double getCurrentPrice(){ return currentPrice; }
    public double getPreviousPrice(){ return previousPrice; }

    // Called when market price changes
    public void updatePrice(double newPrice) {
        this.previousPrice = this.currentPrice;  // save old price first
        this.currentPrice  = newPrice;
    }

    // Formula: ((current - previous) / previous) * 100
    public double getPriceChangePercent() {
        if (previousPrice == 0) return 0;
        return ((currentPrice - previousPrice) / previousPrice) * 100;
    }

    @Override
    public String toString() {
        String direction = currentPrice >= previousPrice ? "▲" : "▼";
        return String.format(
            "[%s] %-20s | ₹%8.2f | %s %.2f%% | %s",
            symbol,
            companyName,
            currentPrice,
            direction,
            Math.abs(getPriceChangePercent()),
            sector
        );
    }

}
