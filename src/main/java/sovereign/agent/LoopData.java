package sovereign.agent;

import java.util.*;

public class LoopData {
    public UserMessage userMessage;
    public List<String> systemPrompts = new ArrayList<>();
    public List<Map<String, Object>> history = new ArrayList<>();
    public List<Map<String, Object>> messages = new ArrayList<>();
    public String lastResponse = "";
    public int iteration = 0;

    public LoopData() {}
    public LoopData(UserMessage msg) { this.userMessage = msg; }
}
