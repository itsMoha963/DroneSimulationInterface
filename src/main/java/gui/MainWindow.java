package gui;

import gui.interfaces.TabbedPaneActivationListener;
import gui.view.DroneCatalog;
import gui.view.DroneDashboard;
import gui.view.FlightDynamics;
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
    private static final Logger log = Logger.getLogger(MainWindow.class.getName());
    private Component lastComponent = null;

    public MainWindow() {
        // Set the title of the main window
        setTitle("Drone Simulation Interface");
        setSize(900, 1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setTheme("Dark");           // Initialize default theme (Dark/Light)
        createToolBar();
        loadAppIcon();
        createTabbedPane();
    }

    private void createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        add(toolBar, BorderLayout.NORTH);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu settingsMenu = new JMenu("Settings");
        menuBar.add(settingsMenu);

        JMenu themeMenu = new JMenu("Change Theme");
        settingsMenu.add(themeMenu);

        // Theme-Options
        JMenuItem lightThemeItem = new JMenuItem("Light Theme");
        lightThemeItem.addActionListener(e -> setTheme("Light"));
        themeMenu.add(lightThemeItem);

        JMenuItem darkThemeItem = new JMenuItem("Dark Theme");
        darkThemeItem.addActionListener(e -> setTheme("Dark"));
        themeMenu.add(darkThemeItem);
    }

    private void loadAppIcon() {
        try {
            BufferedImage appIcon = ImageIO.read(getClass().getResource(Constants.APP_ICON_PATH));

            // Setting the icon works differently for macOS
            if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                Taskbar.getTaskbar().setIconImage(appIcon);
            }
            else {
                setIconImage(appIcon);
            }
        } catch (IOException e) {
            log.log(Level.WARNING, "Failed to load App Icon" + e.getMessage());
        }
    }

    private void createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Drone Catalog", new DroneCatalog());
        tabbedPane.addTab("Flight Dynamics", new FlightDynamics());
        tabbedPane.addTab("Drone Dashboard", new DroneDashboard());

        tabbedPane.addChangeListener(
                onStateChange -> {
                    Component newComponent = tabbedPane.getSelectedComponent();

                    if (lastComponent instanceof TabbedPaneActivationListener listener) {
                        listener.onDeactivate();
                    }

                    if (newComponent instanceof TabbedPaneActivationListener listener) {
                        listener.onActivate();
                    }

                    lastComponent = newComponent;
                }
        );

        add(tabbedPane);
    }

    private void setTheme(String theme) {
        try {
            if (theme.equals("Dark")){
                UIManager.setLookAndFeel(new FlatDarculaLaf());
            } else if (theme.equals("Light")){
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
            SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException e){
            log.log(Level.WARNING, "Failed to set Look and Feel: " + e.getMessage());
        }
    }
}