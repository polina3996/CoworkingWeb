<!DOCTYPE html>
<html>
<head>
    <title>Add Workspace</title>
</head>
<body>
    <h1>Add Workspace</h1>

    <form action="addWorkspace" method="post">
        <label for="type">Type:</label>
        <input type="text" id="type" name="type" required><br><br>

        <label for="price">Price ($):</label>
        <input type="number" id="price" name="price" step="0.01" required><br><br>

        <button type="submit">Add Workspace</button>
    </form>

    <a href="/coworking-spaces-1.0.0/">Back to Main Menu</a>
</body>
</html>
