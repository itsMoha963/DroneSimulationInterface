import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.*;
import gui.MainWindow;
import utils.RootLogger;

public class Main {
    public static void main(String[] args) {
        // Default Theme
        try {
            UIManager.setLookAndFeel( new FlatDarculaLaf() );
        } catch(Exception ex) {
            System.err.println("Failed to initialize FlatLightLeaf Look and Feel");
        }

        try {
            RootLogger.initializeLogger(true); // Initialize RootLogger
        }
        catch (Exception ex) {
            System.err.println("Failed to initialize RootLogger");
        }

        EventQueue.invokeLater( () ->  {
            MainWindow mainWindow = new MainWindow(); // Create the main window
            mainWindow.setVisible(true);             // Make the main window visible
        }
        );
    }
}