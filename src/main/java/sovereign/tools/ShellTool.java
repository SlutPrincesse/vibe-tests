package sovereign.tools;

import java.io.*;
import java.util.*;

public class ShellTool implements Tool {
    public String getName() { return "shell"; }
    public String getDescription() { return "Execute a shell command"; }
    public Map<String, Object> getParametersSchema() {
        Map<String, Object> s = new HashMap<>();
        s.put("command", Map.of("type", "string"));
        return s;
    }
    public Object execute(Map<String, Object> args) throws Exception {
        String cmd = String.valueOf(args.getOrDefault("command", ""));
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        StringBuilder out = new StringBuilder();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = r.readLine()) != null) out.append(line).append("\n");
        }
        p.waitFor();
        Map<String, Object> res = new HashMap<>();
        res.put("output", out.toString());
        res.put("exitCode", p.exitValue());
        return res;
    }
}
