package sovereign.extensions;

import java.util.*;

public class ExtensionManager {
    private final List<Extension> extensions = new ArrayList<>();
    public void register(Extension e) { extensions.add(e); e.onLoad(); }
    public List<Extension> all() { return extensions; }
}
