package sovereign.models;

import java.net.http.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class OpenAIChatModel implements ChatModel {
    private final String baseUrl;
    private final String apiKey;
    private final String model;
    private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(30)).build();

    public OpenAIChatModel(String baseUrl, String apiKey, String model) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length()-1) : baseUrl;
        this.apiKey = apiKey;
        this.model = model;
    }

    public String complete(java.util.List<java.util.Map<String, Object>> messages) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"model\":\"").append(model).append("\",\"messages\":[");
        for (int i=0;i<messages.size();i++) {
            if (i>0) sb.append(",");
            var m = messages.get(i);
            sb.append("{\"role\":\"").append(m.get("role")).append("\",\"content\":\"")
              .append(String.valueOf(m.get("content")).replace("\"","\\\"").replace("\n","\\n"))
              .append("\"}");
        }
        sb.append("]}");
        HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + "/chat/completions"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + apiKey)
            .POST(HttpRequest.BodyPublishers.ofString(sb.toString(), StandardCharsets.UTF_8))
            .build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return res.body();
    }
}
