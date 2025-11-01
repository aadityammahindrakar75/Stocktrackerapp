package com.stocktracker.servlet;

import java.io.IOException;
import java.util.List;

import com.stocktracker.dao.WatchlistDAO;
import com.stocktracker.dao.WatchlistItem;
import com.stocktracker.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/watchlist")
public class WatchlistServlet extends HttpServlet {

    private WatchlistDAO watchlistDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        watchlistDAO = new WatchlistDAO();
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
        List<WatchlistItem> items = watchlistDAO.getUserWatchlist(user.getId());
        req.setAttribute("watchlist", items);
        req.getRequestDispatcher("/watchlist.jsp").forward(req, resp);
    }

    /**
     * Handles add/remove operations.
     * POST params:
     *   action=add  -> symbol (required)
     *   action=remove -> symbol (required)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.html");
            return;
        }

        User user = (User) session.getAttribute("user");
        String action = req.getParameter("action");
        String symbol = req.getParameter("symbol");

        if (action == null || symbol == null || symbol.trim().isEmpty()) {
            req.setAttribute("error", "Invalid request.");
            doGet(req, resp);
            return;
        }

        symbol = symbol.trim().toUpperCase();

        switch (action) {
            case "add":
                boolean added = watchlistDAO.addToWatchlist(user.getId(), symbol);
                if (!added) {
                    req.setAttribute("error", "Could not add symbol to watchlist (maybe already present).");
                }
                resp.sendRedirect(req.getContextPath() + "/watchlist");
                break;

            case "remove":
                boolean removed = watchlistDAO.removeFromWatchlist(user.getId(), symbol);
                if (!removed) {
                    req.setAttribute("error", "Could not remove symbol from watchlist.");
                }
                resp.sendRedirect(req.getContextPath() + "/watchlist");
                break;

            default:
                req.setAttribute("error", "Unknown action.");
                doGet(req, resp);
        }
    }
}
