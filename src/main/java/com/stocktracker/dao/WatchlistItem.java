package com.stocktracker.dao;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a stock entry in a user's watchlist with additional display info.
 */
public class WatchlistItem implements Serializable {

    private int id;
    private int userId;
    private String symbol;
    private String companyName;
    private double latestPrice;
    private LocalDateTime addedAt;

    public WatchlistItem() {}

    public WatchlistItem(int id, int userId, String symbol, String companyName,
                         double latestPrice, LocalDateTime addedAt) {
        this.id = id;
        this.userId = userId;
        this.symbol = symbol;
        this.companyName = companyName;
        this.latestPrice = latestPrice;
        this.addedAt = addedAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public double getLatestPrice() { return latestPrice; }
    public void setLatestPrice(double latestPrice) { this.latestPrice = latestPrice; }

    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }

    public String getAddedAtFormatted() {
        if (addedAt == null) return "";
        return addedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String toString() {
        return "WatchlistItem{" +
                "id=" + id +
                ", userId=" + userId +
                ", symbol='" + symbol + '\'' +
                ", companyName='" + companyName + '\'' +
                ", latestPrice=" + latestPrice +
                ", addedAt=" + getAddedAtFormatted() +
                '}';
    }
}
