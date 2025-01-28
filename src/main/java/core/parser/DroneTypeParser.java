package core.parser;

import org.json.JSONObject;
import core.drone.DroneType;

/**
 * Parser for JSON objects into DroneTypes
 * @param obj The Jason object to parse
 * @return A DroneType instance with data from the JSON object
 */
public class DroneTypeParser implements JsonDroneParser<DroneType> {
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
     * Validates if the given object contains the required data for DroneType
     * @param obj The JSON object
     * @return True if the JSON object contains the required data
     */
    @Override
    public boolean isValid(JSONObject obj) {
        return obj.has("id") && obj.has("manufacturer");
    }

    /**
     * Endpoint for fetching drone type data
     * @return Endpoint string
     */
    @Override
    public String getEndpoint() {
        return "dronetypes";
    }
}
