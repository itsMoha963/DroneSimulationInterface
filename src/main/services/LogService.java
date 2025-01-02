package src.main.services;

import src.main.core.LogEntry;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LogService {
    private static List<LogEntry> logs = new ArrayList<>();
    private static final String LOG_FILE_PATH = "logs.txt";

    public LogService() {
        // Add sample log entries
        logs.add(new LogEntry(LocalDateTime.now(), "INFO", "Lorem ipsum dolor sit amet, consectetur adipiscing elit."));
        logs.add(new LogEntry(LocalDateTime.now(), "ERROR", "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
        logs.add(new LogEntry(LocalDateTime.now(), "DEBUG", "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."));
        logs.add(new LogEntry(LocalDateTime.now(), "WARN", "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."));
        logs.add(new LogEntry(LocalDateTime.now(), "INFO", "testing the entry logs"));
        saveLogsToFile();
    }

    public List<LogEntry> getLogs() {
        return logs;
    }

    public static void addLog(LogEntry logEntry) {
        logs.add(logEntry);
        saveLogsToFile();
    }

    public static void log(String level, String message) {
        LogEntry logEntry = new LogEntry(LocalDateTime.now(), level, message);
        addLog(logEntry);
    }

    private static void saveLogsToFile() {
        try (FileWriter writer = new FileWriter(LOG_FILE_PATH)) {
            for (LogEntry logEntry : logs) {
                writer.write("Timestamp: " + logEntry.getTimestamp() + "\n");
                writer.write("Message: " + logEntry.getMessage() + "\n");
                writer.write("Level: " + logEntry.getLevel() + "\n");
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}