package websocket;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class UserMessageTextEncoder implements Encoder.Text<UserMessage> {
	@Override
	public void init(EndpointConfig ec) {}
	
	@Override
	public void destroy() {}
	
	@Override
	public String encode(UserMessage msg) throws EncodeException {
		JsonObject jsonObject = Json.createObjectBuilder()
				.add("isMsg", msg.isMsg())
				.add("time", msg.getTime())
				.add("userName", msg.getUserName())
				.add("content", msg.getContent())
				.build();
		return jsonObject.toString();
	}
}