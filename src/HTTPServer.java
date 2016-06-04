import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HTTPServer {
	
	public static void main(String[] args) {
		// http://stackoverflow.com/questions/3732109/simple-http-server-in-java-using-only-java-se-api
		
		HttpServer server;
		try {
			System.out.println("Trying to start server port 8990...");
			server = HttpServer.create(new InetSocketAddress(8990), 0);
	        server.createContext("/test", new MyHandler());
	        server.setExecutor(null); // creates a default executor
	        server.start();
	        System.out.println("Ready!!!");
	        System.out.println("===================================");
		} catch (IOException e) {
			e.printStackTrace();
		}
        
	}
	
	private static long RUNNER = 0;
	
	static class MyHandler implements HttpHandler {
		
        @Override
        public void handle(HttpExchange t) throws IOException {
        	System.out.println(RUNNER++ + " " + t.getRemoteAddress() + " " + t.getRequestURI());
        	
            String response = "This is the response";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            System.out.println("===================================\n\n");
        }
    }

}
