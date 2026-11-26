package sovereign.tools;

import sovereign.notifications.NotificationManager;
import java.util.*;

public class NotificationTool implements Tool {
    private final NotificationManager nm;
    public NotificationTool(NotificationManager nm) { this.nm = nm; }
    public String getName() { return "notify"; }
    public String getDescription() { return "Send notification"; }
    public Map<String, Object> getParametersSchema() { return Map.of("message", Map.of("type","string")); }
    public Object execute(Map<String, Object> args) throws Exception {
        nm.notify(String.valueOf(args.getOrDefault("message","")));
        return Map.of("ok", true);
    }
}
