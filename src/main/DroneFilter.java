package src.main;

public class DroneFilter {
    private final String carriageType;
    private final int minWeight;
    private final int maxWeight;

    DroneFilter(Builder builder) {
        this.carriageType = builder.carriageType;
        this.minWeight = builder.minWeight;
        this.maxWeight = builder.maxWeight;
    }

    public String getCarriageType() { return carriageType; }
    public int getMinWeight() { return minWeight; }
    public int getMaxWeight() { return maxWeight; }

    public static class Builder {
        private String carriageType;
        private int minWeight;
        private int maxWeight;

        public Builder carriageType(String carriageType) {
            this.carriageType = carriageType;
            return this;
        }

        public Builder weightRange(int minWeight, int maxWeight) {
            this.minWeight = minWeight;
            this.maxWeight = maxWeight;
            return this;
        }

        public DroneFilter build() {
            return new DroneFilter(this);
        }
    }
}