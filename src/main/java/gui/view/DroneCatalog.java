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
import java.util.logging.Logger;

public class DroneCatalog extends JPanel implements TabbedPaneActivationListener {
    private static final Logger log = Logger.getLogger(DroneSimulationInterfaceAPI.class.getName());

    // GUI
    private JPanel contentPanel;

    private final AutoRefresh autoRefresh = new AutoRefresh();

    public DroneCatalog() {
        initialize();
    }

    private void initialize() {
        try {
            setLayout(new BorderLayout());

            JPanel titlePanel = createTitlePanel();
            add(titlePanel, BorderLayout.NORTH);

            JScrollPane scrollPane = createContentScrollPane();
            populateContentPanelAsync();
            add(scrollPane, BorderLayout.CENTER);

            revalidate();
            repaint();
        } catch (DroneAPIException e) {
            APIErrorPanel errorPanel = new APIErrorPanel(action -> {
                removeAll();
                initialize();
            }, "Failed to fetch drones");
            removeAll();
            add(errorPanel);
        }
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

    private void populateContentPanelAsync() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                populateContentPanel();
                return null;
            }
        };
        worker.execute(); // Start t
    }

    private void populateContentPanel() {
        contentPanel.removeAll();
        Map<Integer, DroneType> drones = fetchDrones();

        for (DroneType drone : drones.values()) {
            contentPanel.add(new DroneCardPanel(drone));
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JScrollPane createContentScrollPane() throws DroneAPIException {
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

    @Override
    public void onActivate() {
        autoRefresh.start(
                this::populateContentPanelAsync,
                120,
                60,
                TimeUnit.SECONDS
        );
    }

    @Override
    public void onDeactivate() {
        autoRefresh.stop();
    }
}