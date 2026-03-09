package com.stocktrader;
import com.stocktrader.models.Stock;
import com.stocktrader.models.StockSector;

public class Main {
    public static void main(String[] args) {
        Stock tcs     = new Stock("TCS",      "Tata Consultancy Services", 3850.00, StockSector.TECHNOLOGY);
        Stock reliance= new Stock("RELIANCE", "Reliance Industries",       2920.50, StockSector.ENERGY);
        Stock hdfc    = new Stock("HDFCBANK", "HDFC Bank Ltd",             1640.75, StockSector.FINANCE);

        System.out.println("=== MARKET DATA ===");
        System.out.println(tcs);
        System.out.println(reliance);
        System.out.println(hdfc);

        System.out.println("\n--- Price update ---");
        tcs.updatePrice(3920.00);      // price went UP
        reliance.updatePrice(2875.00); // price went DOWN
        hdfc.updatePrice(1640.75);     // no change

        System.out.println("\n=== UPDATED MARKET DATA ===");
        System.out.println(tcs);
        System.out.println(reliance);
        System.out.println(hdfc);

        System.out.println("\n--- Individual checks ---");
        System.out.printf("TCS change: %.2f%%%n", tcs.getPriceChangePercent());
        System.out.printf("Reliance sector: %s%n", reliance.getSector());
    }
}
