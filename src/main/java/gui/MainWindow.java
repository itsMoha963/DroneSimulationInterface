package gui;

import gui.view.DroneCatalog;
import gui.view.DroneDashboard;
import gui.view.FlightDynamics;
import services.DroneSimulationInterfaceAPI;
import utils.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.formdev.flatlaf.*;

public class MainWindow extends JFrame {
    private static final Logger log = Logger.getLogger(MainWindow.class.getName()); // Logger for MainWindow
    private DroneSimulationInterfaceAPI droneAPI;                                   // Drone API for the MainWindow to show the data of the drones

    public MainWindow() {
        // Set the title of the main window
        setTitle("Drone Simulation Interface");
        setSize(900, 1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Set the layout of the main window
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        add(toolBar, BorderLayout.NORTH);

        // create settings-Menü
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        //add settings-Menü
        JMenu settingsMenu = new JMenu("Settings");
        menuBar.add(settingsMenu);

        // add Theme-undermenu
        JMenu themeMenu = new JMenu("Change Theme");
        settingsMenu.add(themeMenu);

        //Theme-Options
        JMenuItem lightThemeItem = new JMenuItem("Light Theme");
        lightThemeItem.addActionListener(e -> setLookAndFeel("Light"));
        themeMenu.add(lightThemeItem);

        JMenuItem darkThemeItem = new JMenuItem("Dark Theme");
        darkThemeItem.addActionListener(e -> setLookAndFeel("Dark"));
        themeMenu.add(darkThemeItem);

        // Add the icon to the main window
        try {
            BufferedImage appIcon = ImageIO.read(getClass().getResource(Constants.APP_ICON_PATH));

            if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                Taskbar.getTaskbar().setIconImage(appIcon);
            }
            else {
                setIconImage(appIcon);
            }
        } catch (IOException e) {
            log.log(Level.WARNING, "Failed to load App Icon" + e.getMessage());
        }

        createTaskBar();
    }

    // Create the task bar for the main window
    public void createTaskBar() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Drone Catalog", new DroneCatalog());
        tabbedPane.addTab("Flight Dynamics", new FlightDynamics());
        tabbedPane.addTab("Drone Dashboard", new DroneDashboard());
        add(tabbedPane);
    }

    // Set the theme color of the main window
    private void setLookAndFeel(String theme) {
        try{
            if(theme.equals("Dark")){
                UIManager.setLookAndFeel(new FlatDarculaLaf());
            } else if(theme.equals("Light")){
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
            SwingUtilities.updateComponentTreeUI(this);
        }catch (UnsupportedLookAndFeelException e){
            log.log(Level.WARNING, "Failed to set Look and Feel" + e.getMessage());
        }
    }
}