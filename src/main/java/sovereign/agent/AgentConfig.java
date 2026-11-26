package sovereign.agent;

public class AgentConfig {
    public String chatModel = "openai/gpt-4o";
    public String utilityModel = "openai/gpt-4o-mini";
    public String embeddingModel = "openai/text-embedding-3-small";
    public int maxTokens = 4096;
    public double temperature = 0.7;
    public String systemPrompt = "You are a helpful agent.";
    public String apiBaseUrl = "https://api.openai.com/v1";
    public String apiKey = "";
}
