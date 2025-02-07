package core.drone;

import services.Helper;

/**
 * The DynamicDrone class is responsible for a dynamic drone object.
 * It contains the drone's link, timestamp, speed, alignment roll, alignment pitch, alignment yaw, longitude, latitude, battery status, last seen, and status.
 * @see DroneBase
 */
public class DynamicDrone extends DroneBase {
    private final String drone;
    private final String timestamp;
    private final int speed;
    private final double align_roll;
    private final double align_pitch;
    private final double align_yaw;
    private final double longitude;
    private final double latitude;
    private final int battery_status;
    private final String last_seen;
    private final String status;

    /**
     * Creates a DynamicDrone instance and extracts the ID from the provided drone link {@code drone}.
     *
     * @param drone          The drone link containing the ID.
     * @param timestamp      The timestamp when the drone data was recorded.
     * @param speed          The speed of the drone.
     * @param align_roll     The roll alignment of the drone.
     * @param align_pitch    The pitch alignment of the drone.
     * @param align_yaw      The yaw alignment of the drone.
     * @param longitude      The longitude coordinate of the drone.
     * @param latitude       The latitude coordinate of the drone.
     * @param battery_status The battery status of the drone.
     * @param last_seen      The last recorded time the drone was seen.
     * @param status         The operational status of the drone.
     */
    public DynamicDrone(String drone, String timestamp, int speed, double align_roll, double align_pitch, double align_yaw, double longitude, double latitude, int battery_status, String last_seen, String status) {
        this.drone = drone;
        this.timestamp = timestamp;
        this.speed = speed;
        this.align_roll = align_roll;
        this.align_pitch = align_pitch;
        this.align_yaw = align_yaw;
        this.longitude = longitude;
        this.latitude = latitude;
        this.battery_status = battery_status;
        this.last_seen = last_seen;
        this.status = status;
        this.id = Helper.extractDroneIDFromUrl(drone);
    }

    public String getDrone() { return drone; }
    public String getTimestamp() { return timestamp; }
    public int getSpeed() { return speed; }
    public double getAlignRoll() { return align_roll; }
    public double getAlignPitch() { return align_pitch; }
    public double getAlignYaw() { return align_yaw; }
    public double getLongitude() { return longitude; }
    public double getLatitude() { return latitude; }
    public int getBatteryStatus() { return battery_status; }
    public String getLastSeen() { return last_seen; }
    public String getStatus() { return status; }
}
