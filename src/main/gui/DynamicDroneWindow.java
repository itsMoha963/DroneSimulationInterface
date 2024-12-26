package src.main.gui;

import com.formdev.flatlaf.ui.FlatLineBorder;
import src.main.core.DynamicDrone;
import src.main.core.parser.DynamicDroneParser;
import src.main.services.DroneSimulationInterfaceAPI;
import src.main.utils.Colors;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DynamicDroneWindow extends JPanel {
    private JPanel innerContentPanel;
    private JLabel pageLabel;
    private int currentPage = 1;

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
        pagePanel.setBackground(Colors.POWDER_BLUE);
    
        JButton nextButton = new JButton(">");
        JButton prevButton = new JButton("<");
        pageLabel = new JLabel(currentPage + "", SwingConstants.CENTER);
        pageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pageLabel.setForeground(Colors.GUNMETAL);

        nextButton.addActionListener(e -> {
            currentPage++;
            pageLabel.setText(currentPage + "");
            System.out.println("Current Page: " + currentPage);
            updateDroneView();
        });

        prevButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                updateDroneView();
            }
            System.out.println("Current Page: " + currentPage);
            pageLabel.setText(currentPage + "");
        });
    
        pagePanel.add(nextButton, BorderLayout.EAST);
        pagePanel.add(prevButton, BorderLayout.WEST);
        pagePanel.add(pageLabel, BorderLayout.CENTER);
        add(pagePanel, BorderLayout.SOUTH);
    }

    private void updateDroneView() {
        DroneSimulationInterfaceAPI api = new DroneSimulationInterfaceAPI();

        ArrayList<DynamicDrone> drones = new ArrayList<>();
        try {
            drones = api.fetchDroneData(new DynamicDroneParser(), 16, (currentPage - 1) * 16 + 1);
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