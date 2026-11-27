
package com.bridgelabz.quantitymeasurement.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * UC16: Loads application configuration.
 * JVM system properties can override file properties.
 */
public final class ApplicationConfig {

    private final Properties properties = new Properties();

    public ApplicationConfig() {
        try (InputStream input = getClass()
                .getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input != null) {
                properties.load(input);
            }

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to load application configuration",
                    exception);
        }
    }

    public String get(String key, String defaultValue) {
        String override = System.getProperty(key);

        if (override != null) {
            return override;
        }

        return properties.getProperty(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return Integer.parseInt(
                get(key, String.valueOf(defaultValue)));
    }

    public String getRepositoryType() {
        return get("repository.type", "database");
    }

    public String getDatabaseUrl() {
        return get("database.url",
                "jdbc:h2:mem:quantity_dev;DB_CLOSE_DELAY=-1");
    }

    public String getDatabaseUsername() {
        return get("database.username", "sa");
    }

    public String getDatabasePassword() {
        return get("database.password", "");
    }
}
