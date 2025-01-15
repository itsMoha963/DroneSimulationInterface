package gui.view;

import core.Drone;
import core.DroneType;
import core.DynamicDrone;
import core.parser.DroneParser;
import core.parser.DroneTypeParser;
import services.DroneSimulationInterfaceAPI;
import gui.BatteryPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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

        JScrollPane leftScrollPane = new JScrollPane(dronesPanel);
        leftScrollPane.setBorder(BorderFactory.createEtchedBorder(UIManager.getColor("Panel.background").brighter(),
                UIManager.getColor("Panel.background").darker()));

        leftScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        leftScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.weightx = 0.2; // Fills 20% of the screen horizontally
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        add(leftScrollPane, gridBagConstraints);

        droneInfoLabel = new JPanel();
        droneInfoLabel.setBorder(BorderFactory.createEtchedBorder(UIManager.getColor("Panel.background").brighter(),
                UIManager.getColor("Panel.background").darker()));
        droneInfoLabel.setBackground(UIManager.getColor("Panel.background"));


        gridBagConstraints.gridx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.weightx = 0.8; // Fills 80% of the screen
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        add(droneInfoLabel, gridBagConstraints);

        showPlaceholder();

        try {
            loadDrones();
        } catch (IOException | InterruptedException e) {
            log.log(Level.SEVERE, "Failed to load Drones.");
        }
        preWarm();
    }

    private void loadDrones() throws IOException, InterruptedException {
        dronesPanel.removeAll();

        // Only 40 Drones exist, so we only fetch 40 drones.
        Map<Integer, Drone> drones = DroneSimulationInterfaceAPI.getInstance().fetchDrones(new DroneParser(), 40, 0);

        for (Drone drone : drones.values()) {
            addDroneButton(drone);
        }

        log.log(Level.INFO, "Successfully fetched " + drones.size() + " Drones. Repainting.");

        dronesPanel.revalidate();
        dronesPanel.repaint();
    }
    public void addDroneButton(Drone drone) {
        JButton button = createDroneButton(drone);
        dronesPanel.add(button);
        dronesPanel.revalidate();
        dronesPanel.repaint();
    }
    private void preWarm() {
        try {
            droneTypesCache = DroneSimulationInterfaceAPI.getInstance().fetchDrones(new DroneTypeParser(), 40, 0);
            droneCache = DroneSimulationInterfaceAPI.getInstance().fetchDrones(new DroneParser(), 40, 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when User clicks on a Button, to load Drone Information
     * Calculates Custom Information from existing Data.
     * - How much Battery time is left
     * - Drone Carriage ballast
     * @param drone
     */
    private void loadDronePage(Drone drone) {
        droneInfoLabel.removeAll();

        int id = drone.getId();
        droneInfoLabel.removeAll();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Static drone details panel
        JPanel staticDetailsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        staticDetailsPanel.setBorder(BorderFactory.createTitledBorder("Static Drone Details"));
        Font labelFont = new Font("Arial", Font.PLAIN, 16); // Increase font size by 5-8 points

        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setFont(labelFont);
        staticDetailsPanel.add(typeLabel);
        JLabel typeValue = new JLabel(String.valueOf(drone.getId()));
        typeValue.setFont(labelFont);
        staticDetailsPanel.add(typeValue);

        JLabel weightLabel = new JLabel("Weight:");
        weightLabel.setFont(labelFont);
        staticDetailsPanel.add(weightLabel);
        JLabel weightValue = new JLabel(drone.getCarriageWeight() + " kg");
        weightValue.setFont(labelFont);
        staticDetailsPanel.add(weightValue);

        JLabel droneTypeLabel = new JLabel("Drone Type:");
        droneTypeLabel.setFont(labelFont);
        staticDetailsPanel.add(droneTypeLabel);

        String droneType = drone.getDroneType();
        String droneTypeId = "Unknown";
        if (droneType != null) {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(".*/(\\d+)/\\?format=json");
            java.util.regex.Matcher matcher = pattern.matcher(droneType);
            if (matcher.find()) {
                droneTypeId = matcher.group(1);
            }
        }
        JLabel droneTypeValue = new JLabel(droneTypeId);
        droneTypeValue.setFont(labelFont);
        staticDetailsPanel.add(droneTypeValue);

        JLabel createdLabel = new JLabel("Created:");
        createdLabel.setFont(labelFont);
        staticDetailsPanel.add(createdLabel);
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm");
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(drone.getCreated(), inputFormatter);
            String formattedCreated = offsetDateTime.format(outputFormatter);
            JLabel createdValue = new JLabel(formattedCreated);
            createdValue.setFont(labelFont);
            staticDetailsPanel.add(createdValue);
        } catch (DateTimeParseException e) {
            JLabel createdValue = new JLabel("Invalid date format");
            createdValue.setFont(labelFont);
            staticDetailsPanel.add(createdValue);
        }

        Drone ddrone = droneCache.get(drone.getId());
        DroneType type = droneTypesCache.get(ddrone.getDroneTypeID());

        int maxCarriageWeight = type.getMaxCarriage();
        int currentCarriageWeight = drone.getCarriageWeight();
        double loadPercentage = ((double) currentCarriageWeight / maxCarriageWeight) * 100;

        JLabel maxWeightLabel = new JLabel("Max Carriage Weight:");
        maxWeightLabel.setFont(labelFont);
        staticDetailsPanel.add(maxWeightLabel);
        JLabel maxWeightValue = new JLabel(maxCarriageWeight + " kg");
        maxWeightValue.setFont(labelFont);
        staticDetailsPanel.add(maxWeightValue);

        JLabel currentWeightLabel = new JLabel("Current Carriage Weight:");
        currentWeightLabel.setFont(labelFont);
        staticDetailsPanel.add(currentWeightLabel);
        JLabel currentWeightValue = new JLabel(currentCarriageWeight + " kg");
        currentWeightValue.setFont(labelFont);
        staticDetailsPanel.add(currentWeightValue);

        JLabel loadPercentageLabel = new JLabel("Load Percentage:");
        loadPercentageLabel.setFont(labelFont);
        staticDetailsPanel.add(loadPercentageLabel);
        JLabel loadPercentageValue = new JLabel(String.format("%.2f%%", loadPercentage));
        loadPercentageValue.setFont(labelFont);
        staticDetailsPanel.add(loadPercentageValue);

        mainPanel.add(staticDetailsPanel);

        // Dynamic data panel
        JPanel dynamicDataPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        dynamicDataPanel.setBorder(BorderFactory.createTitledBorder("Dynamic Data"));

        double totalSpeed = 0;
        int speedCount = 0;
        double totalDistance = 0;
        double previousLatitude = 0;
        double previousLongitude = 0;
        boolean isFirstPoint = true;

        int maxBatteryCapacity = type.getBatteryCapacity();
        int initialBatteryCapacity = maxBatteryCapacity;
        int finalBatteryCapacity = 0;

        try {
            ArrayList<DynamicDrone> dynamics = DroneSimulationInterfaceAPI.getInstance().fetchDrones(id, 40, 0);

            for (DynamicDrone d : dynamics) {
                totalSpeed += d.getSpeed();
                speedCount++;

                double currentLatitude = d.getLatitude();
                double currentLongitude = d.getLongitude();

                if (!isFirstPoint) {
                    double distance = calculateHaversineDistance(previousLatitude, previousLongitude, currentLatitude, currentLongitude);
                    totalDistance += distance;
                } else {
                    isFirstPoint = false;
                }

                previousLatitude = currentLatitude;
                previousLongitude = currentLongitude;

                finalBatteryCapacity = d.getBatteryStatus();
            }

            if (speedCount > 0) {
                double averageSpeed = totalSpeed / speedCount;
                JLabel avgSpeedLabel = new JLabel("Average Speed:");
                avgSpeedLabel.setFont(labelFont);
                dynamicDataPanel.add(avgSpeedLabel);
                JLabel avgSpeedValue = new JLabel(String.format("%.2f km/h", averageSpeed));
                avgSpeedValue.setFont(labelFont);
                dynamicDataPanel.add(avgSpeedValue);
            } else {
                JLabel avgSpeedLabel = new JLabel("Average Speed:");
                avgSpeedLabel.setFont(labelFont);
                dynamicDataPanel.add(avgSpeedLabel);
                JLabel avgSpeedValue = new JLabel("No data available");
                avgSpeedValue.setFont(labelFont);
                dynamicDataPanel.add(avgSpeedValue);
            }

            JLabel totalDistanceLabel = new JLabel("Total Distance:");
            totalDistanceLabel.setFont(labelFont);
            dynamicDataPanel.add(totalDistanceLabel);
            JLabel totalDistanceValue = new JLabel(String.format("%.2f km", totalDistance));
            totalDistanceValue.setFont(labelFont);
            dynamicDataPanel.add(totalDistanceValue);

            int batteryConsumed = initialBatteryCapacity - finalBatteryCapacity;
            JLabel batteryConsumedLabel = new JLabel("Battery Consumed:");
            batteryConsumedLabel.setFont(labelFont);
            dynamicDataPanel.add(batteryConsumedLabel);
            JLabel batteryConsumedValue = new JLabel(batteryConsumed + " mAh");
            batteryConsumedValue.setFont(labelFont);
            dynamicDataPanel.add(batteryConsumedValue);

        } catch (IOException | RuntimeException | InterruptedException e) {
            JLabel errorLabel = new JLabel("Error fetching dynamic data:");
            errorLabel.setFont(labelFont);
            dynamicDataPanel.add(errorLabel);
            JLabel errorMessage = new JLabel(e.getMessage());
            errorMessage.setFont(labelFont);
            dynamicDataPanel.add(errorMessage);
        }

        mainPanel.add(dynamicDataPanel);

        // Add BatteryPanel to display battery status visually
        BatteryPanel batteryPanel = new BatteryPanel(finalBatteryCapacity, maxBatteryCapacity);
        batteryPanel.setBorder(BorderFactory.createTitledBorder("Battery Status"));
        mainPanel.add(batteryPanel);

        // Create a JScrollPane to make the content scrollable
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Add the JScrollPane to the droneInfoLabel
        droneInfoLabel.setLayout(new BorderLayout());
        droneInfoLabel.add(scrollPane, BorderLayout.CENTER);

        // Refresh the layout
        droneInfoLabel.revalidate();
        droneInfoLabel.repaint();
    }



    //generated by chatgpt
    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371; // Radius of the earth in km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    private JButton createDroneButton(Drone drone) {
        JButton button = new JButton("Drone " + drone.getId());
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

        button.addActionListener(e -> loadDronePage(drone));
        return button;
    }

    private void showPlaceholder() {
        droneInfoLabel.removeAll();

        // Platzhalter
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

}