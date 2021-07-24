package websocket;

import java.util.List;

public class UserListMessage {
	private boolean isMsg;	//false if msg is a new users list
	private List<String> users;

	public UserListMessage(boolean isMsg, List<String> users) {
		this.isMsg = isMsg;
		this.users = users;
	}

	public List<String> getUsers() {
		return users;
	}

	public boolean isMsg() {
		return isMsg;
	}

}
