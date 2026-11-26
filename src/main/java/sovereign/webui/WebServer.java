package sovereign.webui;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;

public class WebServer {
    private HttpServer server;
    public void start(int port) throws Exception {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", ex -> {
            byte[] b = "Sovereign Agent (Java)".getBytes();
            ex.sendResponseHeaders(200, b.length);
            ex.getResponseBody().write(b);
            ex.close();
        });
        server.start();
    }
    public void stop() { if (server != null) server.stop(0); }
}
