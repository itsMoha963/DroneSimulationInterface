package src.main;

import javax.swing.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Main
{
    private static String TOKEN;
    private static final String ENDPOINT_URL = "http://dronesim.facets-labs.com/api/";
    private static final String USER_AGENT = "group21";

    public static void main(String[] args) {
        // To Load Token, without exposing it to GitHub.
        try (FileInputStream inputStream = new FileInputStream("config.properties") ) {
            Properties properties = new Properties();
            properties.load(inputStream);
            TOKEN = properties.getProperty("API_TOKEN");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration files");
        }

        EventQueue.invokeLater( () ->  {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        }
        );


        try {
            URL url = new URL(ENDPOINT_URL + "drones/?format=json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Token " + TOKEN);
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.connect();
            int responseCode = conn.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                response.append(inputLine);
            }

            JSONObject json = new JSONObject(response.toString());
            JSONArray jsonFile = json.getJSONArray("results");

            for (int i = 0; i < jsonFile.length(); i++) {
                JSONObject o = jsonFile.getJSONObject(i);
                if(o.has("carriage_type") && o.has("carriage_weight")){
                    String a = o.getString("carriage_type");
                    int b = o.getInt("carriage_weight");
                    int id = o.getInt("id");
                    String data = "Drone " + id + ": carriage type " + a + " (weight: " + b + "g)";
                    System.out.println(data);
                }
            }

            conn.disconnect();
        } catch (Exception e) {
            System.out.print("Failed to connect to API");
        }
    }
}