package src.main;

public class Drone extends DroneBase {
    private String carriageType;
    private int carriageWeight;
    private String droneType;
    private String created;

    public Drone(int id, String serialNumber, String carriageType, int carriageWeight, String droneType, String created) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.carriageType = carriageType;
        this.carriageWeight = carriageWeight;
        this.droneType = droneType;
        this.created = created;
    }

    public String getCarriageType() { return carriageType; }
    public int getCarriageWeight() { return carriageWeight; }
    public String getDroneType() { return droneType; }
    public String getCreated() { return created; }
}