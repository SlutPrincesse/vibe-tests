package sovereign.permissions;

import java.util.*;

public class PermissionManager {
    private final Set<String> granted = new HashSet<>();
    public PermissionManager() { granted.add("*"); }
    public void grant(String perm) { granted.add(perm); }
    public void revoke(String perm) { granted.remove(perm); }
    public boolean has(String perm) { return granted.contains("*") || granted.contains(perm); }
    public Set<String> all() { return new HashSet<>(granted); }
}
