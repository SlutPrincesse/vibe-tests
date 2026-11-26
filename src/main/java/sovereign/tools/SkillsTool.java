package sovereign.tools;

import sovereign.skills.SkillRegistry;
import java.util.*;

public class SkillsTool implements Tool {
    private final SkillRegistry reg;
    public SkillsTool(SkillRegistry reg) { this.reg = reg; }
    public String getName() { return "skills"; }
    public String getDescription() { return "Manage skills"; }
    public Map<String, Object> getParametersSchema() { return Map.of("action", Map.of("type","string"), "name", Map.of("type","string"), "content", Map.of("type","string")); }
    public Object execute(Map<String, Object> args) throws Exception {
        String action = String.valueOf(args.getOrDefault("action","list"));
        Map<String, Object> r = new HashMap<>();
        if ("register".equals(action)) { reg.register(String.valueOf(args.get("name")), String.valueOf(args.getOrDefault("content",""))); r.put("ok", true); }
        else if ("get".equals(action)) r.put("content", reg.get(String.valueOf(args.get("name"))));
        else r.put("skills", reg.names());
        return r;
    }
}
