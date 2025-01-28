package core.parser;

import org.json.JSONObject;
import core.drone.DynamicDrone;

/**
 * Parser for JSON objects into DynamicDrone
 * @param obj The Jason object to parse
 * @return A DynamicDrone instance with data from the JSON object
 */

public class DynamicDroneParser implements JsonDroneParser<DynamicDrone> {
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
     * Validates if the given object contains the required data for DynamicDrone
     * @param obj The JSON object
     * @return True if the JSON object contains the required data
     */

    @Override
    public boolean isValid(JSONObject obj) {
        return obj.has("drone") && obj.has("timestamp");
    }

    /**
     * Endpoint for fetching dynmaic drone data
     * @return Endpoint string
     */
    @Override
    public String getEndpoint() {
        return "dronedynamics";
    }
}
