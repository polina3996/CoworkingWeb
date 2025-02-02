<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Update Workspace</title>
</head>
<body>
    <h1>Update Coworking Space</h1>

    <c:if test="${not empty errorMessage}">
        <p style="color: red;">${errorMessage}</p>
    </c:if>

    <c:if test="${not empty successMessage}">
        <p style="color: green;">${successMessage}</p>
    </c:if>

    <form action="updateWorkspace" method="post">
        <label for="id">Select Workspace to Update (ID):</label>
        <select name="id" id="id">
            <c:forEach var="workspace" items="${workspaces}">
                <option value="${workspace.id}">${workspace.id} - ${workspace.type} - ${workspace.price}</option>
            </c:forEach>
        </select>
         <label for="type">New Type:</label>
         <input type="text" id="type" name="type" required><br><br>

         <label for="price">New Price ($):</label>
         <input type="number" id="price" name="price" step="0.01" required><br><br>

        <button type="submit">Update</button>
    </form>
</body>
</html>
