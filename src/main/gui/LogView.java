package src.main.gui;

import com.formdev.flatlaf.ui.FlatLineBorder;
import src.main.core.LogEntry;
import src.main.services.LogService;
import src.main.utils.Colors;
import  java.time.LocalDateTime;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogView extends JPanel {
    private JPanel innerContentPanel;
    private static final String LOG_FILE_PATH = "logs.txt";

    public LogView(LogService logService) {
        setLayout(new BorderLayout());
        setBackground(Colors.EERIE_BLACK);

        innerContentPanel = new JPanel();
        innerContentPanel.setBackground(Colors.EERIE_BLACK);
        innerContentPanel.setLayout(new BoxLayout(innerContentPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(innerContentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);

        updateLogView();
    }

    private void updateLogView() {
        try {
            List<LogEntry> logEntries = readLogsFromFile();

            innerContentPanel.removeAll();
            for (LogEntry logEntry : logEntries) {
                innerContentPanel.add(createLogEntryPanel(logEntry));
                innerContentPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space between cards
            }

            innerContentPanel.revalidate();
            innerContentPanel.repaint();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading log entries: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<LogEntry> readLogsFromFile() {
        List<LogEntry> logEntries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Timestamp: ")) {
                    String timestamp = line.substring(11);
                    String message = reader.readLine().substring(9);
                    String level = reader.readLine().substring(7);
                    reader.readLine(); // Skip empty line
                    logEntries.add(new LogEntry(LocalDateTime.parse(timestamp), level, message));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logEntries;
    }

    private JPanel createLogEntryPanel(LogEntry logEntry) {
        JPanel logEntryPanel = new JPanel();
        logEntryPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
        logEntryPanel.setLayout(new GridLayout(4, 1));
        logEntryPanel.setBackground(Colors.GUNMETAL);
        logEntryPanel.setMaximumSize(new Dimension(800, 100));

        logEntryPanel.add(createLabelHelper("Timestamp: " + logEntry.getTimestamp()));
        logEntryPanel.add(createLabelHelper("Message: " + logEntry.getMessage()));
        logEntryPanel.add(createLabelHelper("Level: " + logEntry.getLevel()));

        logEntryPanel.setBorder(new FlatLineBorder(new Insets(8, 8, 8, 8), null, 0, 16));

        return logEntryPanel;
    }

    private JLabel createLabelHelper(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(Colors.PLATINUM);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        return label;
    }
}