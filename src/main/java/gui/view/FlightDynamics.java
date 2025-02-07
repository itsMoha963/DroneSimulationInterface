package gui.view;

import core.drone.Drone;
import core.drone.DroneType;
import core.drone.DynamicDrone;
import core.parser.DroneParser;
import core.parser.DroneTypeParser;
import gui.interfaces.TabbedPaneActivationListener;
import gui.components.FlightDynamicsPanel;
import services.DroneSimulationInterfaceAPI;
import utils.AutoRefresh;
import exception.DroneAPIException;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@code FlightDynamics} class is a {@link JPanel} responsible for displaying the flight dynamics of drones in the GUI.
 * It handles displaying the dynamic data of drones, pagination, and auto-refreshing the data asynchronously.
 * <p>
 * It implements {@link TabbedPaneActivationListener} to handle activation and deactivation events for refreshing data.
 * The class is designed to support pagination and automatically load new drone data for each page, displaying them in a scrollable list.
 * It also includes error handling and automatic data preloading to improve performance.
 */
public class FlightDynamics extends JPanel implements TabbedPaneActivationListener {
    private static final Logger log = Logger.getLogger(FlightDynamics.class.getName());

    public static final int MAX_DRONES_PER_PAGE = 20;

    // Drone Cache
    private Map<Integer, DroneType> droneTypesCache = Map.of();
    private Map<Integer, Drone> droneCache = Map.of();

    private final AutoRefresh autoRefresh = new AutoRefresh();

    // GUI Elements
    private JPanel contentPanel;
    private JLabel currentPageLabel;
    private int currentPage = 0;

    /**
     * Creates a new {@code FlightDynamics} panel.
     * Initializes the GUI layout and preloads drone data asynchronously.
     */
    public FlightDynamics() {
        initializeGUI();
        preWarmAsync();
    }

    /**
     * Initializes the graphical user interface (GUI) components.
     * Sets up layout, title panel, content panel, and pagination controls.
     */
    private void initializeGUI() {
        setLayout(new BorderLayout());
        JPanel titlePanel = createTitlePanel();
        contentPanel = createContentPanel();
        JScrollPane scrollPane = createJScrollPanel(titlePanel);

        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(createPaginationPanel(), BorderLayout.SOUTH);
    }

