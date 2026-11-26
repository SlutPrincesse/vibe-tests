package sovereign.tools;

import sovereign.permissions.PermissionManager;
import java.util.*;

public class PermissionTool implements Tool {
    private final PermissionManager pm;
    public PermissionTool(PermissionManager pm) { this.pm = pm; }
    public String getName() { return "permissions"; }
    public String getDescription() { return "Manage permissions"; }
    public Map<String, Object> getParametersSchema() { return Map.of("action", Map.of("type","string"), "permission", Map.of("type","string")); }
    public Object execute(Map<String, Object> args) throws Exception {
        String action = String.valueOf(args.getOrDefault("action","list"));
        Map<String, Object> r = new HashMap<>();
        if ("grant".equals(action)) { pm.grant(String.valueOf(args.get("permission"))); r.put("ok", true); }
        else if ("revoke".equals(action)) { pm.revoke(String.valueOf(args.get("permission"))); r.put("ok", true); }
        else if ("check".equals(action)) { r.put("allowed", pm.has(String.valueOf(args.get("permission")))); }
        else r.put("permissions", pm.all());
        return r;
    }
}
