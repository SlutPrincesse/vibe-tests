package sovereign.memory;

import java.util.*;

public class MemoryStore {
    private final Map<String, String> store = new LinkedHashMap<>();
    public void put(String key, String value) { store.put(key, value); }
    public String get(String key) { return store.get(key); }
    public Map<String, String> all() { return store; }
    public void clear() { store.clear(); }
}
