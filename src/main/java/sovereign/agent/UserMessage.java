package sovereign.agent;

public class UserMessage {
    public String content;
    public boolean intervention = false;

    public UserMessage() {}
    public UserMessage(String content) { this.content = content; }

    @Override
    public String toString() { return content; }
}
