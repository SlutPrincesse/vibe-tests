package sovereign.tools;

import java.util.*;

public class CustomTool implements Tool {
    private final String name;
    private final String commandTemplate;
    public CustomTool(String name, String commandTemplate) {
        this.name = name;
        this.commandTemplate = commandTemplate;
    }
    public String getName() { return name; }
    public String getDescription() { return "Custom tool: " + name; }
    public Map<String, Object> getParametersSchema() { return Map.of("input", Map.of("type","string")); }
    public Object execute(Map<String, Object> args) throws Exception {
        String cmd = commandTemplate;
        for (var e : args.entrySet()) cmd = cmd.replace("{" + e.getKey() + "}", String.valueOf(e.getValue()));
        Map<String, Object> a = new HashMap<>();
        a.put("command", cmd);
        return new ShellTool().execute(a);
    }
}
