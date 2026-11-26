package sovereign.plugins;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class PluginManager {
    private final List<Plugin> plugins = new ArrayList<>();
    public void load(Plugin p) { plugins.add(p); p.onLoad(); }
    public void loadFromDir(File dir) throws Exception {
        if (!dir.exists() || !dir.isDirectory()) return;
        File[] jars = dir.listFiles((d,n) -> n.endsWith(".jar"));
        if (jars == null) return;
        for (File j : jars) {
            URLClassLoader cl = new URLClassLoader(new URL[]{j.toURI().toURL()}, getClass().getClassLoader());
            // load classes named in manifest? Simple: instantiate any Plugin impl found via ServiceLoader
            ServiceLoader<Plugin> sl = ServiceLoader.load(Plugin.class, cl);
            for (Plugin p : sl) load(p);
        }
    }
    public List<Plugin> all() { return plugins; }
}
