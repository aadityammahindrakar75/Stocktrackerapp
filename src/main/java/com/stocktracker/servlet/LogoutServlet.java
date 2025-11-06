// package com.stocktracker.servlet;

// import java.io.IOException;

// import jakarta.servlet.ServletException;
// import jakarta.servlet.annotation.WebServlet;
// import jakarta.servlet.http.HttpServlet;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import jakarta.servlet.http.HttpSession;

// @WebServlet("/logout")
// public class LogoutServlet extends HttpServlet {

//     @Override
//     protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//             throws ServletException, IOException {

//         resp.setContentType("text/html");

//         HttpSession session = req.getSession(false);
//         if (session != null) {
//             session.invalidate();
//         }

//         // ✅ Use JavaScript alert + redirect
//         resp.getWriter().println("<script type='text/javascript'>");
//         resp.getWriter().println("alert('You have logged out successfully!');");
//         resp.getWriter().println("window.location='index.html';");
//         resp.getWriter().println("</script>");
//     }

//     // Optional POST support (same behavior)
//     @Override
//     protected void doPost(HttpServletRequest req, HttpServletResponse resp)
//             throws ServletException, IOException {
//         doGet(req, resp);
//     }
// }
package com.stocktracker.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html;charset=UTF-8");

        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // ✅ Alert + redirect to index.html safely
        resp.getWriter().println("<script type='text/javascript'>");
        resp.getWriter().println("alert('You have logged out successfully!');");
        resp.getWriter().println("window.location='" + req.getContextPath() + "/index.html';");
        resp.getWriter().println("</script>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}

