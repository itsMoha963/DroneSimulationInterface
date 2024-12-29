package src.main.gui;

import src.main.core.DynamicDrone;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class MainWindow extends JFrame {

    private final JPanel contentPanel = new JPanel();

    private static final String WINDOW_DRONE = "DRONE";
    private static final String WINDOW_DYNAMIC_DRONE = "DYNAMIC_DRONE";
    private static final String WINDOW_DRONE_TYPES = "DRONE_TYPES";

    private final HashMap<String, JPanel> Views = new HashMap<>();

    public MainWindow() {
        setTitle("Drone Simulation Interface");
        setSize(900, 1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        try {
            setIconImage(ImageIO.read(new File("Icons/appicon.png")));
        } catch (IOException e) {
            System.err.println("Failed to load App Icon: " + e.getMessage());
        }

        add(contentPanel);
        createViewSwitcher();

        Views.put(WINDOW_DRONE, new DroneWindow());
        Views.put(WINDOW_DYNAMIC_DRONE, new DynamicDroneWindow());
        Views.put(WINDOW_DRONE_TYPES, new DroneTypeView());

        switchView(WINDOW_DRONE);
    }

    public void createViewSwitcher() {
        JComboBox<String> viewSwitcher = new JComboBox<>();
        viewSwitcher.addItem(WINDOW_DRONE);
        viewSwitcher.addItem(WINDOW_DYNAMIC_DRONE);
        viewSwitcher.addItem(WINDOW_DRONE_TYPES);
        viewSwitcher.addActionListener(e -> {
            switchView(viewSwitcher.getSelectedItem().toString());
        });
        add(viewSwitcher, BorderLayout.NORTH);
    }

    /* PIECE OF SHIT JTABBEDPANE BREAKS ALL LAYOUTS AND SCROLLING FUCK YOU*/
    public void createTaskBar() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Drone", new DroneWindow());
        tabbedPane.addTab("Dynamic Drone", new DynamicDroneWindow());
        tabbedPane.addTab("Drone Types", new DroneTypeView());
        add(tabbedPane, BorderLayout.NORTH);
    }

    public void switchView(String viewName) {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(Views.get(viewName), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
