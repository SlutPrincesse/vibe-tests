package sovereign.extensions;

import sovereign.agent.AgentContext;
import java.util.*;

public interface Extension {
    String name();
    default void onLoad() {}
    default void onAgentStart(AgentContext ctx) {}
    default void onAgentEnd(AgentContext ctx) {}
}