    private JScrollPane createJScrollPanel(JPanel titlePanel) {
        JScrollPane scrollPane = new JScrollPane(contentPanel) {
            @Override
            public void setBounds(int x, int y, int width, int height) {
                super.setBounds(x, y, width, height);
                titlePanel.setVisible(getVerticalScrollBar().getValue() == 0);
            }
        };
        scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> titlePanel.setVisible(scrollPane.getVerticalScrollBar().getValue() == 0));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        return scrollPane;
    }

    private JPanel createContentPanel() {
        final JPanel contentPanel;
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UIManager.getColor("Panel.background"));

        contentPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), BorderFactory.createEtchedBorder(UIManager.getColor("Panel.background").brighter(), UIManager.getColor("Panel.background").darker())));
        return contentPanel;
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Flight Dynamics", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(UIManager.getColor("Panel.background"));
        titleLabel.setForeground(UIManager.getColor("Label.foreground"));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        return titlePanel;
    }

    private JPanel createPaginationPanel() {
        JPanel paginationPanel = new JPanel();
        paginationPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        paginationPanel.setBackground(UIManager.getColor("Panel.background"));
        JButton prevPageButton = new JButton("<");
        JButton nextPageButton = new JButton(">");
        currentPageLabel = new JLabel("Page: " + (currentPage + 1));
        currentPageLabel.setForeground(UIManager.getColor("Label.foreground"));

        // StartAutoRefresh resets the refresh timer as it wouldn't make sense to refresh after we just changed the page
        prevPageButton.addActionListener(e -> {
            StartAutoRefresh();
            loadPageAsync(currentPage - 1);
        });
        nextPageButton.addActionListener(e -> {
            StartAutoRefresh();
            loadPageAsync(currentPage + 1);
        });

        paginationPanel.add(prevPageButton);
        paginationPanel.add(currentPageLabel);
        paginationPanel.add(nextPageButton);
        paginationPanel.add(createPagingTextField());
        return paginationPanel;
    }

    // First thread
    private void loadPageAsync(int page) {
        // Runs the loadPage function in a background thread
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                loadPage(page);
                return null;
            }

            @Override
            protected void done() {
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        };
        worker.execute(); // Start the worker thread
    }

    private JTextField createPagingTextField() {
        JTextField pageInputField = new JTextField(5);
        pageInputField.addActionListener(e -> {
            try {
                int targetPage = Integer.parseInt(pageInputField.getText().trim());
                if (targetPage > 0) {
                    loadPageAsync(targetPage);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid page number. Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        return pageInputField;
    }

    private void preWarmAsync() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    droneTypesCache = DroneSimulationInterfaceAPI.getInstance().fetchDrones(new DroneTypeParser(), 40, 0);
                    droneCache = DroneSimulationInterfaceAPI.getInstance().fetchDrones(new DroneParser(), 40, 0);
                } catch (DroneAPIException e) {
                    log.log(Level.SEVERE, "Failed to cache DroneTypes/Drones. Exception: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void done() {
                loadPageAsync(0);
            }
        };
        worker.execute();
    }

    private void loadPage(int page) {
        if (page < 0) {
            return;
        }

        contentPanel.removeAll();

        if (droneCache.isEmpty() || droneTypesCache.isEmpty()) {
            log.log(Level.INFO, "Cache is empty. Attempting to cache drones.");
            preWarmAsync();
        }

        try {
            // Some DynamicDrones have drones that do not exist. One method to fix this would be to fetch a
            // sample of dynamic drones and loop through them till we find one that has the correct drone. But this would be very slow.
            ArrayList<DynamicDrone> drones = DroneSimulationInterfaceAPI.getInstance().fetchDynamicDrones(MAX_DRONES_PER_PAGE, page * MAX_DRONES_PER_PAGE);

            if (drones.isEmpty() && page > 0) {
                loadPage(currentPage);
                return;
            }

            int dronesCreated = 0;          // To keep count of drones with missing data
            for (DynamicDrone drone : drones) {
                JPanel dronePanel = createDronePanel(drone);
                if (dronePanel != null) {
                    dronesCreated++;
                    contentPanel.add(dronePanel);
                }
            }

            log.log(Level.INFO, "Created " + dronesCreated + " drones, for panel with MAX_DRONES_PER_PAGE: " + MAX_DRONES_PER_PAGE);

            currentPage = page;
            currentPageLabel.setText("Page: " + (currentPage + 1));

            contentPanel.revalidate();
            contentPanel.repaint();
        } catch (DroneAPIException e) {
            log.log(Level.SEVERE, "Failed to fetch DynamicDrone during page load: " + page + " Error: " + e.getMessage());
        }
    }

    private JPanel createDronePanel(DynamicDrone drone) {
        Drone baseDrone;
        DroneType droneType;

        // Sometimes the DynamicDrone has a Drone that doesn't exist which means we cant access the DroneType
        try {
            baseDrone = droneCache.get(drone.getId());
            droneType = droneTypesCache.get(baseDrone.getDroneTypeID());
        } catch (Exception e) {
            log.log(Level.WARNING, "Failed to fetch DroneType/Drone for the DynamicDrone: " + drone.getId());
            return null;
        }

        return new FlightDynamicsPanel(drone, baseDrone, droneType);
    }

    /**
     * Triggered when this panel is activated. Used to start auto-refresh when tab is activated.
     */
    @Override
    public void onActivate() { StartAutoRefresh(); }

    /**
     * Triggered when this panel is activated. Used to stop auto-refresh when tab is deactivated.
     */
    @Override
    public void onDeactivate() { autoRefresh.stop(); }

    /**
     * To reduce code duplication as this would be called multiple times in this class.
      */
    private void StartAutoRefresh() {
        autoRefresh.start(() -> loadPage(currentPage), 60, 45, TimeUnit.SECONDS);
    }
}