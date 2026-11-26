package sovereign.tools;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DocumentTool implements Tool {
    public String getName() { return "document"; }
    public String getDescription() { return "Query documents"; }
    public Map<String, Object> getParametersSchema() { return Map.of("path", Map.of("type","string"), "query", Map.of("type","string")); }
    public Object execute(Map<String, Object> args) throws Exception {
        String path = String.valueOf(args.getOrDefault("path",""));
        String query = String.valueOf(args.getOrDefault("query",""));
        String text = Files.readString(Path.of(path));
        // simple: return lines containing query
        List<String> hits = new ArrayList<>();
        for (String line : text.split("\n")) if (line.toLowerCase().contains(query.toLowerCase())) hits.add(line);
        Map<String, Object> r = new HashMap<>();
        r.put("matches", hits);
        r.put("totalLines", text.split("\n").length);
        return r;
    }
}
