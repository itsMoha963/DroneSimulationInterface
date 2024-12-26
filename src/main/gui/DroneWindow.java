package src.main.gui;

import com.formdev.flatlaf.ui.FlatLineBorder;
import src.main.core.Drone;
import src.main.core.parser.DroneParser;
import src.main.services.DroneFilterService;
import src.main.services.DroneSimulationInterfaceAPI;
import src.main.utils.Colors;
import src.main.utils.DroneFilter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DroneWindow extends JPanel {

    private JPanel innerContentPanel;

    public DroneWindow() {
        setLayout(new BorderLayout());
        createToolBar();
        innerContentPanel = new JPanel();
        innerContentPanel.setLayout(new FlowLayout());
        innerContentPanel.setBackground(Colors.EERIE_BLACK);

        DroneSimulationInterfaceAPI api = new DroneSimulationInterfaceAPI();

        ArrayList<Drone> drones = null;
        try {
            drones = api.fetchDroneData(new DroneParser());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(drones.size() + " Drones fetched");

        for (int i = 0; i < drones.size(); i++) {
            innerContentPanel.add(createDronePanel(drones.get(i)));
        }

        add(innerContentPanel);
        setVisible(true);
    }

    private void createToolBar() {
        JToolBar toolBar = new JToolBar();

        JComboBox<String> droneTypeComboBox = new JComboBox<>(new String[]{
                "All Types",
                "NOT",
                "ACT",
                "SEN"
        });
        droneTypeComboBox.addActionListener(e -> filterDronesByType( (String)droneTypeComboBox.getSelectedItem() ) );

        toolBar.add(new JLabel("Refresh: "));
        JButton refreshButton = createToolBarButtonHelper("Refresh", "🔄");
        refreshButton.addActionListener(e -> {
            DroneFilterWindow filterWindow = new DroneFilterWindow();
            filterWindow.setVisible(true);
        });
        toolBar.add(refreshButton);
        toolBar.addSeparator();
        toolBar.add(new JLabel("Filter: "));
        toolBar.add(droneTypeComboBox);

        add(toolBar, BorderLayout.NORTH);
    }

    private JButton createToolBarButtonHelper(String tooltip, String text) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);
        button.setBackground(Colors.GUNMETAL);
        button.setForeground(Colors.PLATINUM);
        return button;
    }

    private void filterDronesByType(String type) {
        System.out.println("Filtering Drones by Type: " + type);

        DroneSimulationInterfaceAPI api = new DroneSimulationInterfaceAPI();

        ArrayList<Drone> drones = null;
        try {
            drones = api.fetchDroneData(new DroneParser());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (drones == null) {
            System.err.println("No Drones to Filter");
            return;
        }

        innerContentPanel.removeAll();

        DroneFilter filter = new DroneFilter.Builder()
                .weightRange(0, 10)
                .carriageType("ACT")
                .build();

        DroneFilterService droneFilterService = new DroneFilterService();
        List<Drone> filteredDrones = droneFilterService.filterDrones(drones, filter);

        System.out.println(filteredDrones.size() + " Drones after Filtering");

        for (int i = 0; i < filteredDrones.size(); i++) {
            innerContentPanel.add(createDronePanel(filteredDrones.get(i)));
        }

        innerContentPanel.revalidate();
        innerContentPanel.repaint();
    }

    private JPanel createDronePanel(Drone drone) {
        JPanel dronePanel = new JPanel();

        dronePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dronePanel.setLayout(new GridLayout(5, 1));
        dronePanel.setBackground(Colors.GUNMETAL);

        JLabel droneIDLabel = createLabelHelper(drone.getId() + "");
        droneIDLabel.setFont(new Font("Arial", Font.BOLD, 32));

        JLabel droneCarriageTypeLabel = createLabelHelper("Carriage Type: " + drone.getCarriageType());
        JLabel droneCarriageWeightLabel = createLabelHelper("Carriage Weight: " + drone.getCarriageWeight() + "g");
        JLabel droneSerialNumberLabel = createLabelHelper("Serial Number: " + drone.getSerialNumber());

        // TODO: Drone Type is a URL to the Dronetypes API, have to compare ID and fetch Information from Dronetypes
        //JLabel droneTypeLabel = createLabelHelper("Drone Type: " + drone.getDroneType());

        dronePanel.add(droneIDLabel);
        dronePanel.add(droneCarriageTypeLabel);
        dronePanel.add(droneCarriageWeightLabel);
        dronePanel.add(droneSerialNumberLabel);

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
