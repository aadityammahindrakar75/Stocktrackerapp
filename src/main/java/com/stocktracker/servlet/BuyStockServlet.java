package com.stocktracker.servlet;

import com.stocktracker.dao.TransactionDAO;
import com.stocktracker.model.User;
import com.stocktracker.model.Stock;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/buyStock")
public class BuyStockServlet extends HttpServlet {

    private TransactionDAO transactionDAO = new TransactionDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }

        String symbol = request.getParameter("symbol");
        double price = Double.parseDouble(request.getParameter("price"));

        transactionDAO.buyStock(user.getId(), symbol, price);

        request.setAttribute("message", "Successfully bought " + symbol + "!");
        request.getRequestDispatcher("portfolio.jsp").forward(request, response);
    }
}
