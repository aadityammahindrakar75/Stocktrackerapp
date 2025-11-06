package com.stocktracker.servlet;

import com.stocktracker.model.Stock;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/recommendations")
public class RecommendationsServlet extends HttpServlet {

    // ✅ Fetch live price from Yahoo Finance API
    private double getLivePrice(String symbol) {
        try {
            String apiUrl = "https://query1.finance.yahoo.com/v7/finance/quote?symbols=" + symbol;
            HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder json = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) json.append(line);
            reader.close();

            JSONObject obj = new JSONObject(json.toString())
                    .getJSONObject("quoteResponse")
                    .getJSONArray("result")
                    .getJSONObject(0);

            return obj.getDouble("regularMarketPrice");

        } catch (Exception e) {
            System.out.println("API ERROR: " + e.getMessage());
            return 0.0;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ✅ Hardcoded recommended stocks for now (you can replace later)
        String[] symbols = {"AAPL", "MSFT", "TSLA", "GOOGL", "AMZN"};

        List<Stock> list = new ArrayList<>();

        for (String sym : symbols) {
            double livePrice = getLivePrice(sym);

            // Demo dummy values for change %
            double change = Math.random() * 5 - 2.5;
            double percent = (change / livePrice) * 100;

            list.add(new Stock(sym, sym + " Inc", livePrice, change, percent, 0, false));
        }

        req.setAttribute("stocks", list);
        req.getRequestDispatcher("stocks.jsp").forward(req, resp);
    }
}
