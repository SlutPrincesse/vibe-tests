package sovereign.mcp;

import sovereign.tools.Tool;
import sovereign.tools.ToolRegistry;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

public class McpServer {
    private ServerSocket serverSocket;
    private ExecutorService pool = Executors.newCachedThreadPool();
    private volatile boolean running = false;
    private Thread acceptThread;
    private final ToolRegistry tools;
    public McpServer(ToolRegistry tools) { this.tools = tools; }
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
            // read request line
            String requestLine = in.readLine();
            int contentLength = 0;
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                if (line.toLowerCase().startsWith("content-length:")) {
                    contentLength = Integer.parseInt(line.substring(15).trim());
                }
            }
            StringBuilder body = new StringBuilder();
            if (contentLength > 0) {
                char[] buf = new char[contentLength];
                int read = 0;
                while (read < contentLength) {
                    int r = in.read(buf, read, contentLength - read);
                    if (r < 0) break;
                    read += r;
                }
                body.append(buf, 0, read);
            }
            String json = body.toString();
            String method = extract(json, "method");
            String id = extract(json, "id");
            String response;
            if ("tools/list".equals(method)) {
                StringBuilder sb = new StringBuilder();
                sb.append("{\"jsonrpc\":\"2.0\",\"id\":").append(id==null?"null":"\""+id+"\"").append(",\"result\":{\"tools\":[");
                boolean first=true;
                for (Tool t : tools.all()) {
                    if (!first) sb.append(",");
                    first=false;
                    sb.append("{\"name\":\"").append(t.getName()).append("\",\"description\":\"").append(t.getDescription()).append("\"}");
                }
                sb.append("]}}");
                response = sb.toString();
            } else if ("tools/call".equals(method)) {
                String name = extract(json, "name");
                Tool t = tools.get(name);
                String result = t==null ? "unknown tool" : String.valueOf(t.execute(new HashMap<>()));
                response = "{\"jsonrpc\":\"2.0\",\"id\":" + (id==null?"null":"\""+id+"\"") + ",\"result\":{\"content\":[{\"type\":\"text\",\"text\":\"" + result.replace("\"","\\\"") + "\"}]}}";
            } else {
                response = "{\"jsonrpc\":\"2.0\",\"id\":" + (id==null?"null":"\""+id+"\"") + ",\"error\":{\"code\":-32601,\"message\":\"Method not found\"}}";
            }
            byte[] b = response.getBytes(StandardCharsets.UTF_8);
            out.write(("HTTP/1.1 200 OK\r\nContent-Type: application/json\r\nContent-Length: " + b.length + "\r\nConnection: close\r\n\r\n").getBytes(StandardCharsets.UTF_8));
            out.write(b);
            out.flush();
        } catch (Exception ignored) {}
        finally { try { s.close(); } catch (IOException ignored) {} }
    }
    public void stop() {
        running = false;
        try { if (serverSocket != null) serverSocket.close(); } catch (IOException ignored) {}
        pool.shutdownNow();
    }
    private static String extract(String json, String key) {
        String pat = "\""+key+"\"";
        int i = json.indexOf(pat);
        if (i < 0) return null;
        int colon = json.indexOf(":", i);
        int q1 = json.indexOf("\"", colon+1);
        if (q1 < 0) return null;
        int q2 = json.indexOf("\"", q1+1);
        return json.substring(q1+1, q2);
    }
}
