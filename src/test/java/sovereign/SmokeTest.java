package sovereign;

import sovereign.agent.*;
import sovereign.tools.*;
import sovereign.memory.*;
import sovereign.knowledge.*;
import sovereign.notifications.*;
import sovereign.scheduler.*;
import sovereign.skills.*;
import sovereign.project.*;
import java.util.*;

public class SmokeTest {
    public static void main(String[] args) throws Exception {
        AgentContext ctx = new AgentContext(AgentContext.generateId(), AgentContext.Type.USER);
        ToolRegistry reg = new ToolRegistry();
        MemoryStore mem = new MemoryStore();
        reg.register(new MemoryTool(mem));
        reg.register(new KnowledgeTool(new KnowledgeBase()));
        reg.register(new NotificationTool(new NotificationManager()));
        reg.register(new SchedulerTool(new Scheduler()));
        reg.register(new SkillsTool(new SkillRegistry()));
        reg.register(new ProjectTool(new ProjectManager()));
        reg.register(new ShellTool());
        reg.register(new FileReadTool());
        reg.register(new FileWriteTool());
        reg.register(new BrowserTool());
        reg.register(new CodeTool());
        reg.register(new DocumentTool());
        // exercise
        Map<String,Object> a = new HashMap<>(); a.put("action","set"); a.put("key","k"); a.put("value","v");
        System.out.println("memory: " + reg.get("memory").execute(a));
        a = new HashMap<>(); a.put("action","list");
        System.out.println("scheduler: " + reg.get("scheduler").execute(a));
        System.out.println("tools count: " + reg.all().size());
        System.out.println("SMOKE OK");
    }
}
