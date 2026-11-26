package sovereign.tools;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileReadTool implements Tool {
    public String getName() { return "file_read"; }
    public String getDescription() { return "Read a file"; }
    public Map<String, Object> getParametersSchema() { return Map.of("path", Map.of("type", "string")); }
    public Object execute(Map<String, Object> args) throws Exception {
        String path = String.valueOf(args.get("path"));
        String content = Files.readString(Path.of(path));
        Map<String, Object> r = new HashMap<>();
        r.put("content", content);
        return r;
    }
}
