package core.parser;

import core.drone.Drone;

/**
 * Parser for JSON objects into Drones
 * @param obj The Jason object to parse
 * @return A DroneType instance with data from the JSON object
 */
public class DroneParser implements JsonDroneParser<Drone> {
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
     * Validates if the given object contains the required data for Drones
     * @param obj The JSON object
     * @return True if the JSON object contains the required data
     */
    @Override
    public boolean isValid(org.json.JSONObject obj) {
        return obj.has("carriage_type") && obj.has("carriage_weight");
    }

    /**
     * Endpoint for fetching drone data
     * @return Endpoint string
     */
    @Override
    public String getEndpoint() {
        return "drones";
    }
}

