package src.main;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executors;

public class DroneSimulationInterfaceAPI {
    private final HttpClient httpClient;
    private final String baseUrl = "http://dronesim.facets-labs.com/api/";
    private final String droneTypesEndpoint = "dronetypes";
    private final String droneDynamicsEndpoint = "dronedynamics";
    private final String droneEndpoint = "drones";

    public DroneSimulationInterfaceAPI() {
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(60))
                .executor(Executors.newFixedThreadPool(5))
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    public void fetchDataFromEndpoint(String endpointUrl) {

    }


    // Gets info from fetchDataFromEndpoint and then Parse accordingly
    public void fetchDrones() {

    }

    public void fetchDroneTypes() {

    }

    public void fetchDroneDynamics() {

    }

}
