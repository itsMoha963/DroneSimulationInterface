package services;

import core.DroneBase;
import core.DynamicDrone;
import core.parser.DynamicDroneParser;
import core.parser.JsonDroneParser;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import java.util.logging.*;

public final class DroneSimulationInterfaceAPI {
    private static final Logger log = Logger.getLogger(DroneSimulationInterfaceAPI.class.getName());
    private final String BASEURL = "http://dronesim.facets-labs.com/api/";
    private String TOKEN = "b2d431185fd5a8670e99e3efdcb2afe193083931";

    private final HttpClient httpClient;
    private final int SECONDS_TILL_TIMEOUT = 300;

    private static DroneSimulationInterfaceAPI instance;

    /**
     * DroneSimulationInterfaceAPI is a Singleton as it is stateless.
     * No need to create multiple Instances for each View.
     * @return Instance of the DroneSimulationInterfaceAPI
     */
    public static DroneSimulationInterfaceAPI getInstance() {
        if (instance == null) {
            instance = new DroneSimulationInterfaceAPI();
        }
        return instance;
    }

    private  <T extends DroneBase> DroneSimulationInterfaceAPI() {
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(SECONDS_TILL_TIMEOUT))
                .build();
    }

    private JSONObject fetchDataFromEndpoint(String endpointUrl, int limit, int offset) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(createURI(endpointUrl, limit, offset))
                .header("Authorization", "Token " + TOKEN)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            log.log(Level.SEVERE, "Error fetching data from endpoint " + endpointUrl + "Limit and Offset: " + limit + " " + offset);
            throw new IOException("API Fetch failed with status code: " + response.statusCode());
        }

        log.log(Level.INFO, "Fetched data from endpoint " + endpointUrl + ": " + response);
        return new JSONObject(response.body());
    }

    public <T extends DroneBase> Map<Integer, T> fetchDrones(JsonDroneParser<T> parser, int limit, int offset) throws IOException, InterruptedException {
        JSONObject jsonObject = fetchDataFromEndpoint(parser.getEndpoint(), limit, offset);
        JSONArray jsonFile = jsonObject.getJSONArray("results");

        Map<Integer, T> data = new HashMap<Integer, T>();

        for (int i = 0; i < jsonFile.length(); i++) {
            JSONObject o = jsonFile.getJSONObject(i);
            if (parser.isValid(o)) {
                T x = parser.parse(o);
                data.put(x.getId(), x);
            }
        }
        return data;
    }

    public ArrayList<DynamicDrone> fetchDrones(int id, int limit, int offset) throws IOException, InterruptedException {
        JSONObject jsonObject = fetchDataFromEndpoint(id + "/dynamics", limit, offset);
        JSONArray jsonFile = jsonObject.getJSONArray("results");

        ArrayList<DynamicDrone> data = new ArrayList<>();
        DynamicDroneParser parser = new DynamicDroneParser();

        for (int i = 0; i < jsonFile.length(); i++) {
            JSONObject o = jsonFile.getJSONObject(i);
            if (parser.isValid(o)) {
                data.add(parser.parse(o));
            }
        }

        return data;
    }

    private URI createURI(String endpointUrl, int limit, int offset) {
        try {
            return new URI(BASEURL + endpointUrl + "/?format=json&limit=" + limit + "&offset=" + offset);
        }
        catch (URISyntaxException e) {
            throw new RuntimeException("Error while constructing URI." + e.getMessage());
        }
    }

    /**
    Not needed currently as TOKEN is useless without connection to the VPN, so no need to hide it.
    */
    private void loadToken() {
        try (FileInputStream inputStream = new FileInputStream("config.properties") ) {
            Properties properties = new Properties();
            properties.load(inputStream);
            TOKEN = properties.getProperty("API_TOKEN");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration files");
        }
    }
}