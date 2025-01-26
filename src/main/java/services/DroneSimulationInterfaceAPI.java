package services;

import core.drone.DroneBase;
import core.drone.DynamicDrone;
import core.parser.DynamicDroneParser;
import core.parser.JsonDroneParser;
import org.json.JSONArray;
import org.json.JSONObject;
import exception.DroneAPIException;


import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

import java.util.logging.*;

/**
 * The DroneSimulationInterfaceAPI class provides an interface for interacting with a dronesim API.
 * This class is implemented as a Singleton to ensure a single instance is used across the application, as it is stateless.
 * It supports fetching all different drone types (Drone, DynamicDrone, DroneType) data from the API using parsers and configurable parameters.
 */
public final class DroneSimulationInterfaceAPI {
    private static final Logger log = Logger.getLogger(DroneSimulationInterfaceAPI.class.getName());
    private final String BASEURL = "http://dronesim.facets-labs.com/api/";
    private String TOKEN = "b2d431185fd5a8670e99e3efdcb2afe193083931";

    private final HttpClient httpClient;
    private final int SECONDS_TILL_TIMEOUT = 300;
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY = 3000;

    private static DroneSimulationInterfaceAPI instance;

    /**
     * DroneSimulationInterfaceAPI is a Singleton as it is stateless.
     * No need to create multiple Instances for each use.
     * @return Instance of the DroneSimulationInterfaceAPI
     */
    public static synchronized DroneSimulationInterfaceAPI getInstance() {
        if (instance == null) {
            try {
                instance = new DroneSimulationInterfaceAPI();
                log.log(Level.INFO, "Initializing new instance for the DroneSimulationInterfaceAPI");
            } catch (DroneAPIException e) {
                log.log(Level.SEVERE, "Error while initializing new instance for the DroneSimulationInterfaceAPI");
                throw new DroneAPIException("Failed to create new DroneSimulationInterfaceAPI");
            }
        }
        return instance;
    }

    private  <T extends DroneBase> DroneSimulationInterfaceAPI() {
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(SECONDS_TILL_TIMEOUT))
                .build();
    }

    private JSONObject fetchDataFromEndpoint(String endpointUrl, int limit, int offset) throws DroneAPIException {
        // When the connection fails we retry MAX_RETRIES times.
        for (int i = 0; i < MAX_RETRIES; i++) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(createURI(endpointUrl, limit, offset))
                        .header("Authorization", "Token " + TOKEN)
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    log.log(Level.INFO, "Got response from endpoint, after " + (i + 1) + " trys");
                    return new JSONObject(response.body());
                } else if (response.statusCode() == 404) {
                    log.log(Level.INFO, "Endpoint not found");
                    throw new DroneAPIException("Endpoint " + endpointUrl + " not found");
                } else if (response.statusCode() == 401) {
                    log.log(Level.SEVERE, "Endpoint " + endpointUrl + " not authorized");
                    throw new DroneAPIException("Authentiocation with the API failed. Check if Token is correct.");
                } else {
                    log.log(Level.WARNING, "API Request to endpoint " + endpointUrl + " failed with status " + response.statusCode() + " retrying....");

                    // Wait for RETRY_DELAY before trying to fetch again.
                    if (i < MAX_RETRIES) {
                        Thread.sleep(RETRY_DELAY);
                    }
                }
            }
            catch (InterruptedException | IOException e) {
                log.log(Level.SEVERE, "Error while fetching data from endpoint " + endpointUrl + " on attempt: " + (i + 1));
                if (i == MAX_RETRIES) {
                    throw new DroneAPIException("Failed to connect to the API after " + (MAX_RETRIES) + " attempts");
                }
            }
        }
        throw new DroneAPIException("Error while Fetching Data from endpoint: " + endpointUrl + " with Limit and Offset: " + limit + " " + offset);
    }

    /**
     * Fetches drones from the API using the given JsonDroneParser {@code parser}.
     * The parser determines the type of drone and validates the API response.
     *
     * @param parser The JsonDroneParser to parse the API response into drone classes
     * @param limit The maximum number of results to fetch from the API
     * @param offset The starting position of results for pagination
     * @param <T> A type that extends DroneBase
     * @return A map of drone IDs to their respective parsed drone class
     * @throws DroneAPIException If an error occurs during the API call
     */
    public <T extends DroneBase> Map<Integer, T> fetchDrones(JsonDroneParser<T> parser, int limit, int offset) throws DroneAPIException {
        try {
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
        } catch (DroneAPIException e) {
            log.log(Level.SEVERE, "Error while parsing drone data");
            throw new DroneAPIException(e.getMessage());
        }
    }

    /**
     * Fetches the dynamic drone data for a specific drone {@code id} from the API.
     * This method retrieves data using the drone ID {@code id} and parses it into DynamicDrone class.
     *
     * @param id The ID of the drone to fetch dynamic data for
     * @param limit The maximum number of results to fetch from the API
     * @param offset The starting position of results for pagination
     * @return A list of DynamicDrone objects containing the parsed dynamic data
     * @throws DroneAPIException If an error occurs during the API call
     */
    public ArrayList<DynamicDrone> fetchDrones(int id, int limit, int offset) throws DroneAPIException {
        try {
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
        catch (DroneAPIException e) {
            log.log(Level.SEVERE, "Error while parsing drone data");
            throw new DroneAPIException("Error while parsing drone data");
        }
    }

    private URI createURI(String endpointUrl, int limit, int offset) {
        try {
            return new URI(BASEURL + endpointUrl + "/?format=json&limit=" + limit + "&offset=" + offset);
        }
        catch (URISyntaxException e) {
            log.log(Level.SEVERE, "Error while constructing URI");
            throw new RuntimeException("Error while constructing URI.");
        }
    }

    /**
     * Loads the API token from a configuration file.
     * This method reads the token from a "config.properties" file and sets it for authentication.
     * Note: This method is currently not needed as the token is already hardcoded.
     */
    private void loadToken() {
        try (FileInputStream inputStream = new FileInputStream("config.properties") ) {
            Properties properties = new Properties();
            properties.load(inputStream);
            TOKEN = properties.getProperty("API_TOKEN");
        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to load configuration files");
            throw new RuntimeException("Failed to load configuration files");
        }
    }
}