<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<script type="text/javascript" src="static/chat.js"></script>
		<link rel="stylesheet" href="static/chat.css">
		<title>Chat</title>
	</head>
	<body>
		<div id="container">
			<input type="hidden" id="username" value="${it}"> 
			<div id="header">Welcome to the chat, ${it}</div>
			<div id="msg"></div>
			<div id="userList"></div>
			<div id="input">
				<input id="inputField" type="text"/>
				<button id="sendBtn" onclick="sendMsg()">Send</button>
			</div>
		</div>
	</body>
</html>