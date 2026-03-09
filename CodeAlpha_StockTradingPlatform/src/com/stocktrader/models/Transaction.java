package com.stocktrader.models;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private final String stockSymbol;
    private final TransactionType type;    
    private final int quantity;
    private final double pricePerShare;
    private final double totalAmount;
    private final LocalDateTime timestamp;

    public Transaction(String stockSymbol, TransactionType type,
                       int quantity, double pricePerShare) {
        this.stockSymbol   = stockSymbol;
        this.type          = type;
        this.quantity      = quantity;
        this.pricePerShare = pricePerShare;
        this.totalAmount   = quantity * pricePerShare;
        this.timestamp     = LocalDateTime.now();  // auto-stamp time
    }

    public String getStockSymbol()     { return stockSymbol; }
    public TransactionType getType()   { return type; }
    public int getQuantity()           { return quantity; }
    public double getPricePerShare()   { return pricePerShare; }
    public double getTotalAmount()     { return totalAmount; }
    public LocalDateTime getTimestamp(){ return timestamp; }

     @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return String.format(
            "%s | %-4s | %-10s | Qty: %3d | Price: %8.2f | Total: %10.2f",
            timestamp.format(fmt),
            type,
            stockSymbol,
            quantity,
            pricePerShare,
            totalAmount
        );
    }
    
}
