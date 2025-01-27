package services;

import static java.lang.Math.*;

public final class Helper {

    /**
     * Extracts the drone id from the given {@code url}
     * @param url
     * @return Drone ID
     */
    public static int extractDroneIDFromUrl(String url) {
        try {
            String[] parts = url.split("/");
            return Integer.parseInt(parts[parts.length - 2]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Invalid URL format for extracting drone ID: " + url, e);
        }
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
        static double Earth_Radius = 6378137.0; //Earth´s radius in meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(long2 - long1);

        double a = sin(dLat / 2) * sin(dLat / 2) + cos(toRadians(lat1)) * cos(toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2);

        double c = 2 * atan2(sqrt(a), sqrt(1 - a));

        return Earth_Radius * c;
    }
}