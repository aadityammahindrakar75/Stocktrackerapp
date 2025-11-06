<%@ page import="java.util.*, com.stocktracker.model.Stock, com.stocktracker.model.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.html");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Recommendations | StockTracker</title>
    <style>
        body {
            font-family: 'Segoe UI', Arial, sans-serif;
            background: #f4f6f9;
            padding: 30px;
        }
        h2 {
            color: #004aad;
            margin-bottom: 20px;
        }
        table {
            border-collapse: collapse;
            width: 100%;
            background: white;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        th, td {
            padding: 14px;
            border: 1px solid #ddd;
            text-align: center;
        }
        th {
            background: #004aad;
            color: white;
        }
        form {
            display: inline;
        }
        input[type="number"] {
            width: 60px;
            padding: 4px;
            text-align: center;
            border-radius: 5px;
            border: 1px solid #ccc;
        }
        button {
            background: #004aad;
            color: white;
            border: none;
            padding: 6px 12px;
            border-radius: 5px;
            cursor: pointer;
        }
        button:hover {
            background: #0066ff;
        }
        .owned {
            color: gray;
            font-weight: bold;
        }
    </style>
</head>
<body>

    <h2>📈 Recommended Stocks for You</h2>

    <table>
        <tr>
            <th>Symbol</th>
            <th>Company Name</th>
            <th>Price</th>
            <th>Change</th>
            <th>Buy</th>
        </tr>

        <c:forEach var="s" items="${recommendedStocks}">
    <tr>
        <td>${s.symbol}</td>
        <td>${s.name}</td>
        <td>$${s.price}</td>
        <td>
            <c:choose>
                <c:when test="${s.change >= 0}">
                    <span style="color:green;">+${s.change} (${s.percentageChange}%)</span>
                </c:when>
                <c:otherwise>
                    <span style="color:red;">${s.change} (${s.percentageChange}%)</span>
                </c:otherwise>
            </c:choose>
        </td>
        <td>
            <c:choose>
                <c:when test="${s.alreadyOwned}">
                    <span class="owned">✅ Owned</span>
                </c:when>
                <c:otherwise>
                    <form action="buyStock" method="post">
                        <input type="hidden" name="symbol" value="${s.symbol}">
                        <input type="hidden" name="price" value="${s.price}">
                        <input type="number" name="quantity" value="1" min="1" max="100">
                        <button type="submit">Buy</button>
                    </form>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
</c:forEach>

    </table>

</body>
</html>
