package com.stocktracker.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.stocktracker.dao.WatchlistDAO;
import com.stocktracker.model.User;
import com.stocktracker.model.WatchlistItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/watchlist")
public class WatchlistServlet extends HttpServlet {

    private static final String API_KEY = "Z1S7WHU6NLKRWSXZ"; // replace with your Alpha Vantage key
    private WatchlistDAO watchlistDAO;

    @Override
    public void init() throws ServletException {
        watchlistDAO = new WatchlistDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        List<WatchlistItem> watchlist = watchlistDAO.getUserWatchlist(user.getId());
        List<WatchlistItem> updatedList = new ArrayList<>();

        // For each stock in the DB, fetch live price using Alpha Vantage
        for (WatchlistItem item : watchlist) {
            try {
                String apiUrl = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="
                        + item.getSymbol() + "&apikey=" + API_KEY;

                URL url = new URL(apiUrl);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder jsonResponse = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) jsonResponse.append(line);
                reader.close();

                JSONObject json = new JSONObject(jsonResponse.toString());
                if (json.has("Global Quote")) {
                    JSONObject quote = json.getJSONObject("Global Quote");
                    double price = Double.parseDouble(quote.optString("05. price", "0.0"));
                    item.setLatestPrice(price);
                }

                updatedList.add(item);
            } catch (Exception e) {
                e.printStackTrace();
                // Keep previous data if API fails
                updatedList.add(item);
            }
        }

        request.setAttribute("watchlist", updatedList);
        request.getRequestDispatcher("watchlist.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }  
    }
}
