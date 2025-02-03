package services;

import static java.lang.Math.*;

/**
 * The {@code Helper} class provides utility methods for common operations
 * such as extracting drone IDs from URLs and calculating distances between two coordinates
 */
public final class Helper {

    /**
     * Extracts the drone ID from the given URL. The drone ID is assumed to be
     * located as the second-to-last segment of the URL, separated by slashes.
     *
     * @param url The URL containing the drone ID.
     * @return The extracted drone ID.
     * @throws IllegalArgumentException If the URL format is invalid or the drone ID cannot be parsed.
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
     * Calculates the distance in meters between two coordinates using the Haversine formula.
     *
     * @param long1 Longitude of the first point.
     * @param lat1 Latitude of the first point.
     * @param long2 Longitude of the second point.
     * @param lat2 Latitude of the second point.
     * @return The distance in meters between the two points on earth.
     */
    public static double haversineDistance(double long1, double lat1, double long2, double lat2) {
        double Earth_Radius = 6378137.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(long2 - long1);

        double a = sin(dLat / 2) * sin(dLat / 2) + cos(toRadians(lat1)) * cos(toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2);

        double c = 2 * atan2(sqrt(a), sqrt(1 - a));

        return Earth_Radius * c;
    }
}
