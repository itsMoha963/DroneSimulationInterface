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
    private static final Logger log = Logger.getLogger(DroneCatalog.class.getName());

    // GUI
    private JPanel contentPanel;

    private final AutoRefresh autoRefresh = new AutoRefresh();

    public DroneCatalog() {
        initialize();
    }

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

    private void populateContentPanelAsync() throws DroneAPIException {
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

    private void populateContentPanel(Map<Integer, DroneType> drones) throws DroneAPIException{
        contentPanel.removeAll();

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