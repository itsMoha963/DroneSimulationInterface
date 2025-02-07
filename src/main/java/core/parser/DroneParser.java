package core.parser;

import core.drone.Drone;

/**
 * The DroneParser class is responsible for parsing and validating drone data.
 * Implements the {@link JsonDroneParser} interface.
 *
 * @see JsonDroneParser
 */
public class DroneParser implements JsonDroneParser<Drone> {

    /**
     * Parses the given JSON object into a {@link Drone} object.
     *
     * @param obj The JSON object containing drone data.
     * @return A {@link Drone} object parsed from the JSON data.
     */
    @Override
    public Drone parse(org.json.JSONObject obj) {
        return new Drone(
                obj.getInt("id"),
                obj.getString("serialnumber"),
                obj.getString("carriage_type"),
                obj.getInt("carriage_weight"),
                obj.getString("dronetype"),
                obj.getString("created")
        );
    }

    /**
     * Validates if the given JSON object contains the required data for a Drone.
     *
     * @param obj The JSON object to validate.
     * @return {@code true} if the JSON object contains the required data, otherwise {@code false}.
     */
    @Override
    public boolean isValid(org.json.JSONObject obj) { return obj.has("carriage_type") && obj.has("carriage_weight"); }

    /**
     * Returns the endpoint for fetching drone data.
     *
     * @return A {@link String} representing the API endpoint for drone data.
     */
    @Override
    public String getEndpoint() { return "drones"; }
}