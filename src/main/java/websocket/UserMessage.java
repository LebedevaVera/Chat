package websocket;

public class UserMessage {
	private boolean isMsg;
	private long time;
	private String userName;
	private String content;
	
	public UserMessage(boolean isMsg, long time, String userName, String content) {
		this.isMsg = isMsg;
		this.time = time;
		this.userName = userName;
		this.content = content;
	}

	public boolean isMsg() {
		return isMsg;
	}

	public void setMsg(boolean isMsg) {
		this.isMsg = isMsg;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}