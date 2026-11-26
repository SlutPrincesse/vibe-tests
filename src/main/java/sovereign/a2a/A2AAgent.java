package sovereign.a2a;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

public class A2AAgent {
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
            int contentLength = 0;
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                if (line.toLowerCase().startsWith("content-length:")) {
                    contentLength = Integer.parseInt(line.substring(15).trim());
                }
            }
            char[] buf = new char[contentLength];
            int read = 0;
            while (read < contentLength) {
                int r = in.read(buf, read, contentLength - read);
                if (r < 0) break;
                read += r;
            }
            String body = new String(buf, 0, read);
            String reply = "{\"status\":\"ok\",\"echo\":\"" + body.replace("\"","\\\"") + "\"}";
            byte[] b = reply.getBytes(StandardCharsets.UTF_8);
            out.write(("HTTP/1.1 200 OK\r\nContent-Type: application/json\r\nContent-Length: " + b.length + "\r\nConnection: close\r\n\r\n").getBytes(StandardCharsets.UTF_8));
            out.write(b);
            out.flush();
        } catch (Exception ignored) {}
        finally { try { s.close(); } catch (IOException ignored) {} }
    }
    public String handle(String message) { return "echo: " + message; }
    public void stop() {
        running = false;
        try { if (serverSocket != null) serverSocket.close(); } catch (IOException ignored) {}
        pool.shutdownNow();
    }
}
