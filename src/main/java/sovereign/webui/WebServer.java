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
            String path = "/";
            if (requestLine != null) {
                String[] parts = requestLine.split(" ");
                if (parts.length > 1) path = parts[1];
            }
            String respBody;
            String ct;
            if (path.startsWith("/api/status")) { respBody = "{\"status\":\"ok\"}"; ct = "application/json"; }
            else if (path.startsWith("/api/chat")) { respBody = "{\"reply\":\"echo: " + body.replace("\"","\\\"") + "\"}"; ct = "application/json"; }
            else { respBody = HTML; ct = "text/html"; }
            byte[] b = respBody.getBytes(StandardCharsets.UTF_8);
            out.write(("HTTP/1.1 200 OK\r\nContent-Type: " + ct + "\r\nContent-Length: " + b.length + "\r\nConnection: close\r\n\r\n").getBytes(StandardCharsets.UTF_8));
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

    private static final String HTML = "<!DOCTYPE html><html><head><meta charset='utf-8'><title>Sovereign Agent</title>"
        + "<style>body{margin:0;font-family:sans-serif;background:#0b1020;color:#e6e9f0;}#wrap{display:flex;height:100vh;}"
        + "#side{width:260px;background:#111827;padding:16px;}#main{flex:1;display:flex;flex-direction:column;}"
        + "#chat{flex:1;overflow:auto;padding:16px;}#bar{display:flex;padding:12px;gap:8px;}"
        + "input{flex:1;padding:8px;border-radius:6px;border:none;}button{padding:8px 14px;border:none;border-radius:6px;background:#6366f1;color:#fff;cursor:pointer;}"
        + "canvas{position:fixed;inset:0;z-index:-1;opacity:.35;}</style></head><body>"
        + "<canvas id='weather'></canvas><div id='wrap'><div id='side'><h2>Sovereign Agent</h2><p>Tools, memory, knowledge...</p><label><input type='checkbox' id='wx' checked> Weather theme</label></div>"
        + "<div id='main'><div id='chat'></div><div id='bar'><input id='msg' placeholder='Type a message...'><button onclick='send()'>Send</button></div></div></div>"
        + "<script>"
        + "const chat=document.getElementById('chat');"
        + "function add(t){const d=document.createElement('div');d.textContent=t;chat.appendChild(d);chat.scrollTop=chat.scrollHeight;}"
        + "async function send(){const m=document.getElementById('msg').value;if(!m)return;add('You: '+m);document.getElementById('msg').value='';"
        + "const r=await fetch('/api/chat',{method:'POST',body:m});const j=await r.json();add('Agent: '+j.reply);}"
        + "/* weather animation */"
        + "const c=document.getElementById('weather'),ctx=c.getContext('2d');let W,H,drops=[];"
        + "function resize(){W=c.width=innerWidth;H=c.height=innerHeight;}addEventListener('resize',resize);resize();"
        + "for(let i=0;i<120;i++)drops.push({x:Math.random()*W,y:Math.random()*H,v:1+Math.random()*3});"
        + "function frame(){ctx.clearRect(0,0,W,H);if(document.getElementById('wx').checked){ctx.fillStyle='rgba(99,102,241,.6)';"
        + "for(const d of drops){ctx.fillRect(d.x,d.y,2,12);d.y+=d.v;if(d.y>H){d.y=-12;d.x=Math.random()*W;}}}"
        + "requestAnimationFrame(frame);}frame();"
        + "</script></body></html>";
}
