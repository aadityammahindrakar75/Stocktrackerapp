package com.stocktracker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.stocktracker.util.DBConnection;

/*
 Suggested MySQL table schema:

 CREATE TABLE watchlist (
   id INT AUTO_INCREMENT PRIMARY KEY,
   user_id INT NOT NULL,
   symbol VARCHAR(16) NOT NULL,
   company_name VARCHAR(255),
   latest_price DOUBLE DEFAULT 0,
   added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   UNIQUE KEY user_symbol_unique (user_id, symbol),
   FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
 );
*/

public class WatchlistDAO {

    private static final String SELECT_BY_USER_SQL =
            "SELECT id, user_id, symbol, company_name, latest_price, added_at FROM watchlist WHERE user_id = ? ORDER BY added_at DESC";

    private static final String INSERT_SQL =
            "INSERT INTO watchlist (user_id, symbol, company_name, latest_price) VALUES (?, ?, ?, ?)";

    private static final String DELETE_SQL =
            "DELETE FROM watchlist WHERE user_id = ? AND symbol = ?";

    private static final String EXISTS_SQL =
            "SELECT id FROM watchlist WHERE user_id = ? AND symbol = ?";

    public List<WatchlistItem> getUserWatchlist(int userId) {
        List<WatchlistItem> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_USER_SQL)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String symbol = rs.getString("symbol");
                    String companyName = rs.getString("company_name");
                    double latestPrice = rs.getDouble("latest_price");
                    Timestamp ts = rs.getTimestamp("added_at");
                    LocalDateTime addedAt = ts != null ? ts.toLocalDateTime() : null;

                    WatchlistItem item = new WatchlistItem(id, userId, symbol, companyName, latestPrice, addedAt);
                    list.add(item);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addToWatchlist(int userId, String symbol) {
        // prevent duplicates
        if (isInWatchlist(userId, symbol)) return false;

        // companyName and latestPrice are optional now; you can fetch from API later
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            ps.setInt(1, userId);
            ps.setString(2, symbol);
            ps.setString(3, null); // company_name - fill later
            ps.setDouble(4, 0.0);  // latest_price - update later
            int rows = ps.executeUpdate();
            return rows == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeFromWatchlist(int userId, String symbol) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, userId);
            ps.setString(2, symbol);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isInWatchlist(int userId, String symbol) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(EXISTS_SQL)) {

            ps.setInt(1, userId);
            ps.setString(2, symbol);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // on error, be conservative and return true to avoid duplicates
            return true;
        }
    }
}
