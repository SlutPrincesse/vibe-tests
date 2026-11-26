package sovereign.plugins;

public interface Plugin {
    String name();
    default void onLoad() {}
}
