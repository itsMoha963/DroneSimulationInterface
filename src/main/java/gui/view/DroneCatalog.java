package gui.view;

import core.drone.DroneType;
import core.parser.DroneTypeParser;
import gui.components.APIErrorPanel;
import services.DroneSimulationInterfaceAPI;
import utils.Constants;
import exception.DroneAPIException;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class DroneCatalog extends JPanel {

    public DroneCatalog() {
        initialize();
    }

    private void initialize() {
        try {
            setLayout(new BorderLayout());

            JPanel titlePanel = createTitlePanel();
            add(titlePanel, BorderLayout.NORTH);

            JScrollPane scrollPane = createContentScrollPane();
            add(scrollPane, BorderLayout.CENTER);
            revalidate();
            repaint();
        } catch (DroneAPIException e) {
            APIErrorPanel errorPanel = new APIErrorPanel(action -> {
                removeAll();
                initialize();
            }, "Failed to fetch drones");
            removeAll();
            add(errorPanel);
        }
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Drone Catalog", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(UIManager.getColor("Label.background"));
        titleLabel.setForeground(UIManager.getColor("Label.foreground"));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        return titlePanel;
    }

    private JLabel createInfoLabel(String text, int fontSize) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
        label.setForeground(UIManager.getColor("Label.foreground"));
        label.setAlignmentX(Component.LEFT_ALIGNMENT); // Left-aligned
        return label;
    }

    private JScrollPane createContentScrollPane() throws DroneAPIException {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(0, 3, 15, 15)); // 3 columns with spacing
        contentPanel.setBackground(UIManager.getColor("Panel.background"));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Map<Integer, DroneType> drones = fetchDrones();

        for (DroneType drone : drones.values()) {
            contentPanel.add(createDroneCard(drone));
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        return scrollPane;
    }

    private Map<Integer, DroneType> fetchDrones() throws DroneAPIException {
        return DroneSimulationInterfaceAPI.getInstance().fetchDrones(new DroneTypeParser(), 40, 0);
    }

    private JPanel createDroneCard(DroneType droneType) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UIManager.getColor("Panel.background").darker());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setPreferredSize(new Dimension(200, 250)); // Smaller card size
        panel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        ImageIcon originalIcon = new ImageIcon(getClass().getResource(Constants.DRONE_ICON_PATH));
        Image scaledImage = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Smaller icon
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel iconLabel = new JLabel(scaledIcon);
        iconLabel.setHorizontalAlignment(JLabel.LEFT);
        iconLabel.setVerticalAlignment(JLabel.TOP);
        topPanel.add(iconLabel, BorderLayout.WEST);

        JLabel nameLabel = new JLabel(droneType.getManufacturer() + " " + droneType.getTypeName(), JLabel.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12)); // Slightly smaller font
        nameLabel.setForeground(UIManager.getColor("Label.foreground"));
        topPanel.add(nameLabel, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);

        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS)); // Vertical alignment
        detailPanel.setOpaque(false);

        detailPanel.add(Box.createVerticalStrut(10));

        detailPanel.add(createInfoLabel("Weight: " + droneType.getWeight() + " g", 16));
        detailPanel.add(createInfoLabel("Max Speed: " + droneType.getMaxSpeed() + " km/h", 16));
        detailPanel.add(createInfoLabel("Battery Capacity: " + droneType.getBatteryCapacity() + " mAh", 16));
        detailPanel.add(createInfoLabel("Control Range: " + droneType.getControlRange() + " m", 16));
        detailPanel.add(createInfoLabel("Max Carriage: " + droneType.getMaxCarriage() + " g", 16));
        panel.add(detailPanel, BorderLayout.CENTER);

        return panel;
    }
}
