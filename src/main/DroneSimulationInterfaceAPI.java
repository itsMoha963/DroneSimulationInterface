package src.main;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class DroneSimulationInterfaceAPI {
    private final HttpClient httpClient;
    private final String baseUrl = "http://dronesim.facets-labs.com/api/";
    private final String droneTypesEndpoint = "dronetypes";
    private final String droneDynamicsEndpoint = "dronedynamics";
    private final String droneEndpoint = "drones";

    private final String TOKEN = "b2d431185fd5a8670e99e3efdcb2afe193083931";

    public DroneSimulationInterfaceAPI() {
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(60))
                .executor(Executors.newFixedThreadPool(5))
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    private JSONObject fetchDataFromEndpoint(String endpointUrl) throws IOException {
        HttpURLConnection connection = getConnection(endpointUrl);
        connection.connect();

        if (connection.getResponseCode() != 200) {
            throw new IOException("Connection Failed! Error Code: " + connection.getResponseCode());
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ( (inputLine = in.readLine()) != null ) {
            System.out.println(inputLine);
            response.append(inputLine);
        }

        return new JSONObject(response.toString());
    }


    // Gets info from fetchDataFromEndpoint and then Parse accordingly
    public ArrayList<Drone> fetchDrones() throws IOException {
        JSONObject jsonObject = fetchDataFromEndpoint(droneEndpoint);
        JSONArray jsonFile = jsonObject.getJSONArray("results");

        ArrayList<Drone> drones = new ArrayList<Drone>();

        for (int i = 0; i < jsonFile.length(); i++) {
            JSONObject o = jsonFile.getJSONObject(i);
            if (o.has("carriage_type") && o.has("carriage_weight")) {
                String type = o.getString("carriage_type");
                String droneTypeURL = o.getString("dronetype");
                String serialNumber = o.getString("serialnumber");
                String created = o.getString("created");
                int weight = o.getInt("carriage_weight");
                int id = o.getInt("id");
                drones.add(new Drone(id, serialNumber, type, weight, droneTypeURL, created));
            }
        }

        return drones;
    }

    public void fetchDroneTypes() {
        throw new RuntimeException("DroneSimulationInterfaceAPI.fetchDroneTypes is Not Implemented");
    }

    public ArrayList<DynamicDrone> fetchDroneDynamics() throws IOException {
        JSONObject jsonObject = fetchDataFromEndpoint(droneEndpoint);
        JSONArray jsonFile = jsonObject.getJSONArray("results");

        ArrayList<DynamicDrone> drones = new ArrayList<DynamicDrone>();

        for (int i = 0; i < jsonFile.length(); i++) {
            JSONObject o = jsonFile.getJSONObject(i);
            if (o.has("drone") && o.has("timestamp")) {
                String droneURL = o.getString("drone");
                String timestamp = o.getString("timestamp");
                int speed = o.getInt("speed");
                int allign_roll = o.getInt("allign_roll");
                int allign_pitch = o.getInt("allign_pitch");
                int allign_yaw = o.getInt("allign_yaw");
                double longitude = o.getDouble("longitude");
                double latitude = o.getDouble("latitude");
                int battery_status = o.getInt("battery_status");
                String last_seen = o.getString("last_seen");
                int status = o.getInt("status");

                drones.add(new DynamicDrone(droneURL, timestamp, speed, allign_roll, allign_pitch, allign_yaw, longitude, latitude, battery_status, last_seen, status));
            }
        }

        return drones;
    }

    private HttpURLConnection getConnection(String endpointUrl) throws IOException {
        URL url = new URL(baseUrl + endpointUrl + "/?format=json&limit=100");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Token " + TOKEN);
        return connection;
    }

}
