package gui;

import gui.interfaces.TabbedPaneActivationListener;
import gui.view.DroneCatalog;
import gui.view.DroneDashboard;
import gui.view.FlightDynamics;
import utils.ConfigManager;
import utils.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.formdev.flatlaf.*;

/**
 * The {@code MainWindow} class represents the main window of the drone simulation application.
 * It is responsible for initializing the user interface, setting up the theme, and managing the main components such as the toolbar, menu, and tabbed pane.
 * <p>
 * The main window contains several views (tabs) like the Drone Catalog, Flight Dynamics, and Drone Dashboard.
 * It also allows for changing the app theme (Light/Dark).
 */
public class MainWindow extends JFrame {
    private static final Logger log = Logger.getLogger(MainWindow.class.getName());
    private Component lastComponent = null;
    private final ConfigManager configManager = new ConfigManager();

    /**
     * Initializes the main window of the application.
     * <p>
     * This constructor sets the window title, size, minimum size, default close operation, theme, and creates the toolbar, app icon, and tabbed pane.
     */
    public MainWindow() {
        // Set the title of the main window
        setTitle("Drone Simulation Interface");
        setSize(1200, 700);
        setMinimumSize(new Dimension(1200, 700));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Initialize default theme (Dark/Light)
        setTheme(configManager.getTheme());
        createToolBar();
        loadAppIcon();
        createTabbedPane();
    }

    /**
     * Creates and configures the toolbar for the main window.
     * It adds a menu bar with a settings menu that allows the user to change the application's theme.
     */
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

                    // Deactivates previous active tab
                    if (lastComponent instanceof TabbedPaneActivationListener listener) {
                        listener.onDeactivate();
                    }

                    // Activates the newly selected tab
                    if (newComponent instanceof TabbedPaneActivationListener listener) {
                        listener.onActivate();
                    }

                    // Update reference
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
            configManager.setTheme(theme);
        } catch (UnsupportedLookAndFeelException e){
            log.log(Level.WARNING, "Failed to set Look and Feel: " + e.getMessage());
        }
    }
}