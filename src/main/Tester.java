package src.main;

import src.main.core.DynamicDrone;
import src.main.core.parser.DynamicDroneParser;
import src.main.services.DroneSimulationInterfaceAPI;

import java.io.IOException;
import java.util.Map;

public class Tester {
    public static void main(String[] args) throws IOException, InterruptedException {
        DroneSimulationInterfaceAPI api = new DroneSimulationInterfaceAPI();
        Map<Integer, DynamicDrone> drones = api.fetchDrones(new DynamicDroneParser(), 10, 3);
        System.out.println("Drones: " + drones.size());

        for (Map.Entry<Integer, DynamicDrone> drone : drones.entrySet()) {
            System.out.println("Drone " + drone.getKey() + ": " + drone.getValue().getDrone());
        }
    }
}
