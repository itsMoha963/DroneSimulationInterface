import java.awt.*;
import gui.MainWindow;
import utils.RootLogger;

public class Main {
    public static void main(String[] args) {
        processArgs(args);

        EventQueue.invokeLater( () ->  {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        }
        );
    }

    private static void processArgs(String[] args) {
        boolean debug = false;

        for (String arg : args) {
            if (arg.equals("--debug")) {
                debug = true;
            }
        }

        RootLogger.initializeLogger(debug);
    }
}