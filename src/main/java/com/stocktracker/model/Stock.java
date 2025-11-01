package com.stocktracker.model;

public class Stock {

    private int id;
    private String symbol;
    private String name;
    private double price;
    private double change;
    private double percentageChange;

    // Default Constructor
    public Stock() {
    }

    // Parameterized Constructor
    public Stock(int id, String symbol, String name, double price, double change, double percentageChange) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.change = change;
        this.percentageChange = percentageChange;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return "Stock{"
                + "id=" + id
                + ", symbol='" + symbol + '\''
                + ", name='" + name + '\''
                + ", price=" + price
                + ", change=" + change
                + ", percentageChange=" + percentageChange
                + '}';
    }
}
