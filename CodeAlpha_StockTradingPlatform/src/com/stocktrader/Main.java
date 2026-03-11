package com.stocktrader;

import com.stocktrader.engine.TradingEngine;
import com.stocktrader.models.Market;
import com.stocktrader.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        Market market          = new Market("NSE India");
        TradingEngine engine   = new TradingEngine(market);
        ConsoleUI ui           = new ConsoleUI(engine);
        ui.start();
    }
}