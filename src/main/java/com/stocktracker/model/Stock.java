package com.stocktracker.model;

public class Stock {

    private int id;
    private String symbol;
    private String name;
    private double price;
    private double change;
    private double percentageChange;
    private int quantity;           // ✅ new field
    private boolean alreadyOwned;   // ✅ to mark if user already owns this stock

    // 🔹 Default Constructor
    public Stock() {
    }

    // 🔹 Constructor for general stock use (without ownership)
    public Stock(int id, String symbol, String name, double price, double change, double percentageChange, int quantity, boolean alreadyOwned) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.change = change;
        this.percentageChange = percentageChange;
        this.quantity = quantity;
        this.alreadyOwned = alreadyOwned;
    }

    // 🔹 Constructor without "id" (useful for insert operations)
    public Stock(String symbol, String name, double price, double change, double percentageChange, int quantity, boolean alreadyOwned) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.change = change;
        this.percentageChange = percentageChange;
        this.quantity = quantity;
        this.alreadyOwned = alreadyOwned;
    }
    public Stock(int id, String symbol, String name, double price, double change, double percentageChange) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.change = change;
        this.percentageChange = percentageChange;
        this.quantity = 0;
        this.alreadyOwned = false;
    }


    // ✅ Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public double getChange() {
        return change;
    }
    public void setChange(double change) {
        this.change = change;
    }

    public double getPercentageChange() {
        return percentageChange;
    }
    public void setPercentageChange(double percentageChange) {
        this.percentageChange = percentageChange;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isAlreadyOwned() {
        return alreadyOwned;
    }
    public void setAlreadyOwned(boolean alreadyOwned) {
        this.alreadyOwned = alreadyOwned;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", change=" + change +
                ", percentageChange=" + percentageChange +
                ", quantity=" + quantity +
                ", alreadyOwned=" + alreadyOwned +
                '}';
    }
}
