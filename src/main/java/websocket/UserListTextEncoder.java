package websocket;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class UserListTextEncoder implements Encoder.Text<UserListMessage> {
	@Override
	public void init(EndpointConfig ec) {}
	
	@Override
	public void destroy() {}
	
	@Override
	public String encode(UserListMessage msg) throws EncodeException {
		JsonArrayBuilder array = Json.createArrayBuilder();
		for(String s : msg.getUsers()) {
			array.add(s);
		}
		JsonObject jsonObject = Json.createObjectBuilder()
				.add("isMsg", msg.isMsg())
				.add("users", array)
				.build();
		return jsonObject.toString();
	}
}
