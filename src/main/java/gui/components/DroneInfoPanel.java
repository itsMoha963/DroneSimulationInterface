package gui.components;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import core.drone.*;
import gui.components.BatteryPanel;
import services.Helper;

public class DroneInfoPanel extends JPanel {

    public DroneInfoPanel(ArrayList<DynamicDrone> drones, Drone drone, DroneType droneType) {
        setLayout(new BorderLayout());
        setupPanel(drones, drone, droneType);
    }

    private void setupPanel(ArrayList<DynamicDrone> drones, Drone drone, DroneType droneType) {
        DynamicDrone latestDynamicDrone = drones.getLast();

        // Create the StatusBar (ON/OFF, TimeStamp, SerialNumber...)
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        statusBar.setOpaque(false);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        OffsetDateTime dateTime = OffsetDateTime.parse(latestDynamicDrone.getTimestamp());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDate = dateTime.format(formatter);

        // Add timestamp
        JLabel timestampLabel = new JLabel("Timestamp: " + formattedDate);
        timestampLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusBar.add(new BatteryPanel(latestDynamicDrone.getBatteryStatus(), droneType.getBatteryCapacity()));

        // Add components to status bar
        statusBar.add(timestampLabel);
        statusBar.add(createStatusIndicator(latestDynamicDrone));
        statusBar.add(new JLabel("SN: " + drone.getSerialNumber()));

        // Create main info panel
        JPanel mainInfo = new JPanel(new GridLayout(4, 1, 10, 10));
        mainInfo.setBackground(UIManager.getColor("Panel.background").brighter());

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

        // Formatting last seen to get the right DateTime
        OffsetDateTime lastSeenTime = OffsetDateTime.parse(latestDynamicDrone.getLastSeen());
        String formattedLastSeen = lastSeenTime.format(formatter);

        // Create info boxes with data
        mainInfo.add(createInfoBox("Current Speed", String.format("%.1f km/h", (double) latestDynamicDrone.getSpeed())));
        mainInfo.add(createInfoBox("Total Distance", String.format("%.2f km", totalDistance / 1000)));
        mainInfo.add(createInfoBox("Location",
                String.format("%.6f, %.6f", latestDynamicDrone.getLongitude(), latestDynamicDrone.getLatitude())));
        mainInfo.add(createInfoBox("Average Speed", String.format("%.2f km/h", averageSpeed)));
        mainInfo.add(createInfoBox("Carriage Last", String.format("%.2f", carriageLast) + " %"));
        mainInfo.add(createInfoBox("Last Seen", formattedLastSeen));

        mainInfo.add(createInfoBox("General Info", "<html>" + "Carriage Type: "
                + drone.getCarriageType() + "<br/>Manufacturer: "
                + droneType.getManufacturer() + "<br/>Model: "
                + droneType.getTypeName() + "</html>")); // Need to add html for line breaks to work

        // Add all components to main panel
        add(statusBar, BorderLayout.NORTH);
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
}
