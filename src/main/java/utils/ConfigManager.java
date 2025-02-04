package utils;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigManager {
    private static final Logger log = Logger.getLogger(ConfigManager.class.getName());

    // Config file path is used as a class variable
    // Here the path is defined via the constructor or a system property.
    private final String configFile;
    private final Properties properties;

    /**
     * Constructor, of the ConfigManager class. It loads the configuration file.
     * If the file does not exist, a new one is created.
     *
     *
     * @param configFile Path to the configuration file.
     */
    public ConfigManager(String configFile) {
        this.configFile = configFile;
        properties = new Properties();
        loadConfig();
    }

    /**
     * Default constructor, of the ConfigManager class.
     * This prevents the path from being “hard” written in the code.
     */
    public ConfigManager() {
        // The default path is the user's home directory
        this(System.getProperty("user.home") + File.separator + "config.properties");
    }

    private void loadConfig() {
        File file = new File(configFile);
        try (FileInputStream fis = new FileInputStream(file)) {
            properties.load(fis);
            log.info("Configuration loaded from: " + file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            log.warning("Config file not found at " + file.getAbsolutePath() + ". Creating a new config file.");
            saveConfig();  // If the file does not exist, a new one is created.
        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to load config file: " + e.getMessage(), e);
        }
    }

    public void saveConfig() {
        File file = new File(configFile);
        System.out.println("Saving config in: " + file.getAbsolutePath());  // gives the path of the config file

        try (FileOutputStream fos = new FileOutputStream(file)) {
            properties.store(fos, "App Configuration");
            log.info("Configuration saved.");
        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to save config file: " + e.getMessage(), e);
        }
    }

    public String getTheme() {
        return properties.getProperty("theme", "Dark");  // Default: Dark Mode
    }

    public void setTheme(String theme) {
        properties.setProperty("theme", theme);
        saveConfig();  // save the new theme in the config file
    }
}
