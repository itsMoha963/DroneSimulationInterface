package services;

public final class ConfigManger {
    private static ConfigManger instance;

    public static ConfigManger getInstance() {
        if (instance == null) {
            instance = new ConfigManger();
        }
        return instance;
    }

    private ConfigManger() {

    }

    private void setTheme(String theme) {

    }

    private String getTheme() {
        return "";
    }

    private String getToken() {
        return "";
    }
}
