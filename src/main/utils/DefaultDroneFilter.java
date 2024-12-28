package src.main.utils;

import src.main.core.Drone;

public class DefaultDroneFilter extends BaseDroneFilter<Drone> {
    private String carriageType;
    private int minWeight;
    private int maxWeight;

    public DefaultDroneFilter(String carriageType, int minWeight, int maxWeight) {
        this.carriageType = carriageType;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;

        addCondition(drone -> carriageType.equals("All Types") || drone.getCarriageType().equals(carriageType));
        addCondition(drone -> drone.getCarriageWeight() >= minWeight && drone.getCarriageWeight() < maxWeight);
    }

    public String getCarriageType() { return carriageType; }
    public int getMinWeight() { return minWeight; }
    public int getMaxWeight() { return maxWeight; }
}
