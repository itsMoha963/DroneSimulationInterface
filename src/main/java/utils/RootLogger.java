package utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

/**
 * The {@code RootLogger} class initializes and configures the logger.
 * Optionally enables console logging.
 */
public class RootLogger {

    /**
     * Initializes the logger with a FileHandler and optional debug mode.
     * @param debug If true, logs are also displayed in the console.
     */
    public static void initializeLogger(boolean debug) {
        try {
            createLogDirectory();
            configureFileHandler(debug);
        } catch (IOException e) {
            throw new SecurityException("Failed to create logs directory.");
        }
    }

    /**
     * Creates the logs directory if it does not exist.
     * @throws SecurityException If the application does not have permissions to create the directory.
     */
    private static void createLogDirectory() throws SecurityException {
        File logsDir = new File("logs");
        if (!logsDir.exists() && !logsDir.mkdirs()) {
            throw new SecurityException("Failed to create logs directory: logs");
        }
    }

    /**
     * Configures the FileHandler for the logger.
     * @param debug If true, console logging will be enabled.
     * @throws IOException If there is an error while creating the FileHandler.
     */
    private static void configureFileHandler(boolean debug) throws IOException {
        String logsDirPath = "logs" + File.separator + "dsi_logs.txt";
        FileHandler fileHandler = new FileHandler(logsDirPath, false);
        fileHandler.setFormatter(new SimpleFormatter());

        Logger rootLogger = Logger.getLogger("");
        rootLogger.addHandler(fileHandler);

        if (!debug) {
            for (Handler handler : rootLogger.getHandlers()) {
                if (handler instanceof ConsoleHandler) {
                    rootLogger.removeHandler(handler);
                }
            }
        }
    }
}
