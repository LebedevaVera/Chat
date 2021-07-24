package rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/static")
public class StaticContentHandler {

	@GET
	@Path("{filename}")
	public InputStream getFile(@PathParam("filename") String fileName) throws FileNotFoundException {
		File file = new File("V:\\Coding_Projects\\Java Web\\websocket_chat\\src\\main\\webapp\\WEB-INF\\static\\" + fileName);
		return new FileInputStream(file);
	}
}
