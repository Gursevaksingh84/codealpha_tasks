package com.stocktrader.engine;

import com.stocktrader.models.Market;
import com.stocktrader.models.Stock;
import com.stocktrader.models.Transaction;
import com.stocktrader.models.TransactionType;
import com.stocktrader.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PortfolioTracker {

    private final User user;
    private final Market market;
    private static final double STARTING_BALANCE = 100000.00;

    public PortfolioTracker(User user, Market market) {
        this.user   = user;
        this.market = market;
    }

    // ── Total cash + stock value ──────────────────────────
    public double getTotalValue() {
        return user.getBalance() + getTotalStockValue();
    }

    // ── Current market value of all holdings ─────────────
    public double getTotalStockValue() {
        double total = 0;
        for (Map.Entry<String, Integer> entry : user.getPortfolio().entrySet()) {
            Stock stock = market.getStock(entry.getKey());
            if (stock != null) {
                total += stock.getCurrentPrice() * entry.getValue();
            }
        }
        return total;
    }

    // ── Total amount invested (what you paid, not current) ─
    public double getTotalInvested() {
        double invested = 0;
        for (Transaction tx : user.getTransactionHistory()) {
            if (tx.getType() == TransactionType.BUY) {
                invested += tx.getTotalAmount();
            } else {
                invested -= tx.getTotalAmount();
            }
        }
        return Math.max(invested, 0);
    }

    // ── Unrealised P&L per stock ──────────────────────────
    // Unrealised = you still own it, paper profit/loss
    public Map<String, Double> getUnrealisedPnL() {
        Map<String, Double> pnl = new HashMap<>();

        // Calculate average buy price per stock
        Map<String, Double> totalPaid = new HashMap<>();
        Map<String, Integer> totalQty = new HashMap<>();

        for (Transaction tx : user.getTransactionHistory()) {
            String sym = tx.getStockSymbol();
            if (tx.getType() == TransactionType.BUY) {
                totalPaid.merge(sym, tx.getTotalAmount(), Double::sum);
                totalQty.merge(sym, tx.getQuantity(), Integer::sum);
            }
        }

        // For each stock currently owned
        for (Map.Entry<String, Integer> entry : user.getPortfolio().entrySet()) {
            String sym   = entry.getKey();
            int heldQty  = entry.getValue();
            Stock stock  = market.getStock(sym);
            if (stock == null) continue;

            double paid     = totalPaid.getOrDefault(sym, 0.0);
            int boughtQty   = totalQty.getOrDefault(sym, 1);
            double avgPrice = paid / boughtQty;
            double currentVal = stock.getCurrentPrice() * heldQty;
            double costBasis  = avgPrice * heldQty;
            pnl.put(sym, currentVal - costBasis);
        }
        return pnl;
    }

    // ── Overall P&L ───────────────────────────────────────
    public double getOverallPnL() {
        return getTotalValue() - STARTING_BALANCE;
    }

    public double getOverallPnLPercent() {
        return (getOverallPnL() / STARTING_BALANCE) * 100;
    }

    // ── Print full tracker report ─────────────────────────
    public void printReport() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║         PORTFOLIO PERFORMANCE REPORT      ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.printf(" Trader        : %s%n", user.getName());
        System.out.printf(" Cash Balance  : Rs%,.2f%n", user.getBalance());
        System.out.printf(" Stock Value   : Rs%,.2f%n", getTotalStockValue());
        System.out.printf(" Total Value   : Rs%,.2f%n", getTotalValue());
        System.out.printf(" Total Invested: Rs%,.2f%n", getTotalInvested());

        double pnl    = getOverallPnL();
        double pnlPct = getOverallPnLPercent();
        String sign   = pnl >= 0 ? "+" : "";
        System.out.printf(" Overall P&L   : %sRs%,.2f (%s%.2f%%)%n",
            sign, pnl, sign, pnlPct);

        System.out.println("\n─────── UNREALISED P&L PER STOCK ──────────");
        System.out.printf(" %-12s %10s %10s %12s%n",
            "SYMBOL", "AVG COST", "CUR PRICE", "P&L");
        System.out.println(" ──────────────────────────────────────────");

        Map<String, Double> unrealised = getUnrealisedPnL();
        if (unrealised.isEmpty()) {
            System.out.println(" No holdings.");
        } else {
            for (Map.Entry<String, Double> entry : unrealised.entrySet()) {
                String sym    = entry.getKey();
                double stockPnl = entry.getValue();
                Stock stock   = market.getStock(sym);
                String pnlStr = (stockPnl >= 0 ? "+" : "") +
                    String.format("Rs%,.2f", stockPnl);
                System.out.printf(" %-12s %10.2f %10.2f %12s%n",
                    sym,
                    stock != null ? stock.getPreviousPrice() : 0,
                    stock != null ? stock.getCurrentPrice() : 0,
                    pnlStr);
            }
        }
        System.out.println(" ══════════════════════════════════════════");

        // Transaction summary
        long buys  = user.getTransactionHistory().stream()
            .filter(t -> t.getType() == TransactionType.BUY).count();
        long sells = user.getTransactionHistory().stream()
            .filter(t -> t.getType() == TransactionType.SELL).count();
        System.out.printf(" Total Trades  : %d  (Buys: %d | Sells: %d)%n",
            buys + sells, buys, sells);
        System.out.println(" ══════════════════════════════════════════\n");
    }
}