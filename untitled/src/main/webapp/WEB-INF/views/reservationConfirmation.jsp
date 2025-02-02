<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Reservation Confirmation</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f9f9f9;
        }
        h1 {
            color: #4CAF50;
        }
        .message {
            margin: 20px 0;
            padding: 10px;
            background-color: #e8f5e9;
            border: 1px solid #4CAF50;
            border-radius: 4px;
            color: #2e7d32;
        }
        a {
            display: inline-block;
            margin-top: 20px;
            text-decoration: none;
            color: #007BFF;
        }
        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <h1>Reservation Confirmed</h1>

    <c:if test="${not empty message}">
        <div class="message">
            <p>${message}</p>
        </div>
    </c:if>

    <p>Thank you for making a reservation. If you have any questions, feel free to contact us!</p>

    <a href="${pageContext.request.contextPath}/">Return to Home</a>
</body>
</html>
