# 📈 BullRun — Stock Trading Platform

A console-based stock trading simulator built in Java using Object-Oriented Programming principles. Simulates a real trading environment with market data, buy/sell operations, portfolio tracking, and persistent data storage.

> Built as part of CodeAlpha Java Internship

---

## 🚀 Features

- 📊 **Live Market Display** — 10 Indian stocks with real-time price simulation
- 💹 **Buy & Sell Shares** — with balance and holdings validation
- 🗂 **Portfolio Tracker** — view holdings, current value, and P&L
- 📜 **Transaction History** — every trade recorded with timestamp
- 💾 **Save & Load** — portfolio persists across sessions using File I/O
- 📉 **Performance Report** — unrealised P&L per stock, cost basis, overall return

---

## 🖥 Sample Output

```
============================================
   BULLRUN — Stock Trading Platform v1.0
============================================

============ MAIN MENU ============
  1. View market
  2. Search stock
  3. Buy shares
  4. Sell shares
  5. View my portfolio
  6. View transaction history
  7. Simulate market update
  8. Save portfolio
  9. Performance report
 10. Exit
===================================

========================================
  NSE India — LIVE MARKET DATA
========================================
SYMBOL     COMPANY                      PRICE    CHANGE
--------------------------------------------------------
TCS        Tata Consultancy Services  3921.62   +1.86%
INFY       Infosys Ltd                1565.44   +2.99%
HDFCBANK   HDFC Bank Ltd              1666.95   +1.60%
RELIANCE   Reliance Industries        2972.01   +1.76%
SBIBANK    State Bank of India         726.38   -2.50%
========================================

╔══════════════════════════════════════════╗
║       PORTFOLIO PERFORMANCE REPORT       ║
╚══════════════════════════════════════════╝
 Trader        : Gursevak
 Cash Balance  : Rs61,500.00
 Stock Value   : Rs42,180.00
 Total Value   : Rs1,03,680.00
 Overall P&L   : +Rs3,680.00 (+3.68%)

 ─────── UNREALISED P&L PER STOCK ──────────
 SYMBOL       AVG COST  CUR PRICE          P&L
 TCS          3850.00   3921.62      +Rs2,148.60
 INFY         1520.00   1565.44      +Rs1,531.40
```

---

## 🏗 Project Structure

```
StockTradingPlatform/
├── src/
│   └── com/
│       └── stocktrader/
│           ├── Main.java                  ← Entry point
│           ├── models/
│           │   ├── Stock.java             ← Stock entity with price tracking
│           │   ├── StockSector.java       ← Enum: TECHNOLOGY, FINANCE, ENERGY...
│           │   ├── User.java              ← User with balance, portfolio, history
│           │   ├── Transaction.java       ← Single trade record with timestamp
│           │   └── TransactionType.java   ← Enum: BUY / SELL
│           ├── engine/
│           │   ├── Market.java            ← Stock registry + price simulation
│           │   ├── TradingEngine.java     ← Core buy/sell logic + P&L
│           │   └── PortfolioTracker.java  ← Performance report + unrealised P&L
│           ├── data/
│           │   └── DataManager.java       ← File I/O: save/load portfolio
│           └── ui/
│               └── ConsoleUI.java         ← Interactive menu system
├── data/
│   └── portfolio.txt                      ← Auto-generated save file
├── out/                                   ← Compiled .class files
├── .gitignore
└── README.md
```

---

## 🧠 OOP Concepts Used

| Concept | Where used |
|---------|-----------|
| **Classes & Objects** | `Stock`, `User`, `Transaction`, `Market` |
| **Encapsulation** | Private fields + getters/setters in all model classes |
| **Enums** | `StockSector`, `TransactionType` |
| **Collections** | `HashMap` for portfolio, `ArrayList` for transaction history |
| **Separation of Concerns** | Models / Engine / Data / UI in separate packages |
| **File I/O** | `BufferedWriter` / `BufferedReader` in `DataManager` |
| **Exception Handling** | Try/catch in buy/sell, file operations, input parsing |
| **Static methods** | `DataManager.savePortfolio()`, `DataManager.loadPortfolio()` |

---

## ⚙️ How to Run

### Prerequisites
- Java JDK 21+ — [Download here](https://adoptium.net)

### Compile
```bash
javac -d out src/com/stocktrader/models/*.java \
             src/com/stocktrader/data/*.java \
             src/com/stocktrader/engine/*.java \
             src/com/stocktrader/ui/*.java \
             src/com/stocktrader/Main.java
```

### Run
```bash
java -cp out com.stocktrader.Main
```

### Windows one-liner
```cmd
javac -d out src/com/stocktrader/models/*.java src/com/stocktrader/data/*.java src/com/stocktrader/engine/*.java src/com/stocktrader/ui/*.java src/com/stocktrader/Main.java && java -cp out com.stocktrader.Main
```

---

## 📦 Modules Built

| Module | Description |
|--------|-------------|
| Module 1 | `Stock` class — symbol, price, sector, change % |
| Module 2 | `User` class — balance, buy/sell, transaction history |
| Module 3 | `Market` class — 10 Indian stocks, price simulation, sector filter |
| Module 4 | `TradingEngine` + `ConsoleUI` — interactive console menu |
| Module 5 | `DataManager` — save/load portfolio with File I/O |
| Module 6 | `PortfolioTracker` — P&L report, cost basis, unrealised gains |

---

## 📊 Stocks Available

| Symbol | Company | Sector |
|--------|---------|--------|
| TCS | Tata Consultancy Services | Technology |
| INFY | Infosys Ltd | Technology |
| WIPRO | Wipro Ltd | Technology |
| RELIANCE | Reliance Industries | Energy |
| ONGC | Oil & Natural Gas Corp | Energy |
| HDFCBANK | HDFC Bank Ltd | Finance |
| ICICIBANK | ICICI Bank Ltd | Finance |
| SBIBANK | State Bank of India | Finance |
| TATAMOTORS | Tata Motors Ltd | Automobile |
| MARUTI | Maruti Suzuki India | Automobile |

---

## 👨‍💻 Author

**Gursevak Singh Aulakh**
BE Computer Engineering — GGCOERC Nashik
[GitHub](https://github.com/Gursevaksingh84) · [LinkedIn](https://linkedin.com/in/your-linkedin)

---

*Built during CodeAlpha Java Internship — 2025*