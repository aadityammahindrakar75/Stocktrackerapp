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
    <title>My Watchlist</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 20px; }
        table { border-collapse: collapse; width: 100%; margin-top: 12px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background:#f2f2f2; }
        .error { color: red; }
        .small-form { display:inline; }
    </style>
</head>
<body>
    <h2>Watchlist — Welcome, ${user.username}</h2>

    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>

    <!-- Add symbol form -->
    <form method="post" action="${pageContext.request.contextPath}/watchlist">
        <input type="hidden" name="action" value="add"/>
        <label>
            Symbol:
            <input type="text" name="symbol" required maxlength="10" />
        </label>
        <button type="submit">Add</button>
    </form>

    <!-- Watchlist table -->
    <table>
        <thead>
            <tr>
                <th>Symbol</th>
                <th>Company</th>
                <th>Latest Price</th>
                <th>Added At</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${watchlist}">
                <tr>
                    <td>${item.symbol}</td>
                    <td><c:out value="${item.companyName}" default="—"/></td>
                    <td><c:out value="${item.latestPrice}" default="0.00"/></td>
                    <td><c:out value="${item.addedAtFormatted}" default=""/></td>
                    <td>
                        <form class="small-form" method="post" action="${pageContext.request.contextPath}/watchlist">
                            <input type="hidden" name="action" value="remove"/>
                            <input type="hidden" name="symbol" value="${item.symbol}"/>
                            <button type="submit" onclick="return confirm('Remove ${item.symbol} from watchlist?')">Remove</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty watchlist}">
                <tr><td colspan="5">No items in your watchlist yet.</td></tr>
            </c:if>
        </tbody>
    </table>

    <p><a href="${pageContext.request.contextPath}/dashboard.jsp">Back to Dashboard</a> | <a href="${pageContext.request.contextPath}/logout">Logout</a></p>
</body>
</html>
