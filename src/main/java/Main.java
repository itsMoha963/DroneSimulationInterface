import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.*;
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
            switch (arg) {
                case "--debug":
                    debug = true;
                    break;
            }
        }

        RootLogger.initializeLogger(debug);
    }
}