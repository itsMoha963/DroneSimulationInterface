package src.main;

public class Drone {
    private int id;
    private String carriageType;
    private int weight;

    public Drone(int id, String carriageType, int weight) {
        this.id = id;
        this.carriageType = carriageType;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public String getCarriageType() {
        return carriageType;
    }

    public int getWeight() {
        return weight;
    }
}