package sovereign.tools;

import sovereign.memory.MemoryStore;
import java.util.*;

public class MemoryTool implements Tool {
    private final MemoryStore store;
    public MemoryTool(MemoryStore store) { this.store = store; }
    public String getName() { return "memory"; }
    public String getDescription() { return "Store/recall memory"; }
    public Map<String, Object> getParametersSchema() { return Map.of("action", Map.of("type","string"), "key", Map.of("type","string"), "value", Map.of("type","string")); }
    public Object execute(Map<String, Object> args) throws Exception {
        String action = String.valueOf(args.getOrDefault("action","get"));
        String key = String.valueOf(args.getOrDefault("key",""));
        Map<String, Object> r = new HashMap<>();
        if ("set".equals(action)) { store.put(key, String.valueOf(args.getOrDefault("value",""))); r.put("ok", true); }
        else { r.put("value", store.get(key)); }
        return r;
    }
}
