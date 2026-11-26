package sovereign.tools;

import java.util.*;

public class CodeTool implements Tool {
    public String getName() { return "code"; }
    public String getDescription() { return "Execute code (delegates to shell)"; }
    public Map<String, Object> getParametersSchema() { return Map.of("language", Map.of("type","string"), "code", Map.of("type","string")); }
    public Object execute(Map<String, Object> args) throws Exception {
        String code = String.valueOf(args.getOrDefault("code",""));
        Map<String, Object> a = new HashMap<>();
        a.put("command", code);
        return new ShellTool().execute(a);
    }
}
