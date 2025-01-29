<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Your Reservations</title>
</head>
<body>
    <h1>Reservations for ${userName}</h1>

    <c:if test="${not empty reservations}">
        <table>
            <tr>
                <th>Workspace</th>
                <th>Start Date</th>
                <th>End Date</th>
                <th>Date of creation</th>
            </tr>
            <c:forEach var="reservation" items="${reservations}">
                <tr>
                    <td>${reservation.workspace}</td>
                    <td>${reservation.start}</td>
                    <td>${reservation.end}</td>
                    <td>${reservation.date}</td>
                <td>
                    <form action="cancelMyReservation" method="post">
                    <input type="hidden" name="id" value="${reservation.id}">
                    <button type="submit">Cancel</button>
                    </form>
                </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>

    <c:if test="${empty reservations}">
        <p>No reservations found.</p>
    </c:if>
</body>
</html>
