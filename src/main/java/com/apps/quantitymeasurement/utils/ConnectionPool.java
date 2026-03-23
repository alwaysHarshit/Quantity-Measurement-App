package com.apps.quantitymeasurement.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public final class ConnectionPool {
    private static final Logger LOGGER = Logger.getLogger(ConnectionPool.class.getName());

    private static ConnectionPool instance;

    private final List<Connection> availableConnections;
    private final List<Connection> usedConnections;
    private final int poolSize;
    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;
    private final String driverClass;
    private final String testQuery;

    private ConnectionPool() throws SQLException {
        DatabaseConfig databaseConfig = DatabaseConfig.load();

        this.poolSize = databaseConfig.getPoolSize();
        this.dbUrl = databaseConfig.getUrl();
        this.dbUsername = databaseConfig.getUsername();
        this.dbPassword = databaseConfig.getPassword();
        this.driverClass = databaseConfig.getDriverClassName();
        this.testQuery = databaseConfig.getTestQuery();
        this.availableConnections = new ArrayList<>();
        this.usedConnections = new ArrayList<>();

        validateConfiguration();
        loadDriver();
        initializeConnections();

        LOGGER.info("Connection pool initialized with size: " + poolSize);
    }

    public static synchronized ConnectionPool getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    private void validateConfiguration() throws SQLException {
        if (dbUrl == null || dbUrl.isBlank()) {
            throw new SQLException("Missing database configuration: db.url");
        }
        if (driverClass == null || driverClass.isBlank()) {
            throw new SQLException("Missing database configuration: db.driver");
        }
        if (poolSize <= 0) {
            throw new SQLException("Invalid pool size: " + poolSize);
        }
    }

    private void loadDriver() throws SQLException {
        try {
            Class.forName(driverClass);
            LOGGER.info("JDBC driver loaded: " + driverClass);
        } catch (ClassNotFoundException exception) {
            throw new SQLException("Unable to load JDBC driver: " + driverClass, exception);
        }
    }

    private void initializeConnections() throws SQLException {
        for (int count = 0; count < poolSize; count++) {
            availableConnections.add(createConnection());
        }
    }

    private Connection createConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        LOGGER.info("Created new database connection");
        return connection;
    }

    public synchronized Connection getConnection() throws SQLException {
        if (!availableConnections.isEmpty()) {
            Connection connection = availableConnections.removeFirst();

            if (!validateConnection(connection)) {
                closeQuietly(connection);
                connection = createConnection();
            }

            usedConnections.add(connection);
            LOGGER.info("Connection acquired. Available=" + availableConnections.size()
                    + ", Used=" + usedConnections.size());
            return connection;
        }

        if (getTotalConnectionCount() < poolSize) {
            Connection connection = createConnection();
            usedConnections.add(connection);
            LOGGER.info("Connection created on demand. Available=" + availableConnections.size()
                    + ", Used=" + usedConnections.size());
            return connection;
        }

        throw new SQLException("No available connection in the pool");
    }

    public synchronized void releaseConnection(Connection connection) {
        if (connection == null) {
            return;
        }

        if (!usedConnections.remove(connection)) {
            LOGGER.warning("Attempted to release a connection not managed by this pool");
            return;
        }

        if (validateConnection(connection)) {
            availableConnections.add(connection);
        } else {
            closeQuietly(connection);
            LOGGER.warning("Released connection was invalid and has been closed");
        }

        LOGGER.info("Connection released. Available=" + availableConnections.size()
                + ", Used=" + usedConnections.size());
    }

    public boolean validateConnection(Connection connection) {
        if (connection == null) {
            return false;
        }

        try {
            if (connection.isClosed()) {
                return false;
            }

            if (testQuery == null || testQuery.isBlank()) {
                return connection.isValid(2);
            }

            try (Statement statement = connection.createStatement()) {
                statement.execute(testQuery);
                return true;
            }
        } catch (SQLException exception) {
            LOGGER.warning("Connection validation failed: " + exception.getMessage());
            return false;
        }
    }

    public synchronized void closeAll() {
        for (Connection connection : availableConnections) {
            closeQuietly(connection);
        }

        for (Connection connection : usedConnections) {
            closeQuietly(connection);
        }

        availableConnections.clear();
        usedConnections.clear();
        LOGGER.info("All pooled connections have been closed");
    }

    public int getAvailableConnectionCount() {
        return availableConnections.size();
    }

    public int getUsedConnectionCount() {
        return usedConnections.size();
    }

    public int getTotalConnectionCount() {
        return availableConnections.size() + usedConnections.size();
    }

    @Override
    public String toString() {
        return "ConnectionPool{"
                + "available=" + availableConnections.size()
                + ", used=" + usedConnections.size()
                + ", poolSize=" + poolSize
                + '}';
    }

    private void closeQuietly(Connection connection) {
        try {
            connection.close();
        } catch (SQLException exception) {
            LOGGER.warning("Failed to close connection: " + exception.getMessage());
        }
    }

    public static void main(String[] args) throws SQLException {
        ConnectionPool connectionPool=new ConnectionPool();
        System.out.println(connectionPool);
    }
}
