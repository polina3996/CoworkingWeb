<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Reservations</title>
</head>
<body>
<h1>All Reservations</h1>

<c:if test="${not empty success}">
    <p style="color: green;">${success}</p>
</c:if>
<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>

<table border="1">
    <tr>
        <th>ID</th>
        <th>User</th>
        <th>Workspace</th>
        <th>Start Date</th>
        <th>End Date</th>
        <th>Date of Creation</th>
    </tr>
    <c:forEach var="reservation" items="${reservations}">
        <tr>
            <td>${reservation.id}</td>
            <td>${reservation.user}</td>
            <td>${reservation.workspace}</td>
            <td>${reservation.start}</td>
            <td>${reservation.end}</td>
            <td>${reservation.date}</td>
            <td>
                <form action="removeReservation" method="post">
                    <input type="hidden" name="id" value="${reservation.id}">
                    <button type="submit">Cancel</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
