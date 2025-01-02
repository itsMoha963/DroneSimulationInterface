package src.main.gui;

import src.main.services.LogService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class MainWindow extends JFrame {
    private LogService logService;
    private LogView logView;

    public MainWindow() {
        logService = new LogService();
        logView = new LogView(logService);

        setTitle("Drone Simulation Interface");
        setSize(900, 1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        try {
            setIconImage(ImageIO.read(new File("Icons/appicon.png")));
        } catch (IOException e) {
            System.err.println("Failed to load App Icon: " + e.getMessage());
        }

        createTaskBar();
    }

    public void createTaskBar() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Drone", new DroneWindow());
        tabbedPane.addTab("Dynamic Drone", new DynamicDroneWindow());
        tabbedPane.addTab("Drone Types", new DroneTypeView());
        tabbedPane.addTab("Logs", logView); // Use the initialized LogView
        add(tabbedPane);
    }
}