<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Make a Reservation</title>
</head>
<body>
    <h1>Make a Reservation</h1>
   <form action="makeAReservation" method="post">
       <label for="workspaceId">Workspace:</label>
       <select id="workspaceId" name="workspaceId" required>
           <c:forEach var="workspace" items="${availableWorkspaces}">
               <option value="${workspace.id}">${workspace.type} - $${workspace.price}</option>
           </c:forEach>
       </select><br><br>

       <label for="userName">Your Name:</label>
       <input type="text" id="userName" name="userName" required><br><br>

       <label for="startDate">Start Date:</label>
       <input type="date" id="startDate" name="startDate" required><br><br>

       <label for="endDate">End Date:</label>
       <input type="date" id="endDate" name="endDate" required><br><br>

       <button type="submit">Make Reservation</button>
   </form>
</body>
</html>
