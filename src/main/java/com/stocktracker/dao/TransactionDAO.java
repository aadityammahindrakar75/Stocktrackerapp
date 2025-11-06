package com.stocktracker.dao;

import java.sql.*;
import java.util.*;
import com.stocktracker.model.Stock;
import com.stocktracker.util.DBConnection;

public class TransactionDAO {

    // ✅ Record a new stock purchase
    public boolean buyStock(int userId, String symbol, double price, int quantity) {
        String sql = "INSERT INTO transactions (user_id, symbol, price, quantity, date) VALUES (?, ?, ?, ?, NOW())";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, symbol);
            ps.setDouble(3, price);
            ps.setInt(4, quantity);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Get all stocks owned by a user with total quantity
    public List<Stock> getUserPortfolio(int userId) {
        List<Stock> portfolio = new ArrayList<>();
        String sql = "SELECT symbol, SUM(quantity) AS total_qty, AVG(price) AS avg_price " +
                     "FROM transactions WHERE user_id = ? GROUP BY symbol";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Stock s = new Stock();
                s.setSymbol(rs.getString("symbol"));
                s.setQuantity(rs.getInt("total_qty"));
                s.setPrice(rs.getDouble("avg_price"));
                portfolio.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return portfolio;
    }


    public void buyStock(int userId, String symbol, double price) {
        String sql = "INSERT INTO transactions (user_id, symbol, price, quantity, timestamp) VALUES (?, ?, ?, 1, NOW())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, symbol);
            ps.setDouble(3, price);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean hasUserBoughtStock(int userId, String symbol) {
        String sql = "SELECT COUNT(*) FROM transactions WHERE user_id = ? AND symbol = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, symbol);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addTransaction(int userId, String symbol, double price) {
        String sql = "INSERT INTO transactions (user_id, symbol, price, timestamp) VALUES (?, ?, ?, NOW())";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, symbol);
            ps.setDouble(3, price);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getUserSymbols(int userId) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT symbol FROM transactions WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("symbol"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean addTransaction(int userId, Stock stock) {
        String sql = "INSERT INTO transactions (user_id, symbol, price, quantity) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, stock.getSymbol());
            stmt.setDouble(3, stock.getPrice());
            stmt.setInt(4, stock.getQuantity());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    
}
