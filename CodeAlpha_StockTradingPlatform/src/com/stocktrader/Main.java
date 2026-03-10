package com.stocktrader;

import com.stocktrader.models.Market;
import com.stocktrader.models.Stock;
import com.stocktrader.models.StockSector;
import com.stocktrader.models.User;

public class Main {
    public static void main(String[] args) {

        // Create market
        Market market = new Market("NSE India");
        System.out.println("Market loaded: " + market.getStockCount() + " stocks");

        // Display all stocks
        market.displayAllStocks();

        // Look up a specific stock
        Stock tcs = market.getStock("TCS");
        System.out.println("\nLookup TCS: " + tcs);

        // Display by sector
        market.displayBySector(StockSector.TECHNOLOGY);

        // Simulate price movement
        market.simulatePriceUpdate();
        market.displayAllStocks();

        // Create user and trade using market stocks
        System.out.println("\n--- User Trading ---");
        User user1 = new User("U001", "User1");
        user1.buyShares(market.getStock("TCS"), 3);
        user1.buyShares(market.getStock("INFY"), 5);

        // Another price update — user's stocks change value
        market.simulatePriceUpdate();
        user1.printPortfolio();
        user1.printTransactionHistory();
    }
}