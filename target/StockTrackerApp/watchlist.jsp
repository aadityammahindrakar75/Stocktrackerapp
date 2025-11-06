<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.stocktracker.model.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    // Session validation
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login.html");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Watchlist | StockTrackerApp</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, sans-serif;
            background-color: #f4f6f9;
            margin: 0;
            padding: 0;
        }

        header {
            background-color: #004aad;
            color: white;
            padding: 20px;
            text-align: center;
        }

        header h1 {
            margin: 0;
            font-size: 24px;
        }

        nav a {
            color: white;
            margin: 0 15px;
            text-decoration: none;
            font-weight: 500;
        }

        nav a:hover {
            color: #ffcc00;
        }

        main {
            max-width: 900px;
            margin: 30px auto;
            background: white;
            border-radius: 10px;
            box-shadow: 0 3px 8px rgba(0, 0, 0, 0.1);
            padding: 25px;
        }

        h2 {
            color: #004aad;
            border-bottom: 2px solid #004aad;
            padding-bottom: 6px;
        }

        form {
            margin-top: 15px;
            display: flex;
            gap: 10px;
        }

        input[type="text"] {
            flex: 1;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 15px;
        }

        button {
            background-color: #004aad;
            color: white;
            border: none;
            padding: 10px 16px;
            border-radius: 6px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        button:hover {
            background-color: #005ce6;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: center;
        }

        th {
            background-color: #f2f2f2;
            color: #004aad;
        }

        .remove-btn {
            background-color: #e60000;
            color: white;
            border: none;
            padding: 6px 10px;
            border-radius: 4px;
            cursor: pointer;
        }

        .remove-btn:hover {
            background-color: #cc0000;
        }

        .empty-msg {
            text-align: center;
            color: #555;
            margin-top: 20px;
        }
    </style>
</head>
<body>
<header>
    <h1>Welcome, ${user.username}</h1>
    <nav>
        <a href="dashboard.jsp">Dashboard</a>
        <a href="watchlist.jsp">My Watchlist</a>
        <a href="logout">Logout</a>
    </nav>
</header>

<main>
    <h2>Your Watchlist</h2>

    <!-- Add Stock Form -->
    <form id="addStockForm">
        <input type="hidden" name="action" value="add">
        <input type="text" id="symbolInput" name="symbol" placeholder="Enter stock symbol (e.g. AAPL)" required>
        <button type="submit">Add Stock</button>
    </form>

    <!-- Watchlist Table -->
    <div id="watchlistContainer">
        <c:choose>
            <c:when test="${not empty watchlist}">
                <table id="watchlistTable">
                    <thead>
                        <tr>
                            <th>Stock Symbol</th>
                            <th>Company</th>
                            <th>Latest Price</th>
                            <th>Added On</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${watchlist}">
                            <tr>
                                <td>${item.symbol}</td>
                                <td>${item.companyName != null ? item.companyName : 'N/A'}</td>
                                <td>${item.latestPrice}</td>
                                <td>${item.addedAtFormatted}</td>
                                <td>
                                    <button class="remove-btn" data-symbol="${item.symbol}">Remove</button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p class="empty-msg">You have no stocks in your watchlist yet. Add one above!</p>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<!-- JavaScript for AJAX -->
<script>
document.addEventListener("DOMContentLoaded", function() {

    // Add Stock via AJAX
    document.getElementById("addStockForm").addEventListener("submit", async function(event) {
        event.preventDefault();
        const formData = new FormData(this);
        const symbol = document.getElementById("symbolInput").value.trim();

        if (!symbol) {
            alert("Please enter a stock symbol!");
            return;
        }

        const response = await fetch("watchlist", {
            method: "POST",
            body: formData
        });
        const text = await response.text();

        alert("Stock added (refreshing list)!");
        location.reload(); // reload to update table
    });

    // Remove stock via AJAX
    document.addEventListener("click", async function(e) {
        if (e.target.classList.contains("remove-btn")) {
            const symbol = e.target.getAttribute("data-symbol");

            if (!confirm("Are you sure you want to remove " + symbol + " from your watchlist?")) return;

            const formData = new FormData();
            formData.append("action", "remove");
            formData.append("symbol", symbol);

            const response = await fetch("watchlist", {
                method: "POST",
                body: formData
            });

            const text = await response.text();
            alert("Stock removed successfully!");
            location.reload(); // reload to refresh list
        }
    });
});
</script>

</body>
</html>
