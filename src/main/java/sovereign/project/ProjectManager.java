package sovereign.project;

import java.util.*;

public class ProjectManager {
    private final Map<String, String> projects = new LinkedHashMap<>();
    public void create(String name, String path) { projects.put(name, path); }
    public String get(String name) { return projects.get(name); }
    public Collection<String> names() { return projects.keySet(); }
}
