package sovereign.util;

import java.io.*;
import java.nio.file.*;

public class FileStore {
    public static void save(String path, String content) throws Exception {
        Files.createDirectories(Path.of(path).getParent() == null ? Path.of(".") : Path.of(path).getParent());
        Files.writeString(Path.of(path), content);
    }
    public static String load(String path) throws Exception {
        return Files.exists(Path.of(path)) ? Files.readString(Path.of(path)) : "";
    }
}
