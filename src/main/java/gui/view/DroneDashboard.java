package gui.view;

import core.Drone;
import core.parser.DroneParser;
import services.DroneSimulationInterfaceAPI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DroneDashboard extends JPanel {
    private final Logger log = Logger.getLogger(DroneDashboard.class.getName());
    private final JPanel dronesPanel;

    public DroneDashboard() {
        setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        dronesPanel = new JPanel();
        dronesPanel.setLayout(new BoxLayout(dronesPanel, BoxLayout.Y_AXIS));
        dronesPanel.setBackground(UIManager.getColor("Panel.background"));

        JScrollPane leftScrollPane = new JScrollPane(dronesPanel);
        leftScrollPane.setBorder(BorderFactory.createEtchedBorder(UIManager.getColor("Panel.background").brighter(),
                UIManager.getColor("Panel.background").darker()));

        leftScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        leftScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.weightx = 0.1;                                       // Fills 10% of the screen horizontally
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        add(leftScrollPane, gridBagConstraints);

        JPanel rightPanel = new JPanel();
        rightPanel.setBorder(BorderFactory.createEtchedBorder(UIManager.getColor("Panel.background").brighter(),
                UIManager.getColor("Panel.background").darker()));
        rightPanel.setBackground(UIManager.getColor("Panel.background"));
        gridBagConstraints.gridx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.weightx = 0.9;                                       // Fills 90% of the screen
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        add(rightPanel, gridBagConstraints);

        try {
            loadDrones();
        } catch (IOException | InterruptedException e) {
            log.log(Level.SEVERE, "Failed to load Drones.");
        }
    }

    private void loadDrones() throws IOException, InterruptedException {
        dronesPanel.removeAll();

        // Only 20 Drones exist, so we only fetch 20 drones.
        Map<Integer, Drone> drones = DroneSimulationInterfaceAPI.getInstance().fetchDrones(new DroneParser(), 20, 0);

        for (Drone drone : drones.values()) {
            dronesPanel.add(createDroneButton(drone));
        }

        log.log(Level.INFO, "Successfully fetched " + drones.size() + "Drones. Repainting.");

        dronesPanel.revalidate();
        dronesPanel.repaint();
    }

    /**
     * Called when User clicks on a Button, to load Drone Information
     * Calculates Custom Information from existing Data.
     * - How much Battery time is left
     * - Drone Carriage ballast
     * @param drone
     */
    private void loadDronePage(Drone drone) {

    }

    // TODO: Resize Buttons to fill horizontally
    private JButton createDroneButton(Drone drone) {
        JButton button = new JButton();
        button.setText("Drone " + drone.getId());
        return button;
    }
}
