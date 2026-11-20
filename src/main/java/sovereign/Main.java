package sovereign;

import sovereign.agent.*;
import sovereign.tools.*;

public class Main {
    public static void main(String[] args) {
        AgentContext ctx = new AgentContext(AgentContext.generateId(), AgentContext.Type.USER);
        AgentContext.setCurrent(ctx.getId());
        AgentConfig cfg = new AgentConfig();
        ToolRegistry reg = new ToolRegistry();
        reg.register(new ShellTool());
        Agent agent = new Agent(ctx, cfg, reg);
        System.out.println("Sovereign Agent (Java) ready. tools=" + reg.names());
    }
}
