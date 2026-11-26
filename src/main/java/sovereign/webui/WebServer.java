package sovereign.webui;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

public class WebServer {
    private ServerSocket serverSocket;
    private ExecutorService pool = Executors.newCachedThreadPool();
    private volatile boolean running = false;
    private Thread acceptThread;

    public void start(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        running = true;
        acceptThread = new Thread(() -> {
            while (running) {
                try {
                    Socket s = serverSocket.accept();
                    pool.submit(() -> handle(s));
                } catch (IOException e) { if (running) {} }
            }
        });
        acceptThread.setDaemon(true);
        acceptThread.start();
    }
    private void handle(Socket s) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
             OutputStream out = s.getOutputStream()) {
            String requestLine = in.readLine();
            // consume headers
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {}
            String path = "/";
            if (requestLine != null) {
                String[] parts = requestLine.split(" ");
                if (parts.length > 1) path = parts[1];
            }
            String body;
            String ct;
            if ("/api/status".equals(path)) { body = "{\"status\":\"ok\"}"; ct = "application/json"; }
            else { body = "<html><body><h1>Sovereign Agent (Java)</h1><p>Status: running</p></body></html>"; ct = "text/html"; }
            byte[] b = body.getBytes(StandardCharsets.UTF_8);
            out.write(("HTTP/1.1 200 OK\r\nContent-Type: " + ct + "\r\nContent-Length: " + b.length + "\r\nConnection: close\r\n\r\n").getBytes(StandardCharsets.UTF_8));
            out.write(b);
            out.flush();
        } catch (IOException ignored) {}
        finally { try { s.close(); } catch (IOException ignored) {} }
    }
    public void stop() {
        running = false;
        try { if (serverSocket != null) serverSocket.close(); } catch (IOException ignored) {}
        pool.shutdownNow();
    }
}
