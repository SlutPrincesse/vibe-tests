package sovereign.tools;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BrowserTool implements Tool {
    public String getName() { return "browser"; }
    public String getDescription() { return "Fetch and extract text from a URL"; }
    public Map<String, Object> getParametersSchema() { return Map.of("url", Map.of("type","string")); }
    public Object execute(Map<String, Object> args) throws Exception {
        String url = String.valueOf(args.get("url"));
        HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
        c.setRequestProperty("User-Agent", "SovereignAgent/1.0");
        c.setConnectTimeout(15000);
        c.setReadTimeout(15000);
        InputStream is = c.getResponseCode() < 400 ? c.getInputStream() : c.getErrorStream();
        StringBuilder html = new StringBuilder();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line; while ((line = r.readLine()) != null) html.append(line).append("\n");
        }
        String text = html.toString().replaceAll("<script[^>]*>.*?</script>", "")
            .replaceAll("<style[^>]*>.*?</style>", "")
            .replaceAll("<[^>]+>", " ")
            .replaceAll("\\s+", " ").trim();
        String title = "";
        int ti = html.toString().toLowerCase().indexOf("<title>");
        if (ti >= 0) {
            int tj = html.toString().toLowerCase().indexOf("</title>", ti);
            if (tj > ti) title = html.toString().substring(ti+7, tj).trim();
        }
        Map<String, Object> r = new HashMap<>();
        r.put("status", c.getResponseCode());
        r.put("title", title);
        r.put("text", text.length() > 4000 ? text.substring(0,4000) : text);
        return r;
    }
}
