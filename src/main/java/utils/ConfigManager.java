package utils;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The ConfigManager is a class that helps manage the apps configuration.
 * It handles loading, saving and writing to the config file.
 */
public class ConfigManager {

    private static final Logger log = Logger.getLogger(ConfigManager.class.getName());

    // Config file path is used as a class variable
    // Here the path is defined via the constructor or a system property.
    private final String configFile;
    private final Properties properties;

    /**
     * Default constructor for ConfigManager.
     * Initializes the configuration directory and file path.
     * If the directory does not exist, it attempts to create it.
     *
     * @throws SecurityException if the config directory cannot be created
     */
    public ConfigManager() {
        String configDirPath = Constants.APP_DIRECTORY + "config";
        File configDir = new File(configDirPath);
        if (!configDir.exists() && !configDir.mkdirs()) {
            throw new SecurityException("Failed to create config directory: " + configDirPath);
        }

        this.configFile = configDirPath + File.separator + "config.properties";
        properties = new Properties();
        loadConfig();
    }

    private void loadConfig() {
        File file = new File(configFile);
        try (FileInputStream fis = new FileInputStream(file)) {
            properties.load(fis);
            log.log(Level.INFO, "Configuration loaded from: " + file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            log.log(Level.WARNING, "Config file not found at " + file.getAbsolutePath() + ". Creating a new config file.");
            saveConfig();  // If the file does not exist, a new one is created.
        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to load config file: " + e.getMessage(), e);
        }
    }

    /**
     * Save the current properties to the configuration file.
     * Called whenever a change is made.
     */
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

    /**
     * Returns the default theme
     * @return The Default Theme
     */
    public String getTheme() {
        return properties.getProperty("theme", Constants.DEFAULT_THEME);
    }

    /**
     * Writes the new theme to the config file
     * @param theme The new theme to set.
     */
    public void setTheme(String theme) {
        properties.setProperty("theme", theme);
        saveConfig();
    }
}
