package utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class RootLogger {

    /**
     * If debug is ticked then the logs are displayed in the console.
     * @param debug
     * @throws IOException
     */
    public static void init(boolean debug) throws IOException {
        // Create Log file/dir if it does not exist
        File logsDir = new File("logs");
        if (!logsDir.exists()) {
            logsDir.mkdirs();
        }

        String logsDirPath = "logs" + File.separator + "dsi_logs.txt";

        // If append is false, then the file gets reset everytime the App gets started
        FileHandler fh = new FileHandler(logsDirPath, false);
        fh.setFormatter(new SimpleFormatter());

        Logger rootLogger = Logger.getLogger("");
        rootLogger.addHandler(fh);

        // Console logging
        if (!debug) {
            Handler[] handlers = rootLogger.getHandlers();
            for (Handler handler : handlers) {
                if (handler instanceof ConsoleHandler) {
                    rootLogger.removeHandler(handler);
                }
            }
        }
    }
}
