<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Remove Workspace</title>
</head>
<body>
    <h1>Remove Coworking Space</h1>

    <c:if test="${not empty errorMessage}">
        <p style="color: red;">${errorMessage}</p>
    </c:if>

    <c:if test="${not empty successMessage}">
        <p style="color: green;">${successMessage}</p>
    </c:if>

    <form action="removeWorkspace" method="post">
        <label for="id">Select Workspace to Remove (ID):</label>
        <select name="id" id="id">
            <c:forEach var="workspace" items="${workspaces}">
                <option value="${workspace.id}">${workspace.id} - ${workspace.type}</option>
            </c:forEach>
        </select>
        <button type="submit">Remove</button>
    </form>
</body>
</html>
