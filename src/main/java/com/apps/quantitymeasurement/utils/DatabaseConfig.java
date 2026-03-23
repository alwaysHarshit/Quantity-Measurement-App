package com.apps.quantitymeasurement.utils;

public final class DatabaseConfig {

    private final String url;
    private final String username;
    private final String password;
    private final String driverClassName;
    private final int poolSize;
    private final String testQuery;

    private DatabaseConfig(ApplicationConfig config) {
        this.url = config.getProperty(ApplicationConfig.ConfigKey.DB_URL, "");
        this.username = config.getProperty(ApplicationConfig.ConfigKey.DB_USERNAME, "");
        this.password = config.getProperty(ApplicationConfig.ConfigKey.DB_PASSWORD, "");
        this.driverClassName = config.getProperty(ApplicationConfig.ConfigKey.DB_DRIVER, "");
        this.poolSize = config.getIntProperty(ApplicationConfig.ConfigKey.DB_POOL_SIZE.getKey(), 5);
        this.testQuery = config.getProperty(ApplicationConfig.ConfigKey.DB_TEST_QUERY, "SELECT 1");
    }

    public static DatabaseConfig load() {
        return new DatabaseConfig(ApplicationConfig.getInstance());
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public String getTestQuery() {
        return testQuery;
    }
}
