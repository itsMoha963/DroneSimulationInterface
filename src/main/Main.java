package src.main;

import javax.swing.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Properties;

import com.formdev.flatlaf.FlatLightLaf;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Main
{
    private static String TOKEN;
    private static final String ENDPOINT_URL = "http://dronesim.facets-labs.com/api/";
    private static final String USER_AGENT = "group21";

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel( new FlatLightLaf() );
        } catch( Exception ex ) {
            System.err.println("Failed to initialize FlatLightLeaf Look and Feel");
        }

        // To Load Token, without exposing it to GitHub.
        try (FileInputStream inputStream = new FileInputStream("config.properties") ) {
            Properties properties = new Properties();
            properties.load(inputStream);
            TOKEN = properties.getProperty("API_TOKEN");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration files");
        }

        DroneSimulationInterfaceAPI api = new DroneSimulationInterfaceAPI();
        try {
            api.fetchDrones();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        EventQueue.invokeLater( () ->  {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        }
        );
    }

    // There are 3 JSON Formats to Parse
    /*
        dronetypes:
            id
            manufacturer
            typename
            weight
            max_speed
            battery_capacity
            control_range
            max_carriages

        dronedynamics:
            drone: http://dronesim.facets-labs.com/api/drones/96(Basically a droneid)/?format=json
            timestamp: 2024-12-15T17:00:52.588123+01:00
            speed
            allign_roll
            allign_pitch
            allign_yaw
            longitude
            latitude
            battery_status
            last_seen
            status

        drones:
            id	92
            dronetype	"http://dronesim.facets-labs.com/api/dronetypes/62/?format=json"
            created	"2024-12-17T17:00:52.649689+01:00"
            serialnumber	"HuX4-2027-7D12D4"
            carriage_weight	28
            carriage_type	"ACT"
     */

}