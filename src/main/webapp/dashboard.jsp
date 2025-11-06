<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.stocktracker.model.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // Validate user session
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.html");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard | StockTrackerApp</title>

    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f6f9;
            color: #333;
        }

        header {
            background-color: #004aad;
            color: white;
            padding: 20px 0;
            text-align: center;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
        }

        header h1 {
            margin: 0;
            font-size: 26px;
            letter-spacing: 0.5px;
        }

        nav {
            margin-top: 10px;
        }

        nav a {
            color: white;
            text-decoration: none;
            margin: 0 15px;
            font-weight: 500;
            transition: color 0.3s;
        }

        nav a:hover {
            color: #ffcc00;
        }

        main {
            padding: 30px;
            max-width: 1100px;
            margin: 0 auto;
        }

        section {
            background: white;
            padding: 25px;
            margin-bottom: 25px;
            border-radius: 10px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
        }

        section h2 {
            margin-top: 0;
            color: #004aad;
            border-bottom: 2px solid #004aad;
            display: inline-block;
            padding-bottom: 5px;
        }

        form {
            display: flex;
            align-items: center;
            gap: 10px;
            margin-top: 15px;
        }

        input[type="text"] {
            flex: 1;
            padding: 10px 12px;
            border-radius: 6px;
            border: 1px solid #ccc;
            font-size: 15px;
            outline: none;
            transition: border-color 0.3s;
        }

        input[type="text"]:focus {
            border-color: #004aad;
        }

        button {
            background-color: #004aad;
            color: white;
            border: none;
            padding: 10px 18px;
            border-radius: 6px;
            cursor: pointer;
            font-size: 15px;
            transition: background-color 0.3s, transform 0.2s;
        }

        button:hover {
            background-color: #005ce6;
            transform: scale(1.03);
        }

        #stock-info, #watchlist-items {
            margin-top: 15px;
            padding: 10px;
            border: 1px solid #ddd;
            background-color: #fafafa;
            border-radius: 6px;
            min-height: 60px;
        }

        @media (max-width: 768px) {
            main {
                padding: 20px;
            }

            form {
                flex-direction: column;
                align-items: stretch;
            }

            nav a {
                display: inline-block;
                margin: 5px 10px;
            }
        }
    </style>
</head>
<body>
<header>
    <h1>Welcome, <%= user.getUsername() %>!</h1>
    <nav>
        <a href="dashboard.jsp">Dashboard</a>
        <a href="watchlist.jsp">My Watchlist</a>
        <a href="recommendations.jsp">Recommendations</a>
        <a href="portfolio.jsp">My Portfolio</a>
        <a href="smartRecommendations.jsp">Smart Recommendations</a>
        <a href="logout" onclick="return confirmLogout();">Logout</a>
    </nav>
</header>

<script>
    function confirmLogout() {
        return confirm("Are you sure you want to log out?");
    }
</script>

<main>
    <!-- Stock Search -->
    <section class="search-section">
        <h2>Search Stocks</h2>
        <form action="StockServlet" method="get">
            <input type="text" name="symbol" placeholder="Enter Stock Symbol (e.g. AAPL)" required>
            <button type="submit">Search</button>
        </form>
    </section>

    <!-- Stock Details -->
    <section class="stock-section">
        <h2>Stock Details</h2>
        <div id="stock-info">
            <c:if test="${not empty error}">
                <p style="color:red;">${error}</p>
            </c:if>

            <c:if test="${not empty stock}">
                <p><strong>Symbol:</strong> ${stock.symbol}</p>
                <p><strong>Company:</strong> ${stock.name}</p>
                <p><strong>Price:</strong> $${stock.price}</p>
                <p><strong>Change:</strong>
                    <c:choose>
                        <c:when test="${stock.change >= 0}">
                            <span style="color: green;">
                                ${stock.change} (${stock.percentageChange}%)
                            </span>
                        </c:when>
                        <c:otherwise>
                            <span style="color: red;">
                                ${stock.change} (${stock.percentageChange}%)
                            </span>
                        </c:otherwise>
                    </c:choose>
                </p>
            </c:if>

            <c:if test="${empty stock and empty error}">
                <p>No stock selected yet. Use the form above to search.</p>
            </c:if>
        </div>
    </section>

    <!-- Watchlist -->
    <section class="watchlist-section">
        <h2>My Watchlist</h2>
        <form action="watchlist" method="post">
            <input type="hidden" name="action" value="add">
            <input type="text" name="symbol" placeholder="Add stock to watchlist" required>
            <button type="submit">Add</button>
        </form>

        <div id="watchlist-items">
            <c:choose>
                <c:when test="${not empty watchlist}">
                    <ul>
                        <c:forEach var="item" items="${watchlist}">
                            <li>
                                ${item.symbol} - ${item.companyName}
                                <form action="watchlist" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="remove">
                                    <input type="hidden" name="symbol" value="${item.symbol}">
                                    <button type="submit">Remove</button>
                                </form>
                            </li>
                        </c:forEach>
                    </ul>
                </c:when>
                <c:otherwise>
                    <p>No items in your watchlist yet.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </section>
</main>
</body>
</html>
