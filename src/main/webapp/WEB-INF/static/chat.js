window.addEventListener("load", connectWebsocket, false);

let ws;

function connectWebsocket() {
	let username = document.getElementById("username").value;
    ws = new WebSocket("ws://localhost:8080/websocket_chat/websocket/chat/" + username);
    ws.onopen = onOpen;
    ws.onclose = onClose;
    ws.onmessage = onMessage;
}

/* 
 * Event onMessage, when user (cient) receives a message from server.
 * If the massage is a user message, the chat messages will be updated,
 * if the message is a user list, the user list of the chat will be updated.
 */
function onMessage(evt) {
    let obj = JSON.parse(evt.data);
    if(obj.isMsg == true) {
        chatMsg(obj);
    } else {
        updateUsersList(obj);
    }
}

function chatMsg(obj) {
    let time = new Date(obj.time).toLocaleTimeString("de-DE", {timeZone: "Europe/Berlin"});
    let divMsg = document.getElementById("msg");
    let newMsgNode = document.createElement("p");
    let msg = null;
    if(obj.content.substring(0, 1) == "@") {
		msg = "<b style='color:#946565;'>" + obj.content + "</b>";
	} else {
		msg = obj.content;
	}
    newMsgNode.innerHTML = "[" + time + "] <b>" + obj.userName + "</b>: " + msg;
    divMsg.appendChild(newMsgNode);
}

function updateUsersList(obj) {
    let divUserList = document.getElementById("userList");
    while(divUserList.firstChild) {
        divUserList.removeChild(divUserList.lastChild);
    }
    for(s of obj.users) {
        let userNode = document.createElement("p");
        userNode.innerHTML = s;
        divUserList.appendChild(userNode);
    }
}

/*
* Event on WebSocket opened.
*/
function onOpen() {
    let div = document.getElementById("msg");
    let newMsgNode = document.createElement("p");
    newMsgNode.style.fontSize = "xx-small"; 
    newMsgNode.innerHTML = "Connected to the chat";
    div.appendChild(newMsgNode);
}

function onClose(e) {
    let div = document.getElementById("msg");
    let newMsgNode = document.createElement("p");
    newMsgNode.style.fontSize = "xx-small";
    newMsgNode.style.color = "red";
    newMsgNode.innerHTML = "Connection closed. Error code " + e.code + ". Reason: " + e.reason;
    div.innerHTML = "";
    div.appendChild(newMsgNode);
}

function sendMsg() {
    let inputField = document.getElementById("inputField");
    let msg = inputField.value;
    //if no message to send, the random number will be sent to the server.
    if(msg == "") {
		msg = Math.round(Math.random() * 100);
	}
    inputField.value = "";
    ws.send(msg);
}