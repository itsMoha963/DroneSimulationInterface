package src.main.core.parser;

import org.json.JSONObject;
import src.main.core.DynamicDrone;

public class DynamicDroneParser implements JsonDroneParser<DynamicDrone> {
    @Override
    public DynamicDrone parse(JSONObject obj) {
        return new DynamicDrone(
                obj.getString("drone"),
                obj.getString("timestamp"),
                obj.getInt("speed"),
                obj.getInt("allign_roll"),
                obj.getInt("allign_pitch"),
                obj.getInt("allign_yaw"),
                obj.getDouble("longitude"),
                obj.getDouble("latitude"),
                obj.getInt("battery_status"),
                obj.getString("last_seen"),
                obj.getInt("status")
        );
    }

    @Override
    public boolean isValid(JSONObject obj) {
        return obj.has("drone") && obj.has("timestamp");
    }
}
