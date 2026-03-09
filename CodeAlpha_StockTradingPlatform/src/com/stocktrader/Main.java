package com.stocktrader;

import com.stocktrader.models.Stock;
import com.stocktrader.models.StockSector;
import com.stocktrader.models.User;

public class Main {
    public static void main(String[] args) {

        // Create stocks
        Stock tcs      = new Stock("TCS",      "Tata Consultancy Services", 3850.00, StockSector.TECHNOLOGY);
        Stock reliance = new Stock("RELIANCE", "Reliance Industries",       2920.50, StockSector.ENERGY);
        Stock hdfc     = new Stock("HDFCBANK", "HDFC Bank Ltd",             1640.75, StockSector.FINANCE);

        // Create user
        User user1 = new User("U001", "User1");
        System.out.println(user1);

        // Test buying
        System.out.println("\n--- Buying shares ---");
        user1.buyShares(tcs, 5);
        user1.buyShares(reliance, 3);
        user1.buyShares(hdfc, 10);

        // Test buying more than balance allows
        System.out.println("\n--- Try to overspend ---");
        user1.buyShares(tcs, 100);  // should fail

        // Test selling
        System.out.println("\n--- Selling shares ---");
        user1.sellShares(tcs, 2);

        // Test selling more than owned
        System.out.println("\n--- Try to oversell ---");
        user1.sellShares(reliance, 10);  // should fail

        // Print portfolio
        user1.printPortfolio();

        // Print transaction history
        user1.printTransactionHistory();
    }
}