<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Available Workspaces</title>
    <style>
        table {
            width: 80%;
            border-collapse: collapse;
            margin: 20px auto;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        h1 {
            text-align: center;
        }
        .error-message {
            color: red;
            text-align: center;
        }
    </style>
</head>
<body>
    <h1>Available Workspaces</h1>

    <c:if test="${not empty errorMessage}">
        <p class="error-message">${errorMessage}</p>
    </c:if>

    <c:if test="${empty availableWorkspaces}">
        <p style="text-align: center;">No available workspaces at the moment. Please check back later!</p>
    </c:if>

    <c:if test="${not empty availableWorkspaces}">
        <table>
            <thead>
                <tr>
                    <th>Workspace ID</th>
                    <th>Type</th>
                    <th>Price</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="workspace" items="${availableWorkspaces}">
                    <tr>
                        <td>${workspace.id}</td>
                        <td>${workspace.type}</td>
                        <td>${workspace.price}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>

    <div style="text-align: center;">
        <a href="/coworking-spaces-1.0.0">Back to Main Menu
