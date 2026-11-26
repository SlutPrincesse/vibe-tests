package sovereign.plugins;

import java.util.*;

public class PluginManager {
    private final List<Plugin> plugins = new ArrayList<>();
    public void load(Plugin p) { plugins.add(p); p.onLoad(); }
    public List<Plugin> all() { return plugins; }
}
