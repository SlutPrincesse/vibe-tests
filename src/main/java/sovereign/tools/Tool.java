package sovereign.tools;

import java.util.*;

public interface Tool {
    String getName();
    String getDescription();
    Map<String, Object> getParametersSchema();
    Object execute(Map<String, Object> args) throws Exception;
}
