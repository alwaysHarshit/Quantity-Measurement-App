package com.apps.quantitymeasurement.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Central configuration holder for the plain Java application.
 *
 * Use case:
 * This class gives the application one place to load and read values from
 * {@code application.properties}. It is useful for logging settings, repository
 * selection, and future database configuration without introducing Spring Boot.
 */
public final class ApplicationConfig {

    /**
     * Logger for configuration loading events and fallback warnings.
     *
     * Use case:
     * Helps trace whether configuration was loaded correctly and whether the app
     * had to fall back to defaults.
     */
    private static final Logger LOGGER = Logger.getLogger(ApplicationConfig.class.getName());

    /**
     * Classpath file name that stores application settings.
     *
     * Use case:
     * Keeps the config file name in one constant so it can be reused safely.
     */
    private static final String CONFIG_FILE = "application.properties";

    /**
     * Single shared instance of the configuration object.
     *
     * Use case:
     * Ensures the properties file is loaded once and the same values are reused
     * across the whole application.
     */
    private static final ApplicationConfig INSTANCE = new ApplicationConfig();

    /**
     * In-memory store for all loaded configuration values.
     *
     * Use case:
     * Holds the raw key-value pairs read from the properties file.
     */
    private final Properties properties = new Properties();

    /**
     * Current runtime environment derived from config or default value.
     *
     * Use case:
     * Lets the application know whether it is running in development, testing,
     * or production mode.
     */
    private final Environment environment;

    /**
     * Supported runtime environments.
     *
     * Use case:
     * Avoids using free-form strings such as "dev" or "prod" throughout the code.
     */
    public enum Environment {
        DEVELOPMENT,
        TESTING,
        PRODUCTION
    }

    /**
     * Known configuration keys used by the application.
     *
     * Use case:
     * Prevents typos in property names and documents the keys expected by the app.
     */
    public enum ConfigKey {
        APP_ENV("app.env"),
        APP_LOGGING_LEVEL("app.logging.level"),
        APP_REPOSITORY_TYPE("app.repository.type"),
        DB_DRIVER("db.driver"),
        DB_URL("db.url"),
        DB_USERNAME("db.username"),
        DB_PASSWORD("db.password"),
        DB_POOL_SIZE("db.pool.size"),
        DB_TEST_QUERY("db.test.query");

        private final String key;

        ConfigKey(String key) {
            this.key = key;
        }

        /**
         * Returns the literal property name used in {@code application.properties}.
         *
         * Use case:
         * Allows calling code to read properties without hardcoding strings.
         */
        public String getKey() {
            return key;
        }
    }

    /**
     * Private constructor so callers must use {@link #getInstance()}.
     *
     * Use case:
     * Enforces singleton usage and guarantees configuration is loaded during startup.
     */
    private ApplicationConfig() {
        loadProperties();
        this.environment = resolveEnvironment();
    }

    /**
     * Returns the shared configuration instance.
     *
     * Use case:
     * Any class that needs app settings can call this method instead of creating
     * separate config objects.
     */
    public static ApplicationConfig getInstance() {
        return INSTANCE;
    }

    /**
     * Loads properties from {@code application.properties} on the classpath.
     *
     * Use case:
     * This is the step that makes the external config file actually control the
     * plain Java application.
     */
    private void loadProperties() {
        try (InputStream inputStream =
                     ApplicationConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                LOGGER.warning("Configuration file not found: " + CONFIG_FILE + ". Using defaults.");
                return;
            }

            properties.load(inputStream);
            LOGGER.info("Configuration loaded from " + CONFIG_FILE);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load configuration file: " + CONFIG_FILE, exception);
        }
    }

    /**
     * Resolves the runtime environment from config and falls back to development.
     *
     * Use case:
     * Keeps the app runnable even if no explicit environment has been configured yet.
     */
    private Environment resolveEnvironment() {
        String configuredEnvironment = getProperty(ConfigKey.APP_ENV, Environment.DEVELOPMENT.name());

        try {
            return Environment.valueOf(configuredEnvironment.trim().toUpperCase());
        } catch (IllegalArgumentException exception) {
            LOGGER.warning("Invalid app.env value: " + configuredEnvironment
                    + ". Falling back to DEVELOPMENT.");
            return Environment.DEVELOPMENT;
        }
    }

    /**
     * Returns the value for a raw string key.
     *
     * Use case:
     * Useful when a caller has a dynamic property name.
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Returns the value for a predefined config key.
     *
     * Use case:
     * Preferred method for normal application code because it avoids typo-prone
     * string literals.
     */
    public String getProperty(ConfigKey key, String defaultValue) {
        return getProperty(key.getKey(), defaultValue);
    }

    /**
     * Returns a required property and fails fast if it is missing.
     *
     * Use case:
     * Useful for mandatory values such as a real database URL in production.
     */
    public String getRequiredProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required configuration property: " + key);
        }
        return value.trim();
    }

    /**
     * Returns a required predefined property and fails fast if it is missing.
     *
     * Use case:
     * Same as {@link #getRequiredProperty(String)} but safer for known keys.
     */
    public String getRequiredProperty(ConfigKey key) {
        return getRequiredProperty(key.getKey());
    }

    /**
     * Returns an integer property or a default value if parsing fails.
     *
     * Use case:
     * Intended for future numeric settings such as pool size or timeout values.
     */
    public int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException exception) {
            LOGGER.warning("Invalid integer value for key " + key + ": " + value
                    + ". Using default: " + defaultValue);
            return defaultValue;
        }
    }

    /**
     * Returns the resolved environment name.
     *
     * Use case:
     * Lets startup code or diagnostics show which environment is active.
     */
    public Environment getEnvironment() {
        return environment;
    }
}
