package src.main.gui;

import com.formdev.flatlaf.ui.FlatLineBorder;
import src.main.core.DynamicDrone;
import src.main.core.parser.DynamicDroneParser;
import src.main.services.DroneSimulationInterfaceAPI;
import src.main.utils.Colors;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.util.ArrayList;

public class DynamicDroneWindow extends JPanel {
    private JPanel innerContentPanel;

    public DynamicDroneWindow() {
        setLayout(new BorderLayout());
        setBackground(Colors.EERIE_BLACK);

        innerContentPanel = new JPanel();
        innerContentPanel.setBackground(Colors.EERIE_BLACK);
        innerContentPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Or use GridLayout

        JScrollPane scrollPane = new JScrollPane(innerContentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        updateDroneView();
        createPagePanel();

        adjustInnerPanelSize(100);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void createPagePanel() {
        JPanel pagePanel = new JPanel();
        pagePanel.setLayout(new BorderLayout());
        pagePanel.setBackground(Colors.EERIE_BLACK);
        pagePanel.add(new Button("Next Page"), BorderLayout.EAST);
        pagePanel.add(new Button("Previous Page"), BorderLayout.WEST);
        pagePanel.add(new JLabel("1"), BorderLayout.CENTER);
        add(pagePanel, BorderLayout.SOUTH);
    }

    private void updateDroneView() {
        DroneSimulationInterfaceAPI api = new DroneSimulationInterfaceAPI();

        ArrayList<DynamicDrone> drones = new ArrayList<>();
        try {
            drones = api.fetchDroneData(new DynamicDroneParser());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        innerContentPanel.removeAll();

        for (DynamicDrone drone : drones) {
            innerContentPanel.add(createDronePanel(drone));
        }

        innerContentPanel.revalidate();
        innerContentPanel.repaint();
    }

    private void adjustInnerPanelSize(int itemCount) {
        int rows = (itemCount / 4) + ((itemCount % 4 > 0) ? 1 : 0);
        int panelWidth = 600; // Adjust as needed
        int panelHeight = rows * 150; // Adjust based on item height
        innerContentPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        innerContentPanel.revalidate();
        innerContentPanel.repaint();
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
}
