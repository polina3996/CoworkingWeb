<!DOCTYPE html>
<html lang="en">
<head>
    <title>Browse Workspaces</title>
</head>
<body>
    <h1>Available Coworking Spaces</h1>

    <c:if test="${not empty errorMessage}">
        <p style="color: red;">${errorMessage}</p>
    </c:if>

    <ul>
        <c:forEach var="workspace" items="${workspaces}">
            <li>${workspace}</li>
        </c:forEach>
    </ul>
</body>
</html>
