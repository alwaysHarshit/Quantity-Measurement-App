package com.apps.quantitymeasurement.repository;

import com.apps.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.apps.quantitymeasurement.exceptions.DatabaseExceptions;
import com.apps.quantitymeasurement.utils.ConnectionPool;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository {

    private static final Logger LOGGER =
            Logger.getLogger(QuantityMeasurementDatabaseRepository.class.getName());

    private static final Set<String> LENGTH_UNITS =
            Set.of("FEET", "METERS", "CENTIMETERS", "INCHES", "YARDS", "CM");
    private static final Set<String> WEIGHT_UNITS =
            Set.of("KILOGRAM", "GRAM", "POUND");
    private static final Set<String> TEMPERATURE_UNITS =
            Set.of("KELVIN", "CELSIUS", "FAHRENHEIT");
    private static final Set<String> VOLUME_UNITS =
            Set.of("LITRE", "MILLILITRE", "GALLON");

    private static final String INSERT_QUERY =
            "INSERT INTO quantity_measurement_entity "
                    + "(this_value, this_unit, this_measurement_type, that_value, that_unit, "
                    + "that_measurement_type, operation, result_value, result_unit, "
                    + "result_measurement_type, result_string, is_error, error_message, "
                    + "created_at, updated_at) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

    private static final String SELECT_ALL_QUERY =
            "SELECT * FROM quantity_measurement_entity ORDER BY created_at DESC";

    private static final String SELECT_BY_OPERATION =
            "SELECT * FROM quantity_measurement_entity WHERE operation = ? ORDER BY created_at DESC";

    private static final String SELECT_BY_MEASUREMENT_TYPE =
            "SELECT * FROM quantity_measurement_entity "
                    + "WHERE this_measurement_type = ? ORDER BY created_at DESC";

    private static final String DELETE_ALL_QUERY =
            "DELETE FROM quantity_measurement_entity";

    private static final String COUNT_QUERY =
            "SELECT COUNT(*) FROM quantity_measurement_entity";

    private static QuantityMeasurementDatabaseRepository instance;

    private final ConnectionPool connectionPool;

    private QuantityMeasurementDatabaseRepository() {
        try {
            this.connectionPool = ConnectionPool.getInstance();
            initializeDatabase();
        } catch (SQLException exception) {
            throw DatabaseExceptions.connectionFailed("repository initialization", exception);
        }
    }

    public static synchronized QuantityMeasurementDatabaseRepository getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementDatabaseRepository();
        }
        return instance;
    }

    private void initializeDatabase() {
        String schema = loadSchema();
        String createTableStatement = extractCreateTableStatement(schema);

        Connection connection = null;
        Statement statement = null;
        try {
            connection = connectionPool.getConnection();
            statement = connection.createStatement();
            statement.execute(createTableStatement);
            LOGGER.info("Database schema ensured for quantity_measurement_entity");
        } catch (SQLException exception) {
            if ("No database selected".equalsIgnoreCase(exception.getMessage())) {
                throw DatabaseExceptions.queryFailed(
                        "schema initialization: update db.url to include the database name, "
                                + "for example jdbc:mysql://localhost:3306/quantity_measurement",
                        exception);
            }
            throw DatabaseExceptions.queryFailed("schema initialization", exception);
        } finally {
            closeResources(statement, connection);
        }
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {
        OperandDetails leftOperand = parseOperand(entity.getLeftOperand());
        OperandDetails rightOperand = parseOperand(entity.getRightOperand());
        OperandDetails resultOperand = parseOperand(entity.getResult());

        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(INSERT_QUERY);

            bindOperand(statement, 1, leftOperand, true);
            bindOperand(statement, 4, rightOperand, false);
            statement.setString(7, entity.getOperation());
            bindResult(statement, 8, resultOperand, entity.getResult());
            statement.setBoolean(12, entity.hasError());
            statement.setString(13, entity.getErrorMessage());

            statement.executeUpdate();
            LOGGER.info("Measurement saved for operation: " + entity.getOperation());
        } catch (SQLException exception) {
            throw DatabaseExceptions.queryFailed("save quantity_measurement_entity", exception);
        } finally {
            closeResources(statement, connection);
        }
    }

    public List<QuantityMeasurementEntity> getAllMeasurements() {
        List<QuantityMeasurementEntity> measurements = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SELECT_ALL_QUERY);

            while (resultSet.next()) {
                measurements.add(mapResultSetToEntity(resultSet));
            }

            LOGGER.info("Fetched measurements count: " + measurements.size());
            return measurements;
        } catch (SQLException exception) {
            throw DatabaseExceptions.queryFailed("select all measurements", exception);
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation) {
        List<QuantityMeasurementEntity> measurements = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(SELECT_BY_OPERATION);
            statement.setString(1, operation);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                measurements.add(mapResultSetToEntity(resultSet));
            }

            return measurements;
        } catch (SQLException exception) {
            throw DatabaseExceptions.queryFailed("select by operation", exception);
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    public List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType) {
        List<QuantityMeasurementEntity> measurements = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(SELECT_BY_MEASUREMENT_TYPE);
            statement.setString(1, measurementType);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                measurements.add(mapResultSetToEntity(resultSet));
            }

            return measurements;
        } catch (SQLException exception) {
            throw DatabaseExceptions.queryFailed("select by measurement type", exception);
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    public int getTotalCount() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(COUNT_QUERY);

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        } catch (SQLException exception) {
            throw DatabaseExceptions.queryFailed("count quantity_measurement_entity", exception);
        } finally {
            closeResources(resultSet, statement, connection);
        }
    }

    public void deleteAll() {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = connectionPool.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(DELETE_ALL_QUERY);
            LOGGER.info("Deleted all quantity measurements from database");
        } catch (SQLException exception) {
            throw DatabaseExceptions.queryFailed("delete all quantity_measurement_entity", exception);
        } finally {
            closeResources(statement, connection);
        }
    }

    public String getPoolStatistics() {
        return "ConnectionPool{available=" + connectionPool.getAvailableConnectionCount()
                + ", used=" + connectionPool.getUsedConnectionCount()
                + ", total=" + connectionPool.getTotalConnectionCount()
                + "}";
    }

    public void releaseResources() {
        connectionPool.closeAll();
    }

    private QuantityMeasurementEntity mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        String operation = resultSet.getString("operation");
        String leftOperand = buildOperand(
                resultSet.getDouble("this_value"),
                resultSet.getString("this_unit"),
                false);
        String rightOperand = buildOperand(
                resultSet.getDouble("that_value"),
                resultSet.getString("that_unit"),
                resultSet.wasNull());

        boolean isError = resultSet.getBoolean("is_error");
        String errorMessage = resultSet.getString("error_message");
        String result = resultSet.getString("result_string");

        if (isError) {
            return QuantityMeasurementEntity.error(operation, leftOperand, rightOperand, errorMessage);
        }

        return new QuantityMeasurementEntity(operation, leftOperand, rightOperand, result);
    }

    private void bindOperand(PreparedStatement statement, int startIndex, OperandDetails operand, boolean required)
            throws SQLException {
        if (operand == null) {
            if (required) {
                throw new SQLException("Left operand cannot be null");
            }

            statement.setNull(startIndex, Types.DOUBLE);
            statement.setNull(startIndex + 1, Types.VARCHAR);
            statement.setNull(startIndex + 2, Types.VARCHAR);
            return;
        }

        statement.setDouble(startIndex, operand.value());
        statement.setString(startIndex + 1, operand.unit());
        statement.setString(startIndex + 2, operand.measurementType());
    }

    private void bindResult(PreparedStatement statement, int startIndex, OperandDetails operand, String resultText)
            throws SQLException {
        if (operand == null) {
            statement.setNull(startIndex, Types.DOUBLE);
            statement.setNull(startIndex + 1, Types.VARCHAR);
            statement.setNull(startIndex + 2, Types.VARCHAR);
        } else {
            statement.setDouble(startIndex, operand.value());
            statement.setString(startIndex + 1, operand.unit());
            statement.setString(startIndex + 2, operand.measurementType());
        }

        statement.setString(11, resultText);
    }

    private OperandDetails parseOperand(String operandText) {
        if (operandText == null || operandText.isBlank()) {
            return null;
        }

        String[] parts = operandText.trim().split("\\s+", 2);
        if (parts.length < 2) {
            throw new IllegalArgumentException("Operand must contain value and unit: " + operandText);
        }

        double value = Double.parseDouble(parts[0]);
        String unit = parts[1].trim().toUpperCase();
        return new OperandDetails(value, unit, inferMeasurementType(unit));
    }

    private String inferMeasurementType(String unit) {
        if (LENGTH_UNITS.contains(unit)) {
            return "LengthUnit";
        }
        if (WEIGHT_UNITS.contains(unit)) {
            return "WeightUnit";
        }
        if (TEMPERATURE_UNITS.contains(unit)) {
            return "TemperatureUnit";
        }
        if (VOLUME_UNITS.contains(unit)) {
            return "VolumeUnit";
        }
        return "Unknown";
    }

    private String buildOperand(double value, String unit, boolean wasNull) {
        if (wasNull || unit == null || unit.isBlank()) {
            return null;
        }
        return value + " " + unit;
    }

    private String loadSchema() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db/schema.sql")) {
            if (inputStream == null) {
                throw new IllegalStateException("Schema file not found: db/schema.sql");
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read db/schema.sql", exception);
        }
    }

    private String extractCreateTableStatement(String schema) {
        int startIndex = schema.indexOf("CREATE TABLE IF NOT EXISTS quantity_measurement_entity");
        int endIndex = schema.indexOf(";", startIndex);

        if (startIndex < 0 || endIndex < 0) {
            throw new IllegalStateException("Could not find quantity_measurement_entity create statement in schema.sql");
        }

        return schema.substring(startIndex, endIndex + 1)
                .replaceAll("\\s+", " ")
                .trim();
    }

    private void closeResources(ResultSet resultSet, Statement statement, Connection connection) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException exception) {
                LOGGER.warning("Failed to close result set: " + exception.getMessage());
            }
        }
        closeResources(statement, connection);
    }

    private void closeResources(Statement statement, Connection connection) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException exception) {
                LOGGER.warning("Failed to close statement: " + exception.getMessage());
            }
        }

        if (connection != null) {
            connectionPool.releaseConnection(connection);
        }
    }

    private record OperandDetails(double value, String unit, String measurementType) {
    }
}
