package core.drone;

import services.Helper;
/**
 * The Drone class is responsible for a drone object.
 * It contains the drone's serial number, carriage type, carriage weight, drone type, created date, and drone type ID.
 * @see DroneBase
 */
public class Drone extends DroneBase {

    private final String carriageType;
    private final String serialNumber;
    private final int carriageWeight;
    private final String droneType;
    private final String created;
    private final int droneTypeID;

    /**
     * Constructor for the Drone class
     * @param id The drone's ID
     * @param serialNumber The drone's serial number
     * @param carriageType The drone's carriage type
     * @param carriageWeight The drone's carriage weight
     * @param droneType The drone's type
     * @param created The drone's created date
     */
    public Drone(int id, String serialNumber, String carriageType, int carriageWeight, String droneType, String created) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.carriageType = carriageType;
        this.carriageWeight = carriageWeight;
        this.droneType = droneType;
        this.created = created;
        this.droneTypeID = Helper.extractDroneIDFromUrl(droneType);
    }
    public String getCarriageType() { return carriageType; }
    public int getCarriageWeight() { return carriageWeight; }
    public String getDroneType() { return droneType; }
    public String getCreated() { return created; }
    public int getDroneTypeID() { return droneTypeID; }
    public String getSerialNumber() { return serialNumber; }
}