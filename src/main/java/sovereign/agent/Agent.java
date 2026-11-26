package sovereign.agent;

import sovereign.models.*;
import sovereign.tools.*;
import java.util.*;

public class Agent {
    private final AgentContext context;
    private final AgentConfig config;
    private final ToolRegistry tools;
    private ChatModel chatModel;
    private UtilityModel utilityModel;
    private EmbeddingModel embeddingModel;
    private final Map<String, Object> data = new HashMap<>();
    private sovereign.extensions.ExtensionManager extensions;

    public Agent(AgentContext context, AgentConfig config, ToolRegistry tools) {
        this.context = context;
        this.config = config;
        this.tools = tools;
        context.setAgent(this);
    }

    public void setExtensions(sovereign.extensions.ExtensionManager em) { this.extensions = em; }
    public sovereign.extensions.ExtensionManager getExtensions() { return extensions; }
    public AgentConfig getConfig() { return config; }
    public ToolRegistry getTools() { return tools; }

    public void setChatModel(ChatModel m) { chatModel = m; }
    public void setUtilityModel(UtilityModel m) { utilityModel = m; }
    public void setEmbeddingModel(EmbeddingModel m) { embeddingModel = m; }

    public ChatModel getChatModel() { return chatModel; }
    public UtilityModel getUtilityModel() { return utilityModel; }
    public EmbeddingModel getEmbeddingModel() { return embeddingModel; }

    public Object getData(String field) { return data.get(field); }
    public void setData(String field, Object value) { data.put(field, value); }

    public List<String> getSystemPrompt(LoopData loopData) {
        List<String> out = new ArrayList<>();
        out.add(config.systemPrompt);
        return out;
    }

    public List<Map<String, Object>> preparePrompt(LoopData loopData) {
        List<Map<String, Object>> msgs = new ArrayList<>();
        for (String sp : getSystemPrompt(loopData)) {
            Map<String, Object> m = new HashMap<>();
            m.put("role", "system");
            m.put("content", sp);
            msgs.add(m);
        }
        if (loopData.userMessage != null) {
            Map<String, Object> m = new HashMap<>();
            m.put("role", "user");
            m.put("content", loopData.userMessage.content);
            msgs.add(m);
        }
        for (Map<String, Object> h : loopData.history) msgs.add(h);
        return msgs;
    }

    public String parseToolCall(String response) {
        // naive: look for <tool_call> name </tool_call>
        int i = response.indexOf("<tool_call>");
        if (i < 0) return null;
        int j = response.indexOf("</tool_call>", i);
        if (j < 0) return null;
        return response.substring(i + "<tool_call>".length(), j).trim();
    }

    public Map<String, Object> executeTool(String name, Map<String, Object> args) throws Exception {
        Tool t = tools.get(name);
        if (t == null) {
            Map<String, Object> r = new HashMap<>();
            r.put("error", "unknown tool: " + name);
            return r;
        }
        Object result = t.execute(args == null ? new HashMap<>() : args);
        Map<String, Object> r = new HashMap<>();
        r.put("result", result);
        return r;
    }

    public void histAddMessage(String role, String content) {
        Map<String, Object> m = new HashMap<>();
        m.put("role", role);
        m.put("content", content);
        context.getData("history", true); // noop
    }

    public String monologueSync(LoopData loopData) throws Exception {
        loopData.iteration = 0;
        while (loopData.iteration < 10) {
            loopData.iteration++;
            List<Map<String, Object>> msgs = preparePrompt(loopData);
            String resp = chatModel == null ? "(no model)" : chatModel.complete(msgs);
            loopData.lastResponse = resp;
            String tool = parseToolCall(resp);
            if (tool == null) break;
            // tool name only for now
            Map<String, Object> tr = executeTool(tool, new HashMap<>());
            loopData.history.add(Map.of("role", "tool", "content", String.valueOf(tr)));
        }
        return loopData.lastResponse;
    }
}
