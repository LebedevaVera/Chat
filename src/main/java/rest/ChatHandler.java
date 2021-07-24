package rest;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.mvc.Viewable;

import websocket.Chat;

@Singleton
@Path("/chat")
public class ChatHandler {
	public static ChatRobot robot = new ChatRobot();
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Viewable showLobby() {
		return new Viewable("lobby.jsp");
	}
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	public synchronized Viewable enterChat(@FormParam("username") String userName) {
		if(isOccupied(userName)) {
			return new Viewable("lobby.jsp", "Username is used");
		}
		System.out.println("Entering chat username " + userName);
		return new Viewable("chat.jsp", userName);
	}
	
	private boolean isOccupied(String userName) {
		for(Session s : Chat.chatRoomSessions) {
			if(s.getUserProperties().containsValue(userName)) {
				return true;
			}
		}
		return false;
	}
}