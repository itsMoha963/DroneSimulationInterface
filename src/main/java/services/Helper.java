package services;

public final class Helper {

    /**
     * Extracts the drone id from the given {@code url}
     * @param url
     * @return Drone ID
     */
    public static int extractID(String url) {
        int droneId = -1;
        String[] parts = url.split("/");
        droneId = Integer.parseInt(parts[parts.length - 2]);
        return droneId;
    }
}
