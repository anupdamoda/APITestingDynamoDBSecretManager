package org.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {

    private static final Properties PROPS = load();

    private Config() {}

    private static Properties load() {
        Properties p = new Properties();
        try (InputStream in = Config.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (in != null) {
                p.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config.properties", e);
        }
        return p;
    }

    public static String get(String key) {
        String env = System.getenv(toEnvKey(key));
        if (env != null && !env.isEmpty()) return env;

        String sys = System.getProperty(key);
        if (sys != null && !sys.isEmpty()) return sys;

        String val = PROPS.getProperty(key);
        if (val == null) {
            throw new IllegalStateException("Missing config key: " + key);
        }
        return val;
    }

    public static String baseUrl() {
        return get("base.url");
    }

    public static String testUserEmail() {
        return get("test.user.email");
    }

    public static String testUserPassword() {
        return get("test.user.password");
    }

    public static String invalidPassword() {
        return get("test.user.invalid.password");
    }

    private static String toEnvKey(String key) {
        return key.toUpperCase().replace('.', '_');
    }

    private static Properties properties = new Properties();

    static {
        try (InputStream input =
                     Config.class.getClassLoader()
                             .getResourceAsStream("config.properties")) {

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Unable to load config.properties", e);
        }
    }
}
