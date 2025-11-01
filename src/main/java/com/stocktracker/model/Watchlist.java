package com.stocktracker.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents a single watchlist entry for a user.
 */
public class Watchlist implements Serializable {
    private int id;
    private int userId;
    private String symbol;
    private LocalDateTime addedAt;

    public Watchlist() {}

    // For creating a new entry before DB assigns id
    public Watchlist(int userId, String symbol) {
        this.userId = userId;
        this.symbol = symbol;
        this.addedAt = LocalDateTime.now();
    }

    // Full constructor
    public Watchlist(int id, int userId, String symbol, LocalDateTime addedAt) {
        this.id = id;
        this.userId = userId;
        this.symbol = symbol;
        this.addedAt = addedAt;
    }

    // Getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }

    // Convenience for JSP/display
    public String getAddedAtFormatted() {
        if (addedAt == null) return "";
        return addedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String toString() {
        return "Watchlist{" +
               "id=" + id +
               ", userId=" + userId +
               ", symbol='" + symbol + '\'' +
               ", addedAt=" + getAddedAtFormatted() +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Watchlist)) return false;
        Watchlist that = (Watchlist) o;
        return userId == that.userId && Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, symbol);
    }
}
