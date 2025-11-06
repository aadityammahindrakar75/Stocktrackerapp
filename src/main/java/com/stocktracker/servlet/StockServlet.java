package com.stocktracker.servlet;

import com.stocktracker.model.Stock;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.json.JSONObject;

import java.io.*;
import java.net.*;

@WebServlet("/StockServlet")
public class StockServlet extends HttpServlet {
    private static final String API_KEY = "Z1S7WHU6NLKRWSXZ"; // Your Alpha Vantage API Key

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String symbol = request.getParameter("symbol");
        if (symbol == null || symbol.trim().isEmpty()) {
            request.setAttribute("error", "Please enter a stock symbol.");
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            return;
        }

        try {
            // 1️⃣ Fetch Real-Time Stock Data
            String apiUrl = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="
                    + URLEncoder.encode(symbol.trim(), "UTF-8")
                    + "&apikey=" + API_KEY;

            URL url = new URL(apiUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder jsonResponse = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                jsonResponse.append(line);
            reader.close();

            JSONObject json = new JSONObject(jsonResponse.toString());

            // Check if "Global Quote" exists (API can return an empty response)
            if (!json.has("Global Quote") || json.getJSONObject("Global Quote").isEmpty()) {
                request.setAttribute("error", "No data found for symbol: " + symbol.toUpperCase());
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                return;
            }

            JSONObject quote = json.getJSONObject("Global Quote");

            // 2️⃣ Extract Stock Data Safely
            String companySymbol = quote.optString("01. symbol", symbol.toUpperCase());
            double price = parseDoubleSafe(quote.optString("05. price"));
            double change = parseDoubleSafe(quote.optString("09. change"));
            double percentChange = parseDoubleSafe(
                    quote.optString("10. change percent").replace("%", "")
            );

            // 3️⃣ Populate Stock Object
            Stock stock = new Stock(0, companySymbol, "N/A", price, change, percentChange);

            // 4️⃣ Set Attribute for JSP
            request.setAttribute("stock", stock);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error fetching stock data. Please try again later.");
        }

        // Forward back to dashboard.jsp
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    // Helper method to safely parse double values
    private double parseDoubleSafe(String value) {
        try {
            return Double.parseDouble(value.trim());
        } catch (Exception e) {
            return 0.0;
        }
    }
}
