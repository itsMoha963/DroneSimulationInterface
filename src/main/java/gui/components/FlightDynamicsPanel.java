package gui.components;

import core.drone.*;

import javax.swing.*;
import java.awt.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * A panel to display all the information of the DynamicDrone relevant for the FlightDynamics Tab.
 */
public class FlightDynamicsPanel extends JPanel {

    /**
     * Constructs the FlightDynamicsPanel to display the given dynamic drone's information.
     *
     * @param dynamicDrone The DynamicDrone object containing real-time data for the drone.
     * @param drone The Drone object belonging to {@code dynamicDrone}.
     * @param droneType The DroneType object that belongs to {@code drone}.
     */
    public FlightDynamicsPanel(DynamicDrone dynamicDrone, Drone drone, DroneType droneType) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Panel.background").darker(), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setBackground(UIManager.getColor("Panel.background"));

        JLabel titleLabel = createTitleLabel(drone);
        JLabel detailsLabel = createDetailsLabel(dynamicDrone);
        JLabel locationLabel = createLocationLabel(dynamicDrone, droneType);
        JLabel alignmentLabel = createAlignmentLabel(dynamicDrone);

        JPanel statusPanel = createStatusPanel(dynamicDrone, droneType);
        JPanel textPanel = createTextPanel(titleLabel, detailsLabel, locationLabel, alignmentLabel, statusPanel);

        add(textPanel, BorderLayout.CENTER);
    }

    private JPanel createTextPanel(JLabel titleLabel, JLabel detailsLabel, JLabel locationLabel, JLabel alignmentLabel, JPanel statusPanel) {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(detailsLabel);
        textPanel.add(locationLabel);
        textPanel.add(alignmentLabel);
        textPanel.add(statusPanel); // Add status panel at the end
        return textPanel;
    }

    private JPanel createStatusPanel(DynamicDrone dynamicDrone, DroneType droneType) {
        JPanel statusPanel = new JPanel();
        statusPanel.setOpaque(false);
        statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

        BatteryPanel batteryPanel = new BatteryPanel(dynamicDrone.getBatteryStatus(), droneType.getBatteryCapacity());
        statusPanel.add(batteryPanel);

        JPanel powerStatus = new JPanel();
        powerStatus.setBackground(Objects.equals(dynamicDrone.getStatus(), "ON") ? Color.GREEN : Color.RED);
        powerStatus.setPreferredSize(new Dimension(15, 15));
        statusPanel.add(powerStatus);

        return statusPanel;
    }

    private JLabel createAlignmentLabel(DynamicDrone dynamicDrone) {
        return new JLabel("Allignments: Yaw:" + dynamicDrone.getAlignYaw() +
                " | Pitch:" + dynamicDrone.getAlignPitch() +
                " | Roll:" + dynamicDrone.getAlignRoll());
    }

    private JLabel createLocationLabel(DynamicDrone dynamicDrone, DroneType droneType) {
        return new JLabel(String.format(
                "Location: [%.6f, %.6f] | Control Range: %.2f m",
                dynamicDrone.getLongitude(), dynamicDrone.getLatitude(), (double) droneType.getControlRange()
        ));
    }

    private JLabel createDetailsLabel(DynamicDrone dynamicDrone) {
        return new JLabel(String.format(
                "Speed: %.2f km/h, Last Seen: %s, Status: %s",
                (double) dynamicDrone.getSpeed(),
                OffsetDateTime.parse(dynamicDrone.getLastSeen()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm")),
                dynamicDrone.getStatus()
        ));
    }

    private JLabel createTitleLabel(Drone drone) {
        JLabel titleLabel = new JLabel("Drone | ID: " + drone.getId());
        titleLabel.setForeground(UIManager.getColor("Label.foreground"));
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        return titleLabel;
    }
}