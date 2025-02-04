package gui.components;

import com.formdev.flatlaf.ui.FlatRoundBorder;
import core.drone.DroneType;
import utils.Constants;

import javax.swing.*;
import java.awt.*;

public class DroneCardPanel extends JPanel {
    public DroneCardPanel(DroneType droneType) {
        setLayout(new BorderLayout());

        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setPreferredSize(new Dimension(200, 250)); // Smaller card size

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

        add(topPanel, BorderLayout.NORTH);

        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS)); // Vertical alignment
        detailPanel.setOpaque(false);

        detailPanel.add(Box.createVerticalStrut(10));

        detailPanel.add(createInfoLabel("Weight: " + droneType.getWeight() + " g", 16));
        detailPanel.add(createInfoLabel("Max Speed: " + droneType.getMaxSpeed() + " km/h", 16));
        detailPanel.add(createInfoLabel("Battery Capacity: " + droneType.getBatteryCapacity() + " mAh", 16));
        detailPanel.add(createInfoLabel("Control Range: " + droneType.getControlRange() + " m", 16));
        detailPanel.add(createInfoLabel("Max Carriage: " + droneType.getMaxCarriage() + " g", 16));
        add(detailPanel, BorderLayout.CENTER);
    }

    private JLabel createInfoLabel(String text, int fontSize) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
        label.setForeground(UIManager.getColor("Label.foreground"));
        label.setAlignmentX(Component.LEFT_ALIGNMENT); // Left-aligned
        return label;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(UIManager.getColor("Panel.background").darker());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
    }
}