package sovereign.agent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AgentContext {
    public enum Type { USER, TASK, BACKGROUND }

    private static final Map<String, AgentContext> contexts = new ConcurrentHashMap<>();
    private static final AtomicInteger counter = new AtomicInteger();
    private static String currentId;

    private final String id;
    private final Type type;
    private final Map<String, Object> data = new ConcurrentHashMap<>();
    private final Map<String, Object> outputData = new ConcurrentHashMap<>();
    private Agent agent;
    private boolean running = false;

    public AgentContext(String id, Type type) {
        this.id = id;
        this.type = type;
    }

    public static String generateId() {
        return "ctx-" + counter.incrementAndGet();
    }

    public static AgentContext get(String id) { return contexts.get(id); }

    public static AgentContext use(String id) {
        AgentContext c = contexts.get(id);
        if (c == null) { c = new AgentContext(id, Type.USER); contexts.put(id, c); }
        return c;
    }

    public static AgentContext current() { return currentId == null ? null : contexts.get(currentId); }

    public static void setCurrent(String ctxid) { currentId = ctxid; }

    public static AgentContext first() { return contexts.values().stream().findFirst().orElse(null); }

    public static Collection<AgentContext> all() { return contexts.values(); }

    public static void remove(String id) { contexts.remove(id); }

    public String getId() { return id; }
    public Type getType() { return type; }

    public Object getData(String key, boolean recursive) { return data.get(key); }
    public void setData(String key, Object value, boolean recursive) { data.put(key, value); }
    public Object getOutputData(String key, boolean recursive) { return outputData.get(key); }
    public void setOutputData(String key, Object value, boolean recursive) { outputData.put(key, value); }

    public Agent getAgent() { return agent; }
    public void setAgent(Agent agent) { this.agent = agent; }

    public boolean isRunning() { return running; }
    public void setRunning(boolean r) { running = r; }

    public void reset() { data.clear(); outputData.clear(); running = false; }
}
