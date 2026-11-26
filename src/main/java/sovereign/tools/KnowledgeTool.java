package sovereign.tools;

import sovereign.knowledge.KnowledgeBase;
import java.util.*;

public class KnowledgeTool implements Tool {
    private final KnowledgeBase kb;
    public KnowledgeTool(KnowledgeBase kb) { this.kb = kb; }
    public String getName() { return "knowledge"; }
    public String getDescription() { return "Knowledge base query"; }
    public Map<String, Object> getParametersSchema() { return Map.of("action", Map.of("type","string"), "id", Map.of("type","string"), "text", Map.of("type","string"), "query", Map.of("type","string")); }
    public Object execute(Map<String, Object> args) throws Exception {
        String action = String.valueOf(args.getOrDefault("action","search"));
        Map<String, Object> r = new HashMap<>();
        switch (action) {
            case "add" -> { kb.add(String.valueOf(args.get("id")), String.valueOf(args.getOrDefault("text",""))); r.put("ok", true); }
            case "get" -> r.put("text", kb.get(String.valueOf(args.get("id"))));
            default -> r.put("results", kb.search(String.valueOf(args.getOrDefault("query",""))));
        }
        return r;
    }
}
