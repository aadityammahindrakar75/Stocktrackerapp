// package com.stocktracker.servlet;

// import jakarta.servlet.*;
// import jakarta.servlet.http.*;
// import jakarta.servlet.annotation.*;
// import org.json.JSONObject;
// import org.json.JSONArray;
// import java.io.*;
// import java.net.*;
// import java.util.*;

// import com.stocktracker.model.Stock;

// @WebServlet("/recommendations")
// public class StockRecommendationServlet extends HttpServlet {
//     private static final String API_KEY = "Z1S7WHU6NLKRWSXZ"; // Replace with your own AlphaVantage key

//     @Override
//     protected void doGet(HttpServletRequest request, HttpServletResponse response)
//             throws ServletException, IOException {

//         // Example suggested stocks (could later be user-personalized)
//         String[] symbols = {"AAPL", "MSFT", "GOOGL", "TSLA", "AMZN"};

//         List<Stock> recommendedStocks = new ArrayList<>();

//         for (String symbol : symbols) {
//             try {
//                 String apiUrl = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="
//                         + symbol + "&apikey=" + API_KEY;

//                 URL url = new URL(apiUrl);
//                 BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
//                 StringBuilder jsonResponse = new StringBuilder();
//                 String line;
//                 while ((line = reader.readLine()) != null)
//                     jsonResponse.append(line);
//                 reader.close();

//                 JSONObject json = new JSONObject(jsonResponse.toString());
//                 JSONObject quote = json.getJSONObject("Global Quote");

//                 String companySymbol = quote.getString("01. symbol");
//                 double price = Double.parseDouble(quote.getString("05. price"));
//                 double change = Double.parseDouble(quote.getString("09. change"));
//                 double changePercent = Double.parseDouble(quote.getString("10. change percent").replace("%", ""));

//                 recommendedStocks.add(new Stock(0, companySymbol, "N/A", price, change, changePercent));

//             } catch (Exception e) {
//                 e.printStackTrace();
//             }
//         }

//         request.setAttribute("recommendedStocks", recommendedStocks);
//         request.getRequestDispatcher("recommendations.jsp").forward(request, response);
//     }
// }

package com.stocktracker.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.json.*;
import java.io.*;
import java.net.*;
import java.util.*;

import com.stocktracker.dao.TransactionDAO;
import com.stocktracker.dao.WatchlistDAO;
import com.stocktracker.model.Stock;
import com.stocktracker.model.User;

@WebServlet("/smartRecommendations")
public class SmartRecommendationServlet extends HttpServlet {
    private static final String API_KEY = "Z1S7WHU6NLKRWSXZ"; // your Alpha Vantage key

    private List<String> popularStocks = Arrays.asList(
        "AAPL","MSFT","TSLA","GOOGL","AMZN","META","NVDA","NFLX","DIS","IBM"
    );

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }

        List<Stock> personalized = new ArrayList<>();
        List<Stock> trending = new ArrayList<>();
        List<Stock> randomSuggestions = new ArrayList<>();

        // --- 1️⃣ Personalized Suggestions based on watchlist + transactions ---
        try {
            WatchlistDAO wdao = new WatchlistDAO();
            TransactionDAO tdao = new TransactionDAO();

            Set<String> interestedSymbols = new HashSet<>();
            interestedSymbols.addAll(wdao.getUserSymbols(user.getId()));
            interestedSymbols.addAll(tdao.getUserSymbols(user.getId()));

            for (String symbol : interestedSymbols) {
                Stock s = fetchStockData(symbol);
                if (s != null) personalized.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // --- 2️⃣ Trending Stocks (using AlphaVantage TOP_GAINERS_LOSERS endpoint) ---
        try {
            String apiUrl = "https://www.alphavantage.co/query?function=TOP_GAINERS_LOSERS&apikey=" + API_KEY;
            URL url = new URL(apiUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            reader.close();

            JSONObject json = new JSONObject(sb.toString());
            JSONArray topGainers = json.getJSONArray("top_gainers");

            for (int i = 0; i < Math.min(5, topGainers.length()); i++) {
                JSONObject g = topGainers.getJSONObject(i);
                trending.add(new Stock(0, g.getString("ticker"), "N/A",
                        Double.parseDouble(g.getString("price")),
                        Double.parseDouble(g.getString("change_amount")),
                        Double.parseDouble(g.getString("change_percentage").replace("%", ""))));
            }
        } catch (Exception e) {
            // fallback to static list if API limit hit
            for (String symbol : popularStocks.subList(0, 5)) {
                Stock s = fetchStockData(symbol);
                if (s != null) trending.add(s);
            }
        }

        // --- 3️⃣ Random Discovery Suggestions ---
        Collections.shuffle(popularStocks);
        for (String symbol : popularStocks.subList(0, 3)) {
            Stock s = fetchStockData(symbol);
            if (s != null) randomSuggestions.add(s);
        }

        request.setAttribute("personalized", personalized);
        request.setAttribute("trending", trending);
        request.setAttribute("randomSuggestions", randomSuggestions);

        request.getRequestDispatcher("smartRecommendations.jsp").forward(request, response);
    }

    // --- Helper method to fetch stock data ---
    private Stock fetchStockData(String symbol) {
        try {
            String apiUrl = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="
                    + symbol + "&apikey=" + API_KEY;

            URL url = new URL(apiUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line);
            reader.close();

            JSONObject json = new JSONObject(sb.toString());
            JSONObject quote = json.getJSONObject("Global Quote");

            double price = Double.parseDouble(quote.getString("05. price"));
            double change = Double.parseDouble(quote.getString("09. change"));
            double changePercent = Double.parseDouble(quote.getString("10. change percent").replace("%", ""));

            return new Stock(0, symbol, "N/A", price, change, changePercent);

        } catch (Exception e) {
            return null;
        }
    }
}
