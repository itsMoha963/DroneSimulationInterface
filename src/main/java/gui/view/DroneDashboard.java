package gui.view;

import core.drone.Drone;
import core.drone.DroneType;
import core.drone.DynamicDrone;
import core.parser.DroneParser;
import core.parser.DroneTypeParser;
import gui.components.APIErrorPanel;
import gui.components.DroneInfoPanel;
import services.DroneSimulationInterfaceAPI;
import exception.DroneAPIException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DroneDashboard extends JPanel {
    private final Logger log = Logger.getLogger(DroneDashboard.class.getName());
    private static final int DRONE_SAMPLE_SIZE = 40;

    // UI Components
    private JPanel droneButtonsPanel;
    private JPanel droneInfoPanel;

    // Cache
    private Map<Integer, DroneType> droneTypesCache = Map.of();
    private Map<Integer, Drone> droneCache = Map.of();

    /**
     * Initializes the DroneDashboard
     */
    public DroneDashboard() {
        setLayout(new GridBagLayout());
        setupPanels();
        cacheDroneData();
        showPlaceholder();
        loadDrones();
    }

    private void setupPanels() {
        droneButtonsPanel = new JPanel();
        droneButtonsPanel.setLayout(new BoxLayout(droneButtonsPanel, BoxLayout.Y_AXIS));
        droneButtonsPanel.setBackground(UIManager.getColor("Panel.background"));

        addComponentToGrid(createScrollPane(), 0, 0.2);

        droneInfoPanel = new JPanel();
        droneInfoPanel.setLayout(new BorderLayout());
        droneInfoPanel.setBorder(BorderFactory.createEtchedBorder(UIManager.getColor("Panel.background").brighter(),
                UIManager.getColor("Panel.background").darker()));
        droneInfoPanel.setBackground(UIManager.getColor("Panel.background"));
        addComponentToGrid(droneInfoPanel, 1, 0.8);
    }

    /**
     * Creates the Scroll Pane for the drone buttons.
     * @return JScrollPane
     */
    private JScrollPane createScrollPane() {
        JScrollPane leftScrollPane = new JScrollPane(droneButtonsPanel);
        leftScrollPane.setBorder(BorderFactory.createEtchedBorder(UIManager.getColor("Panel.background").brighter(),
                UIManager.getColor("Panel.background").darker()));
        leftScrollPane.getVerticalScrollBar().setUnitIncrement(16);                     // 16 for Smoother scrolling
        leftScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        leftScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return leftScrollPane;
    }

    // To reduce code duplication
    private void addComponentToGrid(Component component, int gridx, double weightx) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = gridx;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.weightx = weightx;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        add(component, gridBagConstraints);
    }

    /**
     * Creates the buttons for all the drones
     */
    private void loadDrones() {
        try {
            if (droneCache.isEmpty()) {
                throw new DroneAPIException("Drone Cache is empty");
            }

            for (Drone drone : droneCache.values()) {
                droneButtonsPanel.add(createDroneButton(drone.getId()));
            }

            log.log(Level.INFO, "Created drone buttons.");

            droneButtonsPanel.revalidate();
            droneButtonsPanel.repaint();
        } catch (DroneAPIException e) {
            log.log(Level.SEVERE, "Failed to create drone buttons.");
            showErrorPanel(e);
        }
    }

    /**
     * Preloads data for drone types and caches it to improve performance.
     */
    private void cacheDroneData() {
        try {
            droneTypesCache = DroneSimulationInterfaceAPI.getInstance().fetchDrones(new DroneTypeParser(), 40, 0);
            droneCache = DroneSimulationInterfaceAPI.getInstance().fetchDrones(new DroneParser(), 40, 0);
        } catch (DroneAPIException e) {
            log.log(Level.SEVERE, "Failed to cache drones.");
            showErrorPanel(e);
        }
    }

    /**
     * Displays an error panel when an API exception occurs.
     *
     * @param exception The exception to display in the error panel.
     */
    private void showErrorPanel(DroneAPIException exception) {
        APIErrorPanel errorPanel = new APIErrorPanel(action -> {
            showPlaceholder();
            cacheDroneData();
            loadDrones();
        }, exception.getMessage());

        droneInfoPanel.removeAll();
        droneInfoPanel.add(errorPanel, BorderLayout.CENTER);

        droneInfoPanel.revalidate();
        droneInfoPanel.repaint();
    }

    /**
     * Called when User clicks on a Button, to load Drone Information
     * Calculates Custom Information from existing Data.
     * - How much Battery time is left
     * - Drone Carriage ballast
     * @param id The ID of the drone to display
     */
    private void loadDronePage(int id) {
        droneInfoPanel.removeAll();
        ArrayList<DynamicDrone> dynamicDrones = new ArrayList<>();
        try {
            dynamicDrones = DroneSimulationInterfaceAPI.getInstance().fetchDynamicDronesById(id, DRONE_SAMPLE_SIZE, 0);
        } catch (DroneAPIException e) {
            log.log(Level.SEVERE, "Failed to load Drone Sample.");
            showErrorPanel(e);
            throw e;
        }
        log.log(Level.INFO, "Successfully loaded " + dynamicDrones.size() + " Drones with a sample size of: " + DRONE_SAMPLE_SIZE);

        DynamicDrone dynamicDrone = dynamicDrones.getLast();
        Drone drone = droneCache.get(dynamicDrone.getId());
        DroneType droneType = droneTypesCache.get(drone.getDroneTypeID());

        droneInfoPanel.add(new DroneInfoPanel(dynamicDrones, drone, droneType), BorderLayout.CENTER);

        droneInfoPanel.revalidate();
        droneInfoPanel.repaint();
    }

    private JButton createDroneButton(int id) {
        JButton button = new JButton("Drone " + id);
        button.setHorizontalAlignment(SwingConstants.CENTER);

        button.setMinimumSize(new Dimension(120, 50));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setPreferredSize(new Dimension(100, 50));

        button.setFont(new Font("Arial", Font.BOLD, 16));

        button.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Font currentFont = button.getFont();
                int buttonWidth = button.getWidth();
                int newFontSize = Math.max(buttonWidth / 20, 16);
                button.setFont(new Font(currentFont.getName(), currentFont.getStyle(), newFontSize));
            }
        });

        button.addActionListener(e -> loadDronePage(id));
        return button;
    }

    // Placeholder before a drone is selected
    private void showPlaceholder() {
        droneInfoPanel.removeAll();

        JPanel placeholderPanel = new JPanel();
        placeholderPanel.setLayout(new BoxLayout(placeholderPanel, BoxLayout.Y_AXIS));
        placeholderPanel.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));

        JLabel placeholderLabel = new JLabel("No data to show.");
        placeholderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        placeholderLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        placeholderPanel.add(placeholderLabel);

        JLabel placeholderLabel2 = new JLabel("Click on a Drone to view details.");
        placeholderLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        placeholderLabel2.setFont(new Font("Arial", Font.ITALIC, 16));
        placeholderPanel.add(placeholderLabel2);

        droneInfoPanel.add(placeholderPanel, BorderLayout.CENTER);

        droneInfoPanel.revalidate();
        droneInfoPanel.repaint();
    }
}