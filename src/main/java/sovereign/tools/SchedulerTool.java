package sovereign.tools;

import sovereign.scheduler.Scheduler;
import java.util.*;

public class SchedulerTool implements Tool {
    private final Scheduler s;
    public SchedulerTool(Scheduler s) { this.s = s; }
    public String getName() { return "scheduler"; }
    public String getDescription() { return "Schedule tasks"; }
    public Map<String, Object> getParametersSchema() { return Map.of("action", Map.of("type","string"), "cron", Map.of("type","string"), "command", Map.of("type","string")); }
    public Object execute(Map<String, Object> args) throws Exception {
        String action = String.valueOf(args.getOrDefault("action","list"));
        Map<String, Object> r = new HashMap<>();
        if ("schedule".equals(action)) { s.schedule(String.valueOf(args.getOrDefault("cron","")), String.valueOf(args.getOrDefault("command",""))); r.put("ok", true); }
        else r.put("tasks", s.list());
        return r;
    }
}
