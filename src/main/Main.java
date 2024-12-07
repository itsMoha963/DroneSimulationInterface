package src.main;

import javax.swing.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import  java.net.URL;
import java.net.HttpURLConnection;

public class Main
{
    // DONT UPLOAD TOKEN TO GIT!!!!!
    private static final String TOKEN = "";
    private static final String ENDPOINT_URL = "http://dronesim.facets-labs.com/api/";
    private static final String USER_AGENT = "group21";

    public static void main(String[] args)
    {
        try {
            URL url = new URL(ENDPOINT_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Token " + TOKEN);
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.connect();

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Connection established.");
            } else {
                System.out.print("Error Details: ");
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String inputLine;
                while ( (inputLine = in.readLine()) != null ) {
                    System.out.println(inputLine);
                }
                in.close();

            }

            conn.disconnect();
        } catch (Exception e) {
            System.out.print("");
        }

    }
}