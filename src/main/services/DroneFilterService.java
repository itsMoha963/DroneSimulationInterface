package src.main.services;

import src.main.core.Drone;
import src.main.utils.DroneFilter;

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

        conditionMet &= filter.getCarriageType().equals(drone.getCarriageType()) || filter.getCarriageType().equals("All Types");
        conditionMet &= filter.getMinWeight() <= drone.getCarriageWeight() && filter.getMaxWeight() > drone.getCarriageWeight();

        return conditionMet;
    }
}
