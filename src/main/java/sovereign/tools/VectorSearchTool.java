package sovereign.tools;

import sovereign.knowledge.VectorStore;
import java.util.*;

public class VectorSearchTool implements Tool {
    private final VectorStore vs;
    public VectorSearchTool(VectorStore vs) { this.vs = vs; }
    public String getName() { return "vector_search"; }
    public String getDescription() { return "Semantic vector search"; }
    public Map<String, Object> getParametersSchema() { return Map.of("action", Map.of("type","string"), "id", Map.of("type","string"), "text", Map.of("type","string"), "query", Map.of("type","string")); }
    public Object execute(Map<String, Object> args) throws Exception {
        String action = String.valueOf(args.getOrDefault("action","search"));
        Map<String, Object> r = new HashMap<>();
        if ("add".equals(action)) { vs.add(String.valueOf(args.get("id")), String.valueOf(args.getOrDefault("text",""))); r.put("ok", true); }
        else if ("get".equals(action)) r.put("text", vs.get(String.valueOf(args.get("id"))));
        else r.put("results", vs.search(String.valueOf(args.getOrDefault("query","")), 5));
        return r;
    }
}
