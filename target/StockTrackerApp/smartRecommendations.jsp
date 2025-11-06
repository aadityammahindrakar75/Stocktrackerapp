<%@ page import="java.util.*, com.stocktracker.model.Stock" %>
<!DOCTYPE html>
<html>
<head>
    <title>Smart Recommendations</title>
    <style>
        body { font-family: Arial; background: #f4f6f9; padding: 20px; }
        h2 { color: #004aad; }
        table { width: 100%; border-collapse: collapse; margin-bottom: 30px; background: white; }
        th, td { border: 1px solid #ddd; padding: 10px; text-align: center; }
        th { background-color: #004aad; color: white; }
        button { background-color: #004aad; color: white; border: none; padding: 8px 16px; border-radius: 5px; }
        button:hover { background-color: #0066ff; }
    </style>
</head>
<body>
    <h2>Personalized Recommendations</h2>
    <jsp:include page="partials/stockTable.jsp">
        <jsp:param name="stocks" value="${personalized}" />
    </jsp:include>

    <h2>Trending Stocks</h2>
    <jsp:include page="partials/stockTable.jsp">
        <jsp:param name="stocks" value="${trending}" />
    </jsp:include>

    <h2>Discover More</h2>
    <jsp:include page="partials/stockTable.jsp">
        <jsp:param name="stocks" value="${randomSuggestions}" />
    </jsp:include>
</body>
</html>
