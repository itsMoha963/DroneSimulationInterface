package gui.view;

import core.drone.DroneType;
import core.parser.DroneTypeParser;
import gui.components.APIErrorPanel;
import gui.components.DroneCardPanel;
import gui.interfaces.TabbedPaneActivationListener;
import services.DroneSimulationInterfaceAPI;
import utils.AutoRefresh;
import exception.DroneAPIException;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * The DroneCatalog class is responsible for displaying the Drone Catalog in the GUI.
 * It fetches the drones from the API and displays them.
 *
 * @see DroneCardPanel
 * @see TabbedPaneActivationListener
 */
public class DroneCatalog extends JPanel implements TabbedPaneActivationListener {

    // GUI Components
    private JPanel contentPanel;
    private final AutoRefresh autoRefresh = new AutoRefresh();

    /**
     * Initializes the Drone Catalog GUI by setting up the layout and panels.
     */
    public DroneCatalog() {
        initialize();
    }

    /**
     * Sets up the layout and adds components to the panel.
     */
    private void initialize() {
        setLayout(new BorderLayout());

        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);

        JScrollPane scrollPane = createContentScrollPane();
        populateContentPanelAsync();
        add(scrollPane, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Drone Catalog", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(UIManager.getColor("Label.background"));
        titleLabel.setForeground(UIManager.getColor("Label.foreground"));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        return titlePanel;
    }

    /**
     * Populates the content panel asynchronously with drone data.
     * This method runs in the background and updates the UI once the data is fetched.
     *
     * @throws DroneAPIException if there is an error fetching drone data
     */
    private synchronized void populateContentPanelAsync() throws DroneAPIException {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    Map<Integer, DroneType> drones = fetchDrones();
                    SwingUtilities.invokeLater(() -> populateContentPanel(drones));
                } catch (DroneAPIException e) {
                    SwingUtilities.invokeLater(() -> showErrorPanel(e));
                }

                return null;
            }
        };
        worker.execute();
    }

    /**
     * Displays an error panel in case of a failure to fetch drone data.
     *
     * @param e the exception that occurred during the drone data fetch
     */
    private void showErrorPanel(DroneAPIException e) {
        APIErrorPanel errorPanel = new APIErrorPanel(action -> {
            removeAll();
            initialize();
        }, e.getMessage());
        removeAll();
        add(errorPanel);
        revalidate();
        repaint();
    }

    /**
     * Populates the content panel with drone cards.
     *
     * @param drones a map of drone types to be displayed in the catalog
     * @throws DroneAPIException if there is an error while adding drone cards to the content panel
     */
    private void populateContentPanel(Map<Integer, DroneType> drones) throws DroneAPIException{
        contentPanel.removeAll();

        // Create Drone cards for each drone
        for (DroneType drone : drones.values()) {
            contentPanel.add(new DroneCardPanel(drone));
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JScrollPane createContentScrollPane() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(0, 3, 15, 15)); // 3 columns with spacing
        contentPanel.setBackground(UIManager.getColor("Panel.background"));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private Map<Integer, DroneType> fetchDrones() throws DroneAPIException {
        return DroneSimulationInterfaceAPI.getInstance().fetchDrones(new DroneTypeParser(), 40, 0);
    }

    /**
     * Starts the auto-refresh functionality when the tab is activated.
     * Auto-refresh will fetch the latest drone data every 60 seconds, refreshing every 120 seconds.
     */
    @Override
    public void onActivate() {
        autoRefresh.start(this::populateContentPanelAsync, 120, 60, TimeUnit.SECONDS);
    }

    /**
     * Stops the auto-refresh when the tab is deactivated.
     */
    @Override
    public void onDeactivate() {
        autoRefresh.stop();
    }
}