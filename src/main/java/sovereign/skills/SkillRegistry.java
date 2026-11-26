package sovereign.skills;

import java.util.*;

public class SkillRegistry {
    private final Map<String, String> skills = new LinkedHashMap<>();
    public void register(String name, String content) { skills.put(name, content); }
    public String get(String name) { return skills.get(name); }
    public Collection<String> names() { return skills.keySet(); }
}
