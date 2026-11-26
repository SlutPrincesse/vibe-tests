package sovereign.tools;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileWriteTool implements Tool {
    public String getName() { return "file_write"; }
    public String getDescription() { return "Write a file"; }
    public Map<String, Object> getParametersSchema() { return Map.of("path", Map.of("type", "string"), "content", Map.of("type", "string")); }
    public Object execute(Map<String, Object> args) throws Exception {
        String path = String.valueOf(args.get("path"));
        String content = String.valueOf(args.getOrDefault("content", ""));
        Files.createDirectories(Path.of(path).getParent() == null ? Path.of(".") : Path.of(path).getParent());
        Files.writeString(Path.of(path), content);
        Map<String, Object> r = new HashMap<>();
        r.put("written", path);
        return r;
    }
}
