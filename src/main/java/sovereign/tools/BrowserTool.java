package sovereign.tools;

import java.util.*;

public class BrowserTool implements Tool {
    public String getName() { return "browser"; }
    public String getDescription() { return "Browser automation (stub)"; }
    public Map<String, Object> getParametersSchema() { return Map.of("url", Map.of("type","string")); }
    public Object execute(Map<String, Object> args) throws Exception {
        return Map.of("status", "not implemented", "url", args.getOrDefault("url",""));
    }
}
