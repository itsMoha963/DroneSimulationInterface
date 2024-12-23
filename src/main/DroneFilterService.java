package src.main;

import java.util.List;
import java.util.stream.Collectors;

public class DroneFilterService {

    public List<Drone> filterDrones(List<Drone> drones, DroneFilter filter) {
        return drones.stream()
                .filter(drone -> meetsCondition(drone, filter))
                .collect(Collectors.toList());
    }

    private boolean meetsCondition(Drone drone, DroneFilter filter) {
        boolean conditionMet = true;

        conditionMet &= filter.getCarriageType().equals(drone.getCarriageType()) || filter.getCarriageType().equals("ALL_TYPES");
        conditionMet &= filter.getMinWeight() <= drone.getCarriageWeight() && filter.getMaxWeight() > drone.getCarriageWeight();

        return conditionMet;
    }
}
