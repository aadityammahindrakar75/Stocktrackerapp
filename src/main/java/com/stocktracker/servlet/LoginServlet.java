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
            // input validation failed: set error message and forward back to login page
            resp.getWriter().println("<script type='text/javascript'>");
            resp.getWriter().println("alert('Please enter both email and password.');");
            resp.getWriter().println("window.location='login.html';");
            resp.getWriter().println("</script>");
            return;
        }

        User user = userDAO.login(email, password); // returns null if invalid
        if (user != null) {
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);

            resp.getWriter().println("<script type='text/javascript'>");
            resp.getWriter().println("alert('Login Successful!');");
            resp.getWriter().println("window.location='dashboard.jsp';");
            resp.getWriter().println("</script>");
        } else {
             resp.getWriter().println("<script type='text/javascript'>");
            resp.getWriter().println("alert('Invalid email or password. Please try again.');");
            resp.getWriter().println("window.location='login.html';");
            resp.getWriter().println("</script>");
        }
    }
}
