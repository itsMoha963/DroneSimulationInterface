package utils;

import java.io.File;

/**
 * Class that hold Constants to avoid String/Path errors for Variables used in different classes.
 */
public final class Constants {
    // Icon Paths
    public static final String APP_ICON_PATH = "/icons/appicon.png";
    public static final String BATTERY_ICON_PATH = "/icons/battery.png";
    public static final String REFRESH_ICON_PATH = "/icons/refresh.png";
    public static final String FILTER_ICON_PATH = "/icons/filter.png";
    public static final String DRONE_ICON_PATH = "/icons/drone_light.png";
    public static final String DRONE_LIGHT_THEME_ICON_PATH = "/icons/drone.png";

    // Themes
    public static final String DEFAULT_THEME = "Dark";

    // Carriage Types
    public static final String NOT = "NOT";
    public static final String ALL = "ALL TYPES";
    public static final String ACT = "ACT";
    public static final String SEN = "SEN";

    public static final String SETTINGS_FILE = "settings.xml";
    public static final String CONFIG_FILE = "config.properties";
    public static final String APP_NAME = "DroneSimulationInterface";

    // APP Directory OS Independent
    public static final String APP_DIRECTORY = System.getProperty("user.home") + File.separator + APP_NAME
            + File.separator;
}