package gui.view;

import core.drone.Drone;
import core.drone.DroneType;
import core.drone.DynamicDrone;
import core.parser.DroneParser;
import core.parser.DroneTypeParser;
import gui.components.APIErrorPanel;
import gui.components.BatteryPanel;
import services.DroneSimulationInterfaceAPI;
import services.Helper;
import exception.DroneAPIException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DroneDashboard extends JPanel {
    private final Logger log = Logger.getLogger(DroneDashboard.class.getName());
    private final JPanel dronesPanel;
    private final JPanel droneInfoLabel;
    private Map<Integer, DroneType> droneTypesCache = Map.of();
    private Map<Integer, Drone> droneCache = Map.of();

    public DroneDashboard() {
        setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        dronesPanel = new JPanel();
        dronesPanel.setLayout(new BoxLayout(dronesPanel, BoxLayout.Y_AXIS));
        dronesPanel.setBackground(UIManager.getColor("Panel.background"));

        JScrollPane leftScrollPane = createScrollPane(dronesPanel);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.weightx = 0.3; // Fills 30% of the screen horizontally
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        add(leftScrollPane, gridBagConstraints);

        droneInfoLabel = createInfoPanel();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.weightx = 0.7; // Fills 70% of the screen
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        add(droneInfoLabel, gridBagConstraints);

        showPlaceholder();

        loadDrones();
        preWarm();
    }

    private JScrollPane createScrollPane(JPanel panel) {
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(BorderFactory.createEtchedBorder(UIManager.getColor("Panel.background").brighter(),
                UIManager.getColor("Panel.background").darker()));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return scrollPane;
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createEtchedBorder(UIManager.getColor("Panel.background").brighter(),
                UIManager.getColor("Panel.background").darker()));
        infoPanel.setBackground(UIManager.getColor("Panel.background"));
        return infoPanel;
    }

    private void loadDrones() {
        try {.
            Map<Integer, Drone> drones = fetchDrones();
            populateDroneButtons(drones);
            log.log(Level.INFO, "Successfully fetched " + drones.size() + " Drones.");
        } catch (DroneAPIException e) {
            log.log(Level.SEVERE, "Failed to load Drones. Creating Error handling panel.", e);
            throw new RuntimeException("Drone loading failed", e);
        }
    }
    // Only 40 Drones exist, so we only fetch 40 drones
    private Map<Integer, Drone> fetchDrones() throws DroneAPIException {
        return DroneSimulationInterfaceAPI.getInstance().fetchDrones(new DroneParser(), 40, 0);
    }

    private void populateDroneButtons(Map<Integer, Drone> drones) {
        for (Drone drone : drones.values()) {
            dronesPanel.add(createDroneButton(drone.getId()));
        }
        dronesPanel.revalidate();
        dronesPanel.repaint();
    }

    private void preWarm() {
        try {
            droneTypesCache = DroneSimulationInterfaceAPI.getInstance().fetchDrones(new DroneTypeParser(), 40, 0);
            droneCache = DroneSimulationInterfaceAPI.getInstance().fetchDrones(new DroneParser(), 40, 0);
        } catch (DroneAPIException e) {
            log.log(Level.SEVERE, "Failed to load Drones for Cache.", e);
            throw new RuntimeException("Cache pre-warming failed", e);
        }
    }

    private void showErrorPanel(DroneAPIException exception) {
        APIErrorPanel errorPanel = new APIErrorPanel(action -> {
            showPlaceholder();
            preWarm();
            loadDrones();
        }, exception.getMessage());

        droneInfoLabel.removeAll();
        droneInfoLabel.add(errorPanel);
        droneInfoLabel.revalidate();
        droneInfoLabel.repaint();
    }

    /**
     * Called when User clicks on a Button, to load Drone Information
     * Calculates Custom Information from existing Data.
     * - How much Battery time is left
     * - Drone Carriage ballast
     * @param id
     */
    private void loadDronePage(int id) {
        try {
            droneInfoLabel.removeAll();
            droneInfoLabel.setLayout(new BorderLayout(10, 10));
            droneInfoLabel.setBackground(UIManager.getColor("Panel.background"));
            droneInfoLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            List<DynamicDrone> dynamicDrones = DroneSimulationInterfaceAPI.getInstance().fetchDrones(id, 40, 0);
            log.log(Level.INFO, "Successfully loaded " + dynamicDrones.size() + " Drones.");

            if (!dynamicDrones.isEmpty()) {
                // Additional logic to handle dynamic drone details
            } else {
                log.log(Level.WARNING, "No dynamic drones available for ID: " + id);
            }
        } catch (DroneAPIException e) {
            log.log(Level.SEVERE, "Failed to load Drone Sample.", e);
            showErrorPanel(e);
        }
    }

    // Placeholder before a drone is selected
    private void showPlaceholder() {
        droneInfoLabel.removeAll();

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

        droneInfoLabel.setLayout(new BorderLayout());
        droneInfoLabel.add(placeholderPanel, BorderLayout.CENTER);

        droneInfoLabel.revalidate();
        droneInfoLabel.repaint();
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
}
