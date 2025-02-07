package core.parser;

import org.json.JSONObject;
import core.drone.DroneType;

/**
 * The DroneTypeParser class is responsible for parsing and validating drone type data.
 * Implements the {@link JsonDroneParser} interface.
 *
 * @see JsonDroneParser
 */
public class DroneTypeParser implements JsonDroneParser<DroneType> {

    /**
     * Parses the given JSON object into a {@link DroneType} object.
     *
     * @param obj The JSON object containing drone type data.
     * @return A {@link DroneType} object parsed from the JSON data.
     */
    @Override
    public DroneType parse(JSONObject obj) {
        return new DroneType(
                obj.getInt("id"),
                obj.getString("manufacturer"),
                obj.getString("typename"),
                obj.getInt("weight"),
                obj.getInt("max_speed"),
                obj.getInt("battery_capacity"),
                obj.getInt("control_range"),
                obj.getInt("max_carriage")
        );
    }

    /**
     * Validates if the given JSON object contains the required data for a DroneType.
     *
     * @param obj The JSON object to validate.
     * @return {@code true} if the JSON object contains the required fields, otherwise {@code false}.
     */
    @Override
    public boolean isValid(JSONObject obj) { return obj.has("id") && obj.has("manufacturer"); }

    /**
     * Returns the endpoint for fetching drone type data.
     *
     * @return A {@link String} representing the API endpoint for drone types.
     */
    @Override
    public String getEndpoint() { return "dronetypes"; }
}