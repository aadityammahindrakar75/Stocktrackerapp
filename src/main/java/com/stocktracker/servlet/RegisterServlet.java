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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/registration.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirm");

        // Basic validation
        if (username == null || email == null || password == null || confirm == null ||
            username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            req.setAttribute("error", "Please fill all fields.");
            req.getRequestDispatcher("/registration.html").forward(req, resp);
            return;
        }

        if (!password.equals(confirm)) {
            req.setAttribute("error", "Passwords do not match.");
            req.getRequestDispatcher("/registration.html").forward(req, resp);
            return;
        }

        User user = new User(0, username, email, password);
        boolean created = userDAO.registerUser(user);

        if (created) {
            User loggedIn = userDAO.login(email, password);
            HttpSession session = req.getSession(true);
            session.setAttribute("user", loggedIn);
            resp.sendRedirect(req.getContextPath() + "/dashboard.jsp");
        } else {
            req.setAttribute("error", "Registration failed. Email may already exist.");
            req.getRequestDispatcher("/registration.html").forward(req, resp);
        }
    }
}
