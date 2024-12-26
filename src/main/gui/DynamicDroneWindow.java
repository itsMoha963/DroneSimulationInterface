package src.main.gui;

import com.formdev.flatlaf.ui.FlatLineBorder;
import src.main.core.DynamicDrone;
import src.main.core.parser.DynamicDroneParser;
import src.main.services.DroneSimulationInterfaceAPI;
import src.main.utils.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.util.ArrayList;

public class DynamicDroneWindow extends JPanel {
    private JPanel innerContentPanel;

    // TODO: Bandaid fix for proper wrapping of elements
    private int droneCount = 0;

    public DynamicDroneWindow() {
        setLayout(new BorderLayout());
        setBackground(Colors.EERIE_BLACK);

        innerContentPanel = new JPanel();
        innerContentPanel.setBackground(Colors.EERIE_BLACK);
        innerContentPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JScrollPane scrollPane = new JScrollPane(innerContentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        DroneSimulationInterfaceAPI api = new DroneSimulationInterfaceAPI();

        ArrayList<DynamicDrone> drones = new ArrayList<>();
        try {
            drones = api.fetchDroneData(new DynamicDroneParser());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        droneCount = drones.size();

        System.out.println("Total Dynamic Drones: " + drones.size());

        for (int i = 0; i < drones.size(); i++) {
            innerContentPanel.add(createDronePanel(drones.get(i)));
        }

        adjustInnerPanelSize(drones.size());

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createDronePanel(DynamicDrone drone) {
        JPanel dronePanel = new JPanel();
        dronePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dronePanel.setLayout(new GridLayout(11, 1));
        dronePanel.setBackground(Colors.GUNMETAL);

        dronePanel.add(createLabelHelper("Drone: " + drone.getDrone()));
        dronePanel.add(createLabelHelper("Timestamp: " + drone.getTimestamp()));
        dronePanel.add(createLabelHelper("Speed: " + drone.getSpeed()));
        dronePanel.add(createLabelHelper("Align Roll: " + drone.getAlignRoll()));
        dronePanel.add(createLabelHelper("Align Pitch: " + drone.getAlignPitch()));
        dronePanel.add(createLabelHelper("Align Yaw: " + drone.getAlignYaw()));
        dronePanel.add(createLabelHelper("Longitude: " + drone.getLongitude()));
        dronePanel.add(createLabelHelper("Latitude: " + drone.getLatitude()));
        dronePanel.add(createLabelHelper("Battery Status: " + drone.getBatteryStatus()));
        dronePanel.add(createLabelHelper("Last Seen: " + drone.getLastSeen()));
        dronePanel.add(createLabelHelper("Status: " + drone.getStatus()));

        dronePanel.setBorder( new FlatLineBorder( new Insets( 16, 16, 16, 16 ), null, 0, 32 ) );

        return dronePanel;
    }

    private JLabel createLabelHelper(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(Colors.PLATINUM);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        return label;
    }

    /*
    ?????????????????????????????????????????
     */
    private void adjustInnerPanelSize(int droneCount) {
        int panelWidth = 220; // Breite eines DronePanels + Padding
        int panelHeight = 310; // Höhe eines DronePanels + Padding
        int panelsPerRow = 3; // Beispiel: Anzahl der Panels pro Reihe vor Wrapping

        // Berechnung von Breite und Höhe
        int totalWidth = panelsPerRow * panelWidth;
        int totalRows = (int) Math.ceil(droneCount / (double) panelsPerRow);
        int totalHeight = totalRows * panelHeight;

        innerContentPanel.setPreferredSize(new Dimension(totalWidth, totalHeight));
        innerContentPanel.revalidate(); // Panel-Layout neu berechnen
    }
}
