package core.parser;

import org.json.JSONObject;
import core.drone.DynamicDrone;

/**
 * Parser for JSON objects into DynamicDrone.
 * Implements the {@link JsonDroneParser} interface.
 *
 * @see JsonDroneParser
 */
public class DynamicDroneParser implements JsonDroneParser<DynamicDrone> {

    /**
     * Parses the given JSON object into a {@link DynamicDrone} object.
     *
     * @param obj The JSON object containing dynamic drone data.
     * @return A {@link DynamicDrone} object parsed from the JSON data.
     */
    @Override
    public DynamicDrone parse(JSONObject obj) {
        return new DynamicDrone(
                obj.getString("drone"),
                obj.getString("timestamp"),
                obj.getInt("speed"),
                obj.getDouble("align_roll"),
                obj.getDouble("align_pitch"),
                obj.getDouble("align_yaw"),
                obj.getDouble("longitude"),
                obj.getDouble("latitude"),
                obj.getInt("battery_status"),
                obj.getString("last_seen"),
                obj.getString("status")
        );
    }

    /**
     * Validates if the given JSON object contains the required data for a DynamicDrone.
     *
     * @param obj The JSON object to validate.
     * @return {@code true} if the JSON object contains the required fields, otherwise {@code false}.
     */
    @Override
    public boolean isValid(JSONObject obj) { return obj.has("drone") && obj.has("timestamp"); }

    /**
     * Returns the endpoint for fetching dynamic drone data.
     *
     * @return A {@link String} representing the API endpoint for dynamic drones.
     */
    @Override
    public String getEndpoint() { return "dronedynamics"; }
}
