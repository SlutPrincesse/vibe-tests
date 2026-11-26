package sovereign.tools;

import java.util.*;

public class AdbTool implements Tool {
    public String getName() { return "adb"; }
    public String getDescription() { return "Android Debug Bridge commands"; }
    public Map<String, Object> getParametersSchema() { return Map.of("command", Map.of("type","string")); }
    public Object execute(Map<String, Object> args) throws Exception {
        String cmd = "adb " + String.valueOf(args.getOrDefault("command","devices"));
        Map<String, Object> a = new HashMap<>();
        a.put("command", cmd);
        return new ShellTool().execute(a);
    }
}
