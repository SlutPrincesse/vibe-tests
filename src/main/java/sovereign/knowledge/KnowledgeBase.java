package sovereign.knowledge;

import java.util.*;

public class KnowledgeBase {
    private final Map<String, String> docs = new LinkedHashMap<>();
    public void add(String id, String text) { docs.put(id, text); }
    public String get(String id) { return docs.get(id); }
    public List<String> search(String q) {
        List<String> out = new ArrayList<>();
        for (var e : docs.entrySet()) if (e.getValue().toLowerCase().contains(q.toLowerCase())) out.add(e.getKey());
        return out;
    }
    public Collection<String> ids() { return docs.keySet(); }
}
