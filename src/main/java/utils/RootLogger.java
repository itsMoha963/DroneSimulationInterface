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
            configureFileHandler(debug);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Configures the FileHandler for the logger.
     * @param debug If true, console logging will be enabled.
     * @throws IOException If there is an error while creating the FileHandler.
     */
    private static void configureFileHandler(boolean debug) throws IOException {
        FileHandler fileHandler = getFileHandler();
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

    private static FileHandler getFileHandler() throws IOException {
        String logsDirPath = Constants.APP_DIRECTORY + "logs";
        File logsDir = new File(logsDirPath);
        if (!logsDir.exists()) {
            if (!logsDir.mkdirs()) {
                throw new IOException("Failed to create logs directory: " + logsDirPath);
            }
        }

        String logFilePath = logsDirPath + File.separator + "logs.txt";

        // If append is set to false, the log file gets cleared when the app is launched
        return new FileHandler(logFilePath, false);
    }
}
