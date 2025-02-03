package gui.components;

import core.drone.*;

import javax.swing.*;
import java.awt.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * A panel to display all the information of the DynamicDrone relevant for the FlightDynamics Tab
 */
public class FlightDynamicsPanel extends JPanel {

    /**
     * Creates the JPanel for the given data
     * @param dynamicDrone The DynamicDrone for which the JPanel is created
     * @param drone The Drone which belongs to the DynamicDrone
     * @param droneType The DroneType which belongs to the Drone
     */
    public FlightDynamicsPanel(DynamicDrone dynamicDrone, Drone drone, DroneType droneType) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Panel.background").darker(), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setBackground(UIManager.getColor("Panel.background"));

        JLabel titleLabel = new JLabel("Drone | ID: " + drone.getId());
        titleLabel.setForeground(UIManager.getColor("Label.foreground"));
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel detailsLabel = new JLabel(String.format(
                "Speed: %.2f km/h, Last Seen: %s, Status: %s",
                (double) dynamicDrone.getSpeed(), OffsetDateTime.parse(dynamicDrone.getLastSeen()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm")),
                dynamicDrone.getStatus()
        ));
        detailsLabel.setForeground(UIManager.getColor("Label.disabledForeground"));

        JLabel locationLabel = new JLabel(String.format(
                "Location: [%.6f, %.6f] | Control Range: %.2f m",
                dynamicDrone.getLongitude(), dynamicDrone.getLatitude(), (double) droneType.getControlRange()
        ));
        locationLabel.setForeground(UIManager.getColor("Label.disabledForeground"));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(detailsLabel);
        textPanel.add(locationLabel);

        JPanel statusPanel = new JPanel();
        statusPanel.setOpaque(false);
        statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

        BatteryPanel batteryPanel = new BatteryPanel(dynamicDrone.getBatteryStatus(), droneType.getBatteryCapacity());
        statusPanel.add(batteryPanel);

        JPanel powerStatus = new JPanel();
        powerStatus.setBackground(Objects.equals(dynamicDrone.getStatus(), "ON") ? Color.GREEN : Color.RED);
        powerStatus.setPreferredSize(new Dimension(15, 15));
        statusPanel.add(powerStatus);

        textPanel.add(statusPanel);
        add(textPanel, BorderLayout.CENTER);
    }
}