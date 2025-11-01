<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.stocktracker.util.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login.html");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Dashboard - StockTracker</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 20px; }
        .top { display:flex; justify-content:space-between; align-items:center; }
        table { width:100%; border-collapse:collapse; margin-top:12px; }
        th, td { border:1px solid #ddd; padding:8px; text-align:left; }
        th { background:#f2f2f2; }
        .actions a { margin-right:10px; }
    </style>
</head>
<body>
    <div class="top">
        <h2>Welcome, ${user.username}</h2>
        <div class="actions">
            <a href="${pageContext.request.contextPath}/watchlist">Watchlist</a>
            <a href="${pageContext.request.contextPath}/stocks">My Stocks</a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </div>
    </div>

    <h3>Your Portfolio</h3>

    <c:if test="${not empty error}">
        <p style="color:red">${error}</p>
    </c:if>

    <!-- Add stock form -->
    <form method="post" action="${pageContext.request.contextPath}/stocks" style="margin-bottom:12px;">
        <label>Symbol: <input type="text" name="symbol" required maxlength="10"/></label>
        <label>Quantity: <input type="number" name="quantity" required min="1" style="width:90px"/></label>
        <label>Buy Price: <input type="text" name="buyPrice" required style="width:100px"/></label>
        <button type="submit">Add Stock</button>
    </form>

    <table>
        <thead>
            <tr>
                <th>Symbol</th>
                <th>Quantity</th>
                <th>Buy Price</th>
                <th>Current Price</th>
                <th>Unrealized P/L</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="s" items="${stocks}">
                <tr>
                    <td>${s.symbol}</td>
                    <td>${s.quantity}</td>
                    <td>${s.buyPrice}</td>
                    <td><c:out value="${s.currentPrice}" default="0.00"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty s.currentPrice}">
                                ${ (s.currentPrice - s.buyPrice) * s.quantity }
                            </c:when>
                            <c:otherwise>—</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty stocks}">
                <tr><td colspan="5">You have no stocks yet. Add one above.</td></tr>
            </c:if>
        </tbody>
    </table>

    <p style="margin-top:16px;"><a href="${pageContext.request.contextPath}/watchlist.jsp">Manage Watchlist</a></p>
</body>
</html>
