package sovereign;

import sovereign.agent.*;
import sovereign.tools.*;
import sovereign.memory.*;
import sovereign.knowledge.*;
import sovereign.notifications.*;
import sovereign.scheduler.*;
import sovereign.skills.*;
import sovereign.project.*;
import sovereign.extensions.*;
import sovereign.plugins.*;
import sovereign.webui.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        AgentContext ctx = new AgentContext(AgentContext.generateId(), AgentContext.Type.USER);
        AgentContext.setCurrent(ctx.getId());
        AgentConfig cfg = new AgentConfig();

        MemoryStore mem = new MemoryStore();
        KnowledgeBase kb = new KnowledgeBase();
        NotificationManager nm = new NotificationManager();
        Scheduler sch = new Scheduler();
        SkillRegistry skills = new SkillRegistry();
        ProjectManager pm = new ProjectManager();
        ExtensionManager ext = new ExtensionManager();
        PluginManager plugins = new PluginManager();

        ToolRegistry reg = new ToolRegistry();
        reg.register(new ShellTool());
        reg.register(new FileReadTool());
        reg.register(new FileWriteTool());
        reg.register(new MemoryTool(mem));
        reg.register(new KnowledgeTool(kb));
        reg.register(new NotificationTool(nm));
        reg.register(new SchedulerTool(sch));
        reg.register(new SkillsTool(skills));
        reg.register(new ProjectTool(pm));
        reg.register(new BrowserTool());
        reg.register(new CodeTool());

        Agent agent = new Agent(ctx, cfg, reg);
        System.out.println("Sovereign Agent (Java) ready. tools=" + reg.names());

        // demo
        Map<String, Object> a = new HashMap<>();
        a.put("command", "echo hello");
        System.out.println("shell demo: " + new ShellTool().execute(a));
    }
}
