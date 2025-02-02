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
 * The DroneSimulationInterfaceAPI class provides an interface for interacting with a drone API.
 * This class is implemented as a Singleton to ensure a single instance is used across the application, as it is stateless.
 * It supports fetching all different drone types (Drone, DynamicDrone, DroneType) data from the API using parsers and configurable parameters.
 */
public final class DroneSimulationInterfaceAPI {
    private static final Logger log = Logger.getLogger(DroneSimulationInterfaceAPI.class.getName());
    private static final String BASE_URL = "http://dronesim.facets-labs.com/api/";
    private static final int TIMEOUT_SECONDS = 300;
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 3000;

    private String token = "b2d431185fd5a8670e99e3efdcb2afe193083931";
    private final HttpClient httpClient;

    private static DroneSimulationInterfaceAPI instance;

    /**
     * DroneSimulationInterfaceAPI is a Singleton as it is stateless.
     * No need to create multiple Instances for each use.
     *
     * @return Instance of the DroneSimulationInterfaceAPI
     */
    public static synchronized DroneSimulationInterfaceAPI getInstance() {
        if (instance == null) {
            instance = new DroneSimulationInterfaceAPI();
            log.log(Level.INFO, "Created a new instance for the DroneSimulationInterfaceAPI");
        }
        return instance;
    }

    private DroneSimulationInterfaceAPI() {
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .build();
    }

    /**
     * Fetches data from the given endpoint with specified limit and offset.
     * Retries up to MAX_RETRIES times if the request fails.
     *
     * @param endpointUrl The endpoint to fetch data from.
     * @param limit       The maximum number of results to fetch.
     * @param offset      The starting position for pagination.
     * @return JSONObject containing the response data.
     * @throws DroneAPIException If an error occurs during the API call.
     */
    private JSONObject fetchDataFromEndpoint(String endpointUrl, int limit, int offset) throws DroneAPIException {
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(createURI(endpointUrl, limit, offset))
                        .header("Authorization", "Token " + token)
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                int statusCode = response.statusCode();

                if (statusCode == 200) {
                    log.log(Level.INFO, "Got response from endpoint, after " + attempt + " trys");
                    return new JSONObject(response.body());
                } else if (statusCode == 404) {
                    log.log(Level.INFO, "Endpoint not found");
                    throw new DroneAPIException("Endpoint " + endpointUrl + " not found");
                } else if (statusCode == 401) {
                    log.log(Level.SEVERE, "Endpoint " + endpointUrl + " not authorized");
                    throw new DroneAPIException("Authentication with the API failed. Check if Token is correct.");
                } else {
                    log.log(Level.WARNING, "API Request to endpoint " + endpointUrl + " failed with status " + statusCode + " retrying....");
                }
            } catch (InterruptedException | IOException e) {
                log.log(Level.SEVERE, "Error on attempt " + attempt + " while fetching data from endpoint: " + endpointUrl, e);
            }

            try {
                log.log(Level.INFO, "Sleeping before next API try, for " + RETRY_DELAY_MS + "ms");
                Thread.sleep(RETRY_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Retry thread got interrupted");
            }
        }
        throw new DroneAPIException("Error while fetching data from endpoint: " + endpointUrl);
    }

    /**
     * Fetches drones from the API using the given JsonDroneParser.
     * The parser determines the type of drone and validates the API response.
     *
     * @param parser The JsonDroneParser to parse the API response into drone classes
     * @param limit  The maximum number of results to fetch from the API
     * @param offset The starting position of results for pagination
     * @param <T>    A type that extends DroneBase
     * @return A map of drone IDs to their respective parsed drone class
     * @throws DroneAPIException If an error occurs during the API call
     */
    public <T extends DroneBase> Map<Integer, T> fetchDrones(JsonDroneParser<T> parser, int limit, int offset) throws DroneAPIException {
        try {
            JSONObject jsonObject = fetchDataFromEndpoint(parser.getEndpoint(), limit, offset);
            JSONArray jsonResults = jsonObject.getJSONArray("results");

            Map<Integer, T> data = new HashMap<>();
            for (int i = 0; i < jsonResults.length(); i++) {
                JSONObject jsonDrone = jsonResults.getJSONObject(i);
                if (parser.isValid(jsonDrone)) {
                    T drone = parser.parse(jsonDrone);
                    data.put(drone.getId(), drone);
                }
            }
            return data;
        } catch (DroneAPIException e) {
            log.log(Level.SEVERE, "Error while parsing drone data", e);
            throw new DroneAPIException("Error parsing drone data.", e);
        }
    }

    public ArrayList<DynamicDrone> fetchDynamicDronesById(int id, int limit, int offset) {
        return fetchDynamicDrones(id + "/dynamics", limit, offset);
    }

    /**
     * Fetches dynamic drones from the default endpoint.
     *
     * @param limit  the maximum number of results to fetch
     * @param offset the starting position for pagination
     * @return a list of DynamicDrone objects
     * @throws DroneAPIException if the API call or parsing fails
     */
    public ArrayList<DynamicDrone> fetchDynamicDrones(int limit, int offset) throws DroneAPIException {
        return fetchDynamicDrones(new DynamicDroneParser().getEndpoint(), limit, offset);
    }

    private ArrayList<DynamicDrone> fetchDynamicDrones(String endpoint, int limit, int offset) throws DroneAPIException {
        try {
            JSONObject jsonObject = fetchDataFromEndpoint(endpoint, limit, offset);
            JSONArray jsonResults = jsonObject.getJSONArray("results");

            ArrayList<DynamicDrone> dynamicDrones = new ArrayList<>();
            DynamicDroneParser parser = new DynamicDroneParser();

            for (int i = 0; i < jsonResults.length(); i++) {
                JSONObject jsonDrone = jsonResults.getJSONObject(i);
                if (parser.isValid(jsonDrone)) {
                    dynamicDrones.add(parser.parse(jsonDrone));
                }
            }

            return dynamicDrones;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error parsing dynamic drone data.", e);
            throw new DroneAPIException("Error parsing dynamic drone data.", e);
        }
    }

    /**
     * Creates a URI for the API request with the specified parameters.
     *
     * @param endpointUrl The endpoint URL.
     * @param limit       The maximum number of results to fetch.
     * @param offset      The starting position for pagination.
     * @return A URI object.
     */
    private URI createURI(String endpointUrl, int limit, int offset) {
        try {
            return new URI(BASE_URL + endpointUrl + "/?format=json&limit=" + limit + "&offset=" + offset);
        } catch (URISyntaxException e) {
            log.log(Level.SEVERE, "Error while constructing URI", e);
            throw new RuntimeException("Error while constructing URI.", e);
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
            token = properties.getProperty("API_TOKEN");
        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to load configuration files");
            throw new RuntimeException("Failed to load configuration files");
        }
    }
}
