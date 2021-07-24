package websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import rest.ChatHandler;
import rest.ChatRobot;

@ServerEndpoint(value = "/websocket/chat/{userName}",
				encoders = {UserMessageTextEncoder.class,
							UserListTextEncoder.class})
public class Chat {
	public static List<Session> chatRoomSessions = new ArrayList<Session>();
	
	/*
	 * OnOpen the websocket for the first time.
	 */
	@OnOpen
	public void open(Session session, EndpointConfig config, @PathParam("userName") String userName) {
		session.getUserProperties().put("userName", userName);
		chatRoomSessions.add(session);
		if(!chatRoomSessions.isEmpty()) {
			ChatHandler.robot.enable();
		}
		System.out.println(userName + ": connection opened.");
		sendUsers();
	}
	
	@OnClose
	public void close(Session session, CloseReason reason) {
		chatRoomSessions.remove(session);
		if(chatRoomSessions.isEmpty()) {
			ChatHandler.robot.disable();
		}
		System.out.println(session.getUserProperties().get("userName") + ": connection closed. " + reason.getReasonPhrase());
		sendUsers();
	}
	
	@OnError
	public void error(Session session, Throwable t) {
		chatRoomSessions.remove(session);
		if(chatRoomSessions.isEmpty()) {
			//ChatHandler.robot.disable();
		}
		System.out.println(session.getUserProperties().get("userName") + " connection error: " + t.toString());
		sendUsers();
	}
	
	/*
	 * Executes when a message arrives the websocket.
	 * Sends the arrived message to all the clients in the chat.
	 */
	@OnMessage
	public void text(Session session, String msg) {
		if(!msg.equals("")) {
			if(isDM(msg)) {
				send(session, (String)session.getUserProperties().get("userName"), getDMTarget(msg), /*getDMMsg(*/msg/*)*/);
			} else {
				send((String)session.getUserProperties().get("userName"), msg);
			}
		}
	}
	
	/*
	 * Sends to every client in the chat room the new list of users 
	 * (after someone entered/left the chat).
	 */
	private void sendUsers() {
		try {
			UserListMessage msg = new UserListMessage(false, getUsers());
			for(Session session : chatRoomSessions) {
				session.getBasicRemote().sendObject(msg);
				System.out.println("Sent: userList");
			}
		} catch(IOException | EncodeException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Sends a number to the clients.
	 * This method uses only the robot.
	 */
	public static void send(int num) {
		UserMessage msg = new UserMessage(true, System.currentTimeMillis(), ChatRobot.ROBOT_NAME, num + "");
		try {
			for(Session session : chatRoomSessions) {
				session.getBasicRemote().sendObject(msg);
				//System.out.println("Sent: {0} " + msg.getContent());
			}
		} catch(IOException | EncodeException e) {
			//logger.log(Level.INFO, e.toString());
			System.out.println(e.toString());
		}
	}
	
	private static void send(String userName, String text) {
		UserMessage msg = new UserMessage(true, System.currentTimeMillis(), userName, text);
		try {
			for(Session session : chatRoomSessions) {
				session.getBasicRemote().sendObject(msg);
				System.out.println("Sent: {0} " + msg.getContent());
			}
		} catch(IOException | EncodeException e) {
			System.out.println(e.toString());
		}
	}
	
	/*
	 * Sends a private message from userName, addressed text to the targetUser.
	 * This message can see only targetUser.
	 */
	public static void send(Session sendSession, String userName, String targetUser, String text) {
		try {
			//check if target user exists
			boolean targetUserFound = false;
			for(Session session : chatRoomSessions) {
				if(session.getUserProperties().get("userName").equals(targetUser)) {
					targetUserFound = true;
					UserMessage msg = new UserMessage(true, System.currentTimeMillis(), userName, text);
					session.getBasicRemote().sendObject(msg);
					//System.out.println("Sent: {0} " + msg.getContent());
					break;
				}
			}
			if(targetUserFound) {
				UserMessage msg = new UserMessage(true, System.currentTimeMillis(), 
						//userName, "To " + targetUser + ": " + text);
						userName, text);
				sendSession.getBasicRemote().sendObject(msg);
				//System.out.println("Sent: {0} " + msg.getContent());
			} else {
				UserMessage msg = new UserMessage(true, System.currentTimeMillis(), "System",
						"User " + targetUser + " not found");
				sendSession.getBasicRemote().sendObject(msg);
				//System.out.println("Sent: {0} " + msg.getContent());
			}
		} catch(IOException | EncodeException e) {
			System.out.println(e.toString());
		}
	}
	
	/*
	 * Returns a list of users in the chat room.
	 */
	private List<String> getUsers() {
		List<String> users = new ArrayList<String>();
		for(Session session : chatRoomSessions) {
			users.add((String)session.getUserProperties().get("userName"));
		}
		return users;
	}
	
	/*
	 * Returns true, if the message is a DM-message (has @ symbol).
	 */
	private boolean isDM(String msg) {
		return msg.charAt(0) == '@';
	}
	
	/*
	 * Returns the target user, who is going to receive the message.
	 */
	private String getDMTarget(String msg) {
		//removing "@" and Msg content
		return msg.substring(1, msg.indexOf(' '));
	}
	
//	private String getDMMsg(String msg) {
//		return msg.substring(msg.indexOf(' ') + 1);
//	}
}