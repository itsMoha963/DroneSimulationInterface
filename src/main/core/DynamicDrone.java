package src.main.core;

public class DynamicDrone extends DroneBase {
    private String drone;
    private String timestamp;
    private int speed;
    private double align_roll;
    private double align_pitch;
    private double align_yaw;
    private double longitude;
    private double latitude;
    private int battery_status;
    private String last_seen;
    private String status;

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
    }

    public String getDrone() {
        return drone;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getSpeed() {
        return speed;
    }

    public double getAlignRoll() {
        return align_roll;
    }

    public double getAlignPitch() {
        return align_pitch;
    }

    public double getAlignYaw() {
        return align_yaw;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public int getBatteryStatus() {
        return battery_status;
    }

    public String getLastSeen() {
        return last_seen;
    }

    public String getStatus() {
        return status;
    }
}
