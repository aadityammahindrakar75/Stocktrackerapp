<%@ page import="java.util.*, com.stocktracker.model.Stock, com.stocktracker.dao.TransactionDAO, com.stocktracker.model.User" %>

<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.html");
        return;
    }

    TransactionDAO dao = new TransactionDAO();
    List<Stock> portfolio = dao.getUserPortfolio(user.getId());
%>

<!DOCTYPE html>
<html>
<head>
    <title>My Portfolio | StockTracker</title>
    <style>
        body { font-family: Arial; background: #f4f6f9; padding: 20px; }
        table { border-collapse: collapse; width: 100%; background: white; }
        th, td { padding: 12px; border: 1px solid #ddd; text-align: center; }
        th { background: #004aad; color: white; }
    </style>
</head>
<body>
    <h2>Your Purchased Stocks</h2>
    <table>
        <tr><th>Symbol</th><th>Average Price</th><th>Quantity</th></tr>
        <%
            for (Stock s : portfolio) {
        %>
            <tr>
                <td><%= s.getSymbol() %></td>
                <td>$<%= s.getPrice() %></td>
                <td><%= s.getQuantity() %></td>
            </tr>
        <%
            }
        %>
    </table>
</body>
</html>
