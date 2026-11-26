package sovereign.notifications;

import java.util.*;

public class NotificationManager {
    private final List<String> notifications = new ArrayList<>();
    public void notify(String msg) { notifications.add(msg); }
    public List<String> all() { return notifications; }
}
