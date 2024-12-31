package src.main.services;

import org.json.JSONArray;
import org.json.JSONObject;
import src.main.core.Drone;
import src.main.core.DroneBase;
import src.main.core.DroneType;
import src.main.core.parser.JsonDroneParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DroneSimulationInterfaceAPI {
    private final String baseUrl = "http://dronesim.facets-labs.com/api/";
    private final String TOKEN = "b2d431185fd5a8670e99e3efdcb2afe193083931";

    private JSONObject fetchDataFromEndpoint(String endpointUrl, int limit, int offset) throws IOException {
        HttpURLConnection connection = getConnection(endpointUrl, limit, offset);
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

    public <T> ArrayList<T> fetchDroneData(JsonDroneParser<T> parser) throws IOException {
        JSONObject jsonObject = fetchDataFromEndpoint(parser.getEndpoint(), 100, 0);
        JSONArray jsonFile = jsonObject.getJSONArray("results");

        ArrayList<T> data = new ArrayList<T>();

        for (int i = 0; i < jsonFile.length(); i++) {
            JSONObject o = jsonFile.getJSONObject(i);
            if (parser.isValid(o)) {
                data.add(parser.parse(o));
            }
        }

        return data;
    }

    public  <T extends DroneBase> Map<Integer, T> bulkFetch(JsonDroneParser<T> parser) throws IOException {
        JSONObject jsonObject = fetchDataFromEndpoint(parser.getEndpoint(), 100, 0);
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

    public <T> ArrayList<T> fetchDroneData(JsonDroneParser<T> parser, int limit, int offset) throws IOException {
        JSONObject jsonObject = fetchDataFromEndpoint(parser.getEndpoint(), limit, offset);
        JSONArray jsonFile = jsonObject.getJSONArray("results");

        ArrayList<T> data = new ArrayList<T>();

        for (int i = 0; i < jsonFile.length(); i++) {
            JSONObject o = jsonFile.getJSONObject(i);
            if (parser.isValid(o)) {
                data.add(parser.parse(o));
            }
        }

        return data;
    }

    private HttpURLConnection getConnection(String endpointUrl, int limit, int offset) throws IOException {
        try {
            URL url = new URI(baseUrl + endpointUrl + "/?format=json&limit=" + limit + "&offset=" + offset).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Token " + TOKEN);
            return connection;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
