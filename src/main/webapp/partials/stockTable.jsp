<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Stock Tracker Dashboard</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #f8f9fc;
            color: #333;
            margin: 0;
            padding: 0;
        }

        h2 {
            text-align: center;
            margin-top: 30px;
            color: #2c3e50;
        }

        table {
            width: 80%;
            margin: 40px auto;
            border-collapse: collapse;
            background-color: #fff;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            overflow: hidden;
        }

        th, td {
            padding: 14px 18px;
            text-align: center;
        }

        th {
            background-color: #3498db;
            color: white;
            font-size: 16px;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        td {
            font-size: 15px;
        }

        .positive {
            color: green;
            font-weight: 600;
        }

        .negative {
            color: red;
            font-weight: 600;
        }

        button {
            background-color: #2ecc71;
            color: white;
            border: none;
            padding: 8px 14px;
            border-radius: 8px;
            cursor: pointer;
            font-size: 14px;
            transition: 0.3s;
        }

        button:hover {
            background-color: #27ae60;
        }

        p {
            text-align: center;
            color: #555;
            font-size: 16px;
        }
    </style>
</head>
<body>

<h2>📈 Live Stock Recommendations</h2>

<c:choose>
    <c:when test="${not empty stocks}">
        <table>
            <tr>
                <th>Symbol</th>
                <th>Price</th>
                <th>Change</th>
                <th>Buy</th>
            </tr>
            <c:forEach var="s" items="${stocks}">
                <tr>
                    <td>${s.symbol}</td>
                    <td>$${s.price}</td>
                    <td class="${s.change >= 0 ? 'positive' : 'negative'}">
                        ${s.change} (${s.percentageChange}%)
                    </td>
                    <td>
                        <form action="buyStock" method="post">
                            <input type="hidden" name="symbol" value="${s.symbol}">
                            <input type="hidden" name="price" value="${s.price}">
                            <input type="number" name="quantity" value="1" min="1" max="100" style="width:60px;">
                            <button type="submit">Buy</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:when>
    <c:otherwise>
        <p>No stock data available at the moment.</p>
    </c:otherwise>
</c:choose>

</body>
</html>
