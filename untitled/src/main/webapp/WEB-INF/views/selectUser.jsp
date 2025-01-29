<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Select User</title>
</head>
<body>
    <h1>Select Your Name</h1>

    <form action="myReservations" method="POST">
        <label for="userName">Select your name:</label>
        <select id="userName" name="name">
            <c:forEach var="user" items="${users}">
                <option value="${user.name}">${user.name}</option>
            </c:forEach>
        </select>
        <button type="submit">View Reservations</button>
    </form>
</body>
</html>
