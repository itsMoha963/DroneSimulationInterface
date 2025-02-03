import java.awt.*;
import gui.MainWindow;
import utils.RootLogger;

public class Main {
    public static void main(String[] args) {
        processArgs(args);

        // Creating a separate thread for GUI
        Thread guiThread = new Thread(() -> {
            EventQueue.invokeLater(() -> {
                MainWindow mainWindow = new MainWindow();
                mainWindow.setVisible(true);
            });
        });

        guiThread.start();
    }

    /**
     * Simpy processes all program arguments.
     * Currently only one exists.
     * @param args Program args
     */
    private static void processArgs(String[] args) {
        boolean debug = false;

        for (String arg : args) {
            if (arg.equals("--debug")) {
                debug = true;
                break;
            }
        }

        RootLogger.initializeLogger(debug);
    }
}