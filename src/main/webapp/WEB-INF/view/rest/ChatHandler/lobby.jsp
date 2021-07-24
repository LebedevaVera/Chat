<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>Insert title here</title>
</head>
<body>
	<h1>Welcome to the chat!</h1>
	<div>Enter your user name: </div>
	<form action="chat" method="post">
		<label for="username">User name:</label>
		<input type="text" id="username" name="username" required>
		<input type="submit" value="Enter chat">
	</form>
	<div style="color:red;">${it}</div>
</body>
</html>