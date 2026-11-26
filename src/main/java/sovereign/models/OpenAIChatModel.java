package sovereign.models;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class OpenAIChatModel implements ChatModel {
    private final String baseUrl;
    private final String apiKey;
    private final String model;

    public OpenAIChatModel(String baseUrl, String apiKey, String model) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length()-1) : baseUrl;
        this.apiKey = apiKey;
        this.model = model;
    }

    public String complete(List<Map<String, Object>> messages) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"model\":\"").append(model).append("\",\"messages\":[");
        for (int i=0;i<messages.size();i++) {
            if (i>0) sb.append(",");
            var m = messages.get(i);
            sb.append("{\"role\":\"").append(m.get("role")).append("\",\"content\":\"")
              .append(String.valueOf(m.get("content")).replace("\\","\\\\").replace("\"","\\\"").replace("\n","\\n"))
              .append("\"}");
        }
        sb.append("]}");
        URL url = new URL(baseUrl + "/chat/completions");
        HttpURLConnection c = (HttpURLConnection) url.openConnection();
        c.setRequestMethod("POST");
        c.setRequestProperty("Content-Type", "application/json");
        c.setRequestProperty("Authorization", "Bearer " + apiKey);
        c.setDoOutput(true);
        try (OutputStream os = c.getOutputStream()) { os.write(sb.toString().getBytes(StandardCharsets.UTF_8)); }
        InputStream is = c.getResponseCode() < 400 ? c.getInputStream() : c.getErrorStream();
        StringBuilder out = new StringBuilder();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line; while ((line = r.readLine()) != null) out.append(line).append("\n");
        }
        return out.toString();
    }
}
