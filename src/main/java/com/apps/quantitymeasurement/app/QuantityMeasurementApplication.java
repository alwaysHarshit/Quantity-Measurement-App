package com.apps.quantitymeasurement.app;

import com.apps.quantitymeasurement.controller.QuantityMeasurementController;
import com.apps.quantitymeasurement.dto.QuantityDTO;
import com.apps.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.apps.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.apps.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;
import com.apps.quantitymeasurement.service.QuantityMeasurementServiceImpl;
import com.apps.quantitymeasurement.utils.ApplicationConfig;
import com.apps.quantitymeasurement.utils.DatabaseConfig;

import java.util.Locale;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class QuantityMeasurementApplication {

    private static final Logger LOGGER = Logger.getLogger(QuantityMeasurementApplication.class.getName());

    private static QuantityMeasurementApplication applicationInstance;
    private final QuantityMeasurementController controller;

    private QuantityMeasurementApplication() {
        IQuantityMeasurementRepository repository = QuantityMeasurementDatabaseRepository.getInstance();
        LOGGER.info("Repository initialized: " + repository.getClass().getSimpleName());

        QuantityMeasurementServiceImpl service = new QuantityMeasurementServiceImpl(repository);
        LOGGER.info("Service initialized: " + service.getClass().getSimpleName());

        this.controller = new QuantityMeasurementController(service);
        LOGGER.info("Controller initialized: " + controller.getClass().getSimpleName());
    }

    public static QuantityMeasurementApplication getInstance() {
        if (applicationInstance == null) {
            applicationInstance = new QuantityMeasurementApplication();
        }
        return applicationInstance;
    }


    public static void main(String[] args) {
        configureLogging();

        ApplicationConfig config = ApplicationConfig.getInstance();
        QuantityMeasurementApplication application = QuantityMeasurementApplication.getInstance();
        DatabaseConfig databaseConfig = DatabaseConfig.load();

        LOGGER.info("Application startup");
        LOGGER.info("Environment: " + config.getEnvironment().name());
        LOGGER.info("Repository type: "
                + config.getProperty(ApplicationConfig.ConfigKey.APP_REPOSITORY_TYPE, "cache"));
        LOGGER.info("Log level: "
                + config.getProperty(ApplicationConfig.ConfigKey.APP_LOGGING_LEVEL, "INFO"));
        LOGGER.info("Database URL: "
                + (databaseConfig.getUrl().isBlank() ? "not configured" : databaseConfig.getUrl()));

        QuantityDTO quantity1 = new QuantityDTO(1, QuantityDTO.LengthUnit.FEET);
        QuantityDTO quantity2 = new QuantityDTO(12, QuantityDTO.LengthUnit.INCHES);

        QuantityDTO result = application.controller.performAddition(quantity1, quantity2);
        LOGGER.info("Sample operation result: "
                + application.controller.handleResult(quantity1, quantity2, "+", result).trim());
    }

    private static void configureLogging() {
        String configuredLevel = ApplicationConfig.getInstance().getProperty("app.logging.level", "INFO");
        Level level = parseLevel(configuredLevel);

        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(level);

        for (Handler handler : rootLogger.getHandlers()) {
            handler.setLevel(level);
            handler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    return String.format("[%s] %s%n", record.getLevel().getName(), record.getMessage());
                }
            });
        }
    }

    private static Level parseLevel(String configuredLevel) {
        try {
            return Level.parse(configuredLevel.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            return Level.INFO;
        }
    }
}
