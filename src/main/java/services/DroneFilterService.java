package services;

import core.drone.DroneBase;
import core.filter.BaseDroneFilter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filters drones using condition
 * @param drones The collection of drones
 * @param filter which condition to apply
 * @param <T> Type of drones, extends DroneBase
 * @param return List of drones meeting the conditions
 */

public class DroneFilterService {

    public <T extends DroneBase> List<T> filterDrones(Collection<T> drones, BaseDroneFilter<T> filter) {
        return drones.stream()
                .filter(drone -> meetsCondition(drone, filter))
                .collect(Collectors.toList());
    }

    /**
     * Checks if a drone meets the conditions
     * @param drone The drone that is currently checked
     * @param filter The condition to check with
     * @param <T> Type of drone
     * @return set to true if condition are met
     */

    private <T extends DroneBase> boolean meetsCondition(T drone, BaseDroneFilter<T> filter) {
        return filter.getConditions().stream()
                .allMatch(condition -> condition.evaluate(drone));
    }
}