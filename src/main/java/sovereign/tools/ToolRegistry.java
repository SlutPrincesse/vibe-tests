package sovereign.tools;

import java.util.*;

public class ToolRegistry {
    private final Map<String, Tool> tools = new LinkedHashMap<>();

    public void register(Tool t) { tools.put(t.getName(), t); }
    public Tool get(String name) { return tools.get(name); }
    public Collection<Tool> all() { return tools.values(); }
    public Set<String> names() { return tools.keySet(); }
    public boolean has(String name) { return tools.containsKey(name); }
}
