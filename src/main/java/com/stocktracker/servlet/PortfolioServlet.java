package com.stocktracker.servlet;

import com.stocktracker.model.Stock;
import com.stocktracker.model.User;
import com.stocktracker.util.DBConnection;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/portfolio")
public class PortfolioServlet extends HttpServlet {

    // 🔹 Fetch real-time price using Yahoo Finance API
    private double getLivePrice(String symbol) {
        try {
            String apiUrl = "https://query1.finance.yahoo.com/v7/finance/quote?symbols=" + symbol;
            HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject json = new JSONObject(response.toString());
            return json.getJSONObject("quoteResponse")
                       .getJSONArray("result")
                       .getJSONObject(0)
                       .getDouble("regularMarketPrice");

        } catch (Exception e) {
            System.out.println("Error fetching price for " + symbol + ": " + e.getMessage());
            return 0.0;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }

        List<Stock> portfolio = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT symbol, name, price FROM transactions WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String symbol = rs.getString("symbol");
                String name = rs.getString("name");
                double buyPrice = rs.getDouble("price");

                double currentPrice = getLivePrice(symbol);
                double change = currentPrice - buyPrice;
                double percentageChange = ((change) / buyPrice) * 100;
                portfolio.add(new Stock(symbol, name, currentPrice, change, percentageChange, 0, true));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("portfolio", portfolio);
        request.getRequestDispatcher("portfolio.jsp").forward(request, response);
    }
}
