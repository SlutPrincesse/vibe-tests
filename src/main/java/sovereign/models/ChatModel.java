package sovereign.models;

import java.util.*;

public interface ChatModel {
    String complete(List<Map<String, Object>> messages) throws Exception;
}
