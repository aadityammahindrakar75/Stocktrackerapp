package com.stocktracker.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.stocktracker.model.Stock;

public class StockDAO {

    private Connection connection;

    public StockDAO() {
        try {
            Properties props = new Properties();
            InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties");
            props.load(input);

            Class.forName(props.getProperty("db.driver"));
            connection = DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.username"),
                    props.getProperty("db.password")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Add a new stock (with quantity)
    public boolean addStock(Stock stock) {
        String sql = "INSERT INTO stocks(symbol, name, price, change_value, percentage_change, quantity) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, stock.getSymbol());
            ps.setString(2, stock.getName());
            ps.setDouble(3, stock.getPrice());
            ps.setDouble(4, stock.getChange());
            ps.setDouble(5, stock.getPercentageChange());
            ps.setInt(6, stock.getQuantity()); // ✅ new
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Get all stocks (with quantity)
    public List<Stock> getAllStocks() {
        List<Stock> list = new ArrayList<>();
        String sql = "SELECT * FROM stocks";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Stock s = new Stock(
                        rs.getInt("id"),
                        rs.getString("symbol"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getDouble("change_value"),
                        rs.getDouble("percentage_change"),
                        rs.getInt("quantity"), // ✅ new
                        false // alreadyOwned default false here
                );
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ Get a stock by its symbol
    public Stock getStockBySymbol(String symbol) {
        String sql = "SELECT * FROM stocks WHERE symbol=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, symbol);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Stock(
                        rs.getInt("id"),
                        rs.getString("symbol"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getDouble("change_value"),
                        rs.getDouble("percentage_change"),
                        rs.getInt("quantity"), // ✅ new
                        false
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Optional: update stock quantity (e.g., user buys more)
    public boolean updateStockQuantity(String symbol, int newQuantity) {
        String sql = "UPDATE stocks SET quantity=? WHERE symbol=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setString(2, symbol);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
