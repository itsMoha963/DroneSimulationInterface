package gui;

import gui.view.DroneCatalog;
import gui.view.DroneDashboard;
import gui.view.FlightDynamics;
import services.DroneSimulationInterfaceAPI;
import utils.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainWindow extends JFrame {
    private DroneSimulationInterfaceAPI droneAPI;

    public MainWindow() {
        setTitle("Drone Simulation Interface");
        setSize(900, 1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        add(toolBar, BorderLayout.NORTH);

        try {
            setIconImage(ImageIO.read(getClass().getResource(Constants.APP_ICON_PATH)));
        } catch (IOException e) {
            System.err.println("Failed to load App Icon: " + e.getMessage());
        }

        createTaskBar();
    }

    public void createTaskBar() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Drone Catalog", new DroneCatalog());
        tabbedPane.addTab("Flight Dynamics", new FlightDynamics());
        tabbedPane.addTab("Drone Dashboard", new DroneDashboard());
        add(tabbedPane);
    }
}