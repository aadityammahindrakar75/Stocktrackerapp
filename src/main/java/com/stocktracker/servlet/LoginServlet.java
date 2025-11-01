package com.stocktracker.servlet;

import java.io.IOException;

import com.stocktracker.dao.UserDAO;
import com.stocktracker.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Forward to login page
        req.getRequestDispatcher("/login.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            req.setAttribute("error", "Please enter email and password.");
            req.getRequestDispatcher("/login.html").forward(req, resp);
            return;
        }

        User user = userDAO.login(email, password); // returns null if invalid
        if (user != null) {
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            // optional: set session timeout in seconds
            // session.setMaxInactiveInterval(30*60);
            resp.sendRedirect(req.getContextPath() + "/dashboard.jsp");
        } else {
            req.setAttribute("error", "Invalid email or password.");
            req.getRequestDispatcher("/login.html").forward(req, resp);
        }
    }
}
