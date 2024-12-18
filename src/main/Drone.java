package src.main;

public class Drone {
    private String name;
    private String carriageType;
    private double weight;

    public Drone(String name, String carriageType, double weigh) {
        this.name = name;
        this.carriageType = carriageType;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public String getCarriageType() {
        return carriageType;
    }

    public double getWeight() {
        return weight;
    }
}