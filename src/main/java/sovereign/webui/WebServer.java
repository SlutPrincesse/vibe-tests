package sovereign.webui;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;

public class WebServer {
    private HttpServer server;
    public void start(int port) throws Exception {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", ex -> {
            String html = "<html><body><h1>Sovereign Agent (Java)</h1><p>Status: running</p></body></html>";
            byte[] b = html.getBytes();
            ex.getResponseHeaders().set("Content-Type", "text/html");
            ex.sendResponseHeaders(200, b.length);
            ex.getResponseBody().write(b);
            ex.close();
        });
        server.createContext("/api/status", ex -> {
            byte[] b = "{\"status\":\"ok\"}".getBytes();
            ex.getResponseHeaders().set("Content-Type", "application/json");
            ex.sendResponseHeaders(200, b.length);
            ex.getResponseBody().write(b);
            ex.close();
        });
        server.start();
    }
    public void stop() { if (server != null) server.stop(0); }
}
