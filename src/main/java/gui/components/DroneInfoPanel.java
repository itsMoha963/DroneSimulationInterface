package gui.components;

import javax.swing.*;
import java.awt.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import core.drone.*;
import services.Helper;

/**
 * The Drone Information Panel for the drones passed in the constructor
 * Displays information such as the battery, status, traveled distance and more.
 */
public class DroneInfoPanel extends JPanel {

    /**
     * Simply create the DroneInformation panel for the given DynamicDrone
     * @param drones The Sample for which the information will be calculated
     * @param drone The Drone that belongs to this sample
     * @param droneType The DroneType that belongs to this sample
     */
    public DroneInfoPanel(ArrayList<DynamicDrone> drones, Drone drone, DroneType droneType) {
        setLayout(new BorderLayout());
        setupPanel(drones, drone, droneType);
    }

    private void setupPanel(ArrayList<DynamicDrone> drones, Drone drone, DroneType droneType) {
        DynamicDrone latestDynamicDrone = drones.getLast();

        // Create main info panel
        JPanel mainInfo = new JPanel(new GridLayout(4, 1, 10, 10));
        mainInfo.setBackground(UIManager.getColor("Panel.background").brighter());

        // Cant extract this method as we would then have to loop through the drones twice.
        // Once to calculate the average speed and once to calculate the total distance.
        // Calculates the total distance and average speed the drone traveled for the last DRONE_SAMPLE_SIZE samples.
        double totalDistance = 0;
        double averageSpeed = 0;
        for (int i = 1; i < drones.size(); i++) {
            DynamicDrone prev = drones.get(i - 1);
            DynamicDrone curr = drones.get(i);
            totalDistance += Helper.haversineDistance(prev.getLongitude(), prev.getLatitude(), curr.getLongitude(), curr.getLatitude());
            averageSpeed += curr.getSpeed();
        }
        averageSpeed = averageSpeed / drones.size();

        double carriageLast = (double) drone.getCarriageWeight() / (double) droneType.getWeight();
        carriageLast = carriageLast * 100;                  // To get the actual percentage

        // Create info boxes with data
        mainInfo.add(createInfoBox("Current Speed", String.format("%.1f km/h", (double) latestDynamicDrone.getSpeed())));
        mainInfo.add(createInfoBox("Total Distance", String.format("%.2f km", totalDistance / 1000)));
        mainInfo.add(createInfoBox("Location",
                String.format("%.6f, %.6f", latestDynamicDrone.getLongitude(), latestDynamicDrone.getLatitude())));
        mainInfo.add(createInfoBox("Average Speed", String.format("%.2f km/h", averageSpeed)));
        mainInfo.add(createInfoBox("Carriage Last", String.format("%.2f", carriageLast) + " %"));
        mainInfo.add(createInfoBox("Last Seen", formatTime(latestDynamicDrone.getLastSeen())));

        mainInfo.add(createInfoBox("General Info", "<html>" + "Carriage Type: "
                + drone.getCarriageType() + "<br/>Manufacturer: "
                + droneType.getManufacturer() + "<br/>Model: "
                + droneType.getTypeName() + "</html>")); // Need to add html for line breaks to work
        mainInfo.add(createInfoBox("Allignment", "<html>" + "Roll: "
                + latestDynamicDrone.getAlignRoll() + "<br/>Pitch: "
                + latestDynamicDrone.getAlignPitch() + "<br/>Yaw: "
                + latestDynamicDrone.getAlignYaw() + "</html>")); // Need to add html for line breaks to work

        // Add all components to main panel
        add(createStatusBar(latestDynamicDrone, drone, droneType), BorderLayout.NORTH);
        add(mainInfo, BorderLayout.CENTER);
    }

    private JPanel createInfoBox(String title, String value) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.setBackground(UIManager.getColor("Panel.background").brighter());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIManager.getColor("Panel.foreground").darker().darker().darker()),
                BorderFactory.createEmptyBorder(10, 15, 10, 10)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(UIManager.getColor("Label.foreground").brighter());

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 16));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatusIndicator(DynamicDrone latestDynamicDrone) {
        JPanel statusIndicator = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(latestDynamicDrone.getStatus().equals("ON") ? Color.GREEN : Color.RED);
                g2.fillOval(0, 0, 20, 20);
            }
        };
        statusIndicator.setPreferredSize(new Dimension(20, 20));
        return statusIndicator;
    }

    private JPanel createStatusBar(DynamicDrone dynamicDrone, Drone drone, DroneType droneType) {
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        statusBar.setOpaque(false);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Add timestamp
        JLabel timestampLabel = new JLabel("Timestamp: " + formatTime(dynamicDrone.getTimestamp()));
        timestampLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusBar.add(new BatteryPanel(dynamicDrone.getBatteryStatus(), droneType.getBatteryCapacity()));

        // Add components to status bar
        statusBar.add(timestampLabel);
        statusBar.add(createStatusIndicator(dynamicDrone));
        statusBar.add(new JLabel("SN: " + drone.getSerialNumber()));

        return statusBar;
    }

    private String formatTime(String timestamp) {
        OffsetDateTime lastSeenTime = OffsetDateTime.parse(timestamp);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return lastSeenTime.format(formatter);
    }
}
