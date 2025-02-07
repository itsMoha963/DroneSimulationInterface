package core.drone;

/**
 * The DroneType class is responsible for a drone type object.
 * It contains the drone type's manufacturer, type name, weight, max speed, battery capacity, control range, and max carriage.
 * @see DroneBase
 */
public class DroneType extends DroneBase {

    private final String manufacturer;
    private final String typeName;
    private final int weight;
    private final int maxSpeed;
    private final int batteryCapacity;
    private final int controlRange;
    private final int maxCarriage;

    /**
     * Constructor for the DroneType class
     * @param id The drone type's ID
     * @param manufacturer The drone type's manufacturer
     * @param typeName The drone type's name
     * @param weight The drone type's weight
     * @param maxSpeed The drone type's max speed
     * @param batteryCapacity The drone type's battery capacity
     * @param controlRange The drone type's control range
     * @param maxCarriage The drone type's max carriage
     */
    public DroneType(int id, String manufacturer, String typeName, int weight, int maxSpeed, int batteryCapacity, int controlRange, int maxCarriage) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.typeName = typeName;
        this.weight = weight;
        this.maxSpeed = maxSpeed;
        this.batteryCapacity = batteryCapacity;
        this.controlRange = controlRange;
        this.maxCarriage = maxCarriage;
    }

    public String getManufacturer() {
        return manufacturer;
    }
    public String getTypeName() {
        return typeName;
    }
    public int getWeight() {
        return weight;
    }
    public int getMaxSpeed() {
        return maxSpeed;
    }
    public int getBatteryCapacity() {
        return batteryCapacity;
    }
    public int getControlRange() {
        return controlRange;
    }
    public int getMaxCarriage() {
        return maxCarriage;
    }
}