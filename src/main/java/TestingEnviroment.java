import com.formdev.flatlaf.FlatDarculaLaf;
import gui.components.APIErrorPanel;

import javax.swing.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class TestingEnviroment {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel( new FlatDarculaLaf() );
        } catch(Exception ex) {
            System.err.println("Failed to initialize FlatLightLeaf Look and Feel");
        }

        JFrame frame = new JFrame();
        frame.setTitle("Drone Simulation Interface");
        frame.setSize(900, 1000);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        APIErrorPanel panel = new APIErrorPanel(e -> { Rerun(); }, "An Error occurred during the API call. Retry or exit program.");

        frame.add(panel);
        frame.setVisible(true);
    }

    public static void Rerun() {
        System.out.println("Rerun");
    }
}
