package src.main.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogEntry {
    private LocalDateTime timestamp;
    private String level;
    private String message;


    public LogEntry(LocalDateTime timestamp, String level, String message) {
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter);
    }
}