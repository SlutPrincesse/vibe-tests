package sovereign.scheduler;

import java.util.*;

public class Scheduler {
    private final List<String> tasks = new ArrayList<>();
    public void schedule(String cron, String command) { tasks.add(cron + " :: " + command); }
    public List<String> list() { return tasks; }
}
