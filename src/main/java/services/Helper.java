package services;

import static java.lang.Math.*;

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

    /**
     * Return the distance in meters between the 2 coordinates
     * @param long1
     * @param lat1
     * @param long2
     * @param lat2
     * @return
     */
    public static double haversineDistance(double long1, double lat1, double long2, double lat2) {
        double r = 6378137.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(long2 - long1);

        double a = sin(dLat / 2) * sin(dLat / 2) + cos(toRadians(lat1)) * cos(toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2);

        double c = 2 * atan2(sqrt(a), sqrt(1 - a));

        return r * c;
    }
}