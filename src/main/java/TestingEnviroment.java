import core.drone.DynamicDrone;
import core.parser.DynamicDroneParser;
import services.DroneSimulationInterfaceAPI;

import java.util.ArrayList;
import java.util.Map;

public class TestingEnviroment {
    public static void main(String[] args) {
        DroneSimulationInterfaceAPI api = DroneSimulationInterfaceAPI.getInstance();
        ArrayList<DynamicDrone> x = api.fetchDrones(10000, 0);
        System.out.println("As List: " + x.size());

        Map<Integer, DynamicDrone> y = api.fetchDrones(new DynamicDroneParser(), 10000, 0);
        System.out.println("As HashMap: " + y.size());
    }
}
