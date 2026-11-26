package sovereign.tools;

import sovereign.project.ProjectManager;
import java.util.*;

public class ProjectTool implements Tool {
    private final ProjectManager pm;
    public ProjectTool(ProjectManager pm) { this.pm = pm; }
    public String getName() { return "project"; }
    public String getDescription() { return "Manage projects"; }
    public Map<String, Object> getParametersSchema() { return Map.of("action", Map.of("type","string"), "name", Map.of("type","string"), "path", Map.of("type","string")); }
    public Object execute(Map<String, Object> args) throws Exception {
        String action = String.valueOf(args.getOrDefault("action","list"));
        Map<String, Object> r = new HashMap<>();
        if ("create".equals(action)) { pm.create(String.valueOf(args.get("name")), String.valueOf(args.getOrDefault("path",""))); r.put("ok", true); }
        else if ("get".equals(action)) r.put("path", pm.get(String.valueOf(args.get("name"))));
        else r.put("projects", pm.names());
        return r;
    }
}
