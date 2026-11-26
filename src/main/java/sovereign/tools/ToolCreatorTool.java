package sovereign.tools;

import java.util.*;

public class ToolCreatorTool implements Tool {
    private final ToolRegistry registry;
    public ToolCreatorTool(ToolRegistry registry) { this.registry = registry; }
    public String getName() { return "create_tool"; }
    public String getDescription() { return "Create a custom tool backed by a shell command"; }
    public Map<String, Object> getParametersSchema() { return Map.of("name", Map.of("type","string"), "command", Map.of("type","string")); }
    public Object execute(Map<String, Object> args) throws Exception {
        String name = String.valueOf(args.get("name"));
        String command = String.valueOf(args.get("command"));
        CustomTool ct = new CustomTool(name, command);
        registry.register(ct);
        Map<String, Object> r = new HashMap<>();
        r.put("created", name);
        r.put("ok", true);
        return r;
    }
}
