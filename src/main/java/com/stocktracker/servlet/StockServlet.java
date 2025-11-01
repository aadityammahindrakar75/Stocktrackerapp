package com.stocktracker.servlet;

import java.io.IOException;
import java.util.List;

import com.stocktracker.dao.StockDAO;
import com.stocktracker.model.Stock;
import com.stocktracker.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/stocks")
public class StockServlet extends HttpServlet {

    private StockDAO stockDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        stockDAO = new StockDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.html");
            return;
        }

        User user = (User) session.getAttribute("user");

        // fetch all stocks for this user
        List<Stock> stocks = java.util.Collections.emptyList();
        req.setAttribute("stocks", stocks);
        req.getRequestDispatcher("/dashboard.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.html");
            return;
        }

        User user = (User) session.getAttribute("user");

        String symbol = req.getParameter("symbol");
        String quantityStr = req.getParameter("quantity");
        String buyPriceStr = req.getParameter("buyPrice");

        if (symbol == null || symbol.isEmpty() ||
            quantityStr == null || buyPriceStr == null) {
            req.setAttribute("error", "Please fill all fields.");
            req.getRequestDispatcher("/dashboard.jsp").forward(req, resp);
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityStr);
            double buyPrice = Double.parseDouble(buyPriceStr);

            // Provide default values for missing constructor arguments
            Stock stock = new Stock(
                user.getId(),
                symbol.toUpperCase(),
                String.valueOf(quantity),
                buyPrice,
                0.0, // default value for currentPrice
                0.0  // default value for profitLoss
            );
            boolean added = stockDAO.addStock(stock);

            if (added) {
                resp.sendRedirect(req.getContextPath() + "/stocks");
            } else {
                req.setAttribute("error", "Failed to add stock. Try again.");
                req.getRequestDispatcher("/dashboard.jsp").forward(req, resp);
            }

        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid quantity or price.");
            req.getRequestDispatcher("/dashboard.jsp").forward(req, resp);
        }
    }
}
