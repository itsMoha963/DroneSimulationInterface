package src.main.core;

public class DroneType extends DroneBase {
    private String manufacturer;
    private String typeName;
    private int weight;
    private int maxSpeed;
    private int batteryCapacity;
    private int controlRange;
    private int maxCarriages;

    public DroneType(int id, String manufacturer, String typeName, int weight, int maxSpeed, int batteryCapacity, int controlRange, int maxCarriages) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.typeName = typeName;
        this.weight = weight;
        this.maxSpeed = maxSpeed;
        this.batteryCapacity = batteryCapacity;
        this.controlRange = controlRange;
        this.maxCarriages = maxCarriages;
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

    public int getMaxCarriages() {
        return maxCarriages;
    }

}
