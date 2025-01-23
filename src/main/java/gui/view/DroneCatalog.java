package gui.view;

import core.DroneType;
import core.parser.DroneParser;
import core.parser.DroneTypeParser;
import services.DroneSimulationInterfaceAPI;
import utils.DroneAPIException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DroneCatalog extends JPanel {
    private final Logger log = Logger.getLogger(DroneCatalog.class.getName());

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public DroneCatalog() {
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel droneListPanel = createDroneListPanel();
        mainPanel.add(droneListPanel, "DroneList");

        add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, "DroneList");
    }

    /*
    TODO:
            - SWITCH OUT Hardcoded COLORS for UIManager.getColor("property")
            - can then be modified using .brighter() or .darker()
     */

    private JPanel createDroneListPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel titlePanel = createTitlePanel();
        panel.add(titlePanel, BorderLayout.NORTH);

        try {
            Map<Integer, DroneType> droneTypes = DroneSimulationInterfaceAPI.getInstance().fetchDrones(new DroneTypeParser(), 40, 0);
            if (droneTypes.isEmpty()) { throw new DroneAPIException("Error in fetching Drone Types. DroneTypes is empty"); }
            JPanel contentPanel = createContentPanel(droneTypes);
            JScrollPane scrollPane = new JScrollPane(contentPanel);
            configureScrollPane(scrollPane, titlePanel, panel);
            panel.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error while creating DroneListPanel.");
            panel.add(createErrorHandlingPanel(e), BorderLayout.CENTER);
        }

        return panel;
    }

    private JPanel createErrorHandlingPanel(Exception e) {
        JPanel errorPanel = new JPanel(new BorderLayout());
        errorPanel.setBackground(UIManager.getColor("Panel.background"));

        // Error Message
        JLabel errorLabel = new JLabel("Failed to Load Drone Catalog", JLabel.CENTER);
        errorLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        errorLabel.setForeground(Color.RED);

        // Detailed Error Message
        JTextArea errorDetails = new JTextArea(e.getMessage());
        errorDetails.setEditable(false);
        errorDetails.setBackground(UIManager.getColor("Panel.background"));
        errorDetails.setForeground(UIManager.getColor("Label.foreground"));

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton retryButton = new JButton("Retry");
        retryButton.addActionListener(e1 -> {
            // Reload the entire drone catalog panel
            mainPanel.remove(mainPanel.getComponent(0));
            mainPanel.add(createDroneListPanel(), "DroneList");
            cardLayout.show(mainPanel, "DroneList");
        });

        JButton exitButton = new JButton("Exit Program");
        exitButton.addActionListener(e1 -> System.exit(0));

        buttonPanel.add(retryButton);
        buttonPanel.add(exitButton);

        // Assemble Error Panel
        errorPanel.add(errorLabel, BorderLayout.NORTH);
        errorPanel.add(new JScrollPane(errorDetails), BorderLayout.CENTER);
        errorPanel.add(buttonPanel, BorderLayout.SOUTH);

        return errorPanel;
    }

    private JPanel createContentPanel(Map<Integer, DroneType> droneTypes) {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UIManager.getColor("Panel.background"));
        contentPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), BorderFactory.createEtchedBorder(UIManager.getColor("Panel.background").brighter(),
                UIManager.getColor("Panel.background").darker())));

        for (DroneType drone : droneTypes.values()) {
            contentPanel.add(createDronePanel(drone));
            contentPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Spacing between cards
        }
        return contentPanel;
    }

    private void configureScrollPane(JScrollPane scrollPane, JPanel titlePanel, JPanel contentPanel) {
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);

        // Add scroll listener to show/hide title panel
        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            private boolean isTitleVisible = true;

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                boolean shouldShowTitle = scrollPane.getVerticalScrollBar().getValue() == 0;
                if (shouldShowTitle != isTitleVisible) {
                    titlePanel.setVisible(shouldShowTitle);
                    isTitleVisible = shouldShowTitle;
                    contentPanel.revalidate();
                    contentPanel.repaint();
                }
            }
        });
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Drone Catalog", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(UIManager.getColor("Label.background"));
        titleLabel.setForeground(UIManager.getColor("Label.foreground"));

        titlePanel.setBorder(BorderFactory.createEtchedBorder(UIManager.getColor("Panel.background").brighter(),
                UIManager.getColor("Panel.background").darker()));
        return titlePanel;
    }

    private JPanel createDronePanel(DroneType droneType) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();

                // Activate anti-aliasing
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Shadow properties
                int shadowOffset = 10;
                int shadowAlpha = 50;
                g2d.setColor(new Color(0, 0, 0, shadowAlpha));
                g2d.fillRoundRect(shadowOffset, shadowOffset, getWidth() - shadowOffset * 2, getHeight() - shadowOffset * 2, 15, 15);

                g2d.dispose();
            }
        };

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        panel.setBackground(UIManager.getColor("Panel.background"));

        JLabel titleLabel = new JLabel(droneType.getManufacturer() + " " + droneType.getTypeName(), JLabel.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(UIManager.getColor("Label.foreground"));

        JLabel detailsLabel = new JLabel("Weight: " + droneType.getWeight() + "kg, Max Speed: " + droneType.getMaxSpeed() + " km/h");
        detailsLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        detailsLabel.setForeground(UIManager.getColor("Label.foreground"));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(detailsLabel);

        panel.add(textPanel, BorderLayout.CENTER);

        // Add hover effect
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showDroneDetails(droneType);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                panel.setBackground(UIManager.getColor("Panel.background").brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                panel.setBackground(UIManager.getColor("Panel.background"));
            }
        });

        return panel;
    }

    private void showDroneDetails(DroneType drone) {
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        detailsPanel.setBackground(UIManager.getColor("Panel.background").darker());

        // Back button
        JButton backButton = new JButton("<-");// Back button??? or should it be an arrow????
        backButton.setPreferredSize(new Dimension(40, 30));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(UIManager.getColor("Button.background").brighter());
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "DroneList"));

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setOpaque(false);
        backButtonPanel.add(backButton);
        detailsPanel.add(backButtonPanel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        infoPanel.setBackground(UIManager.getColor("Panel.background").brighter());

        JLabel title = new JLabel(drone.getManufacturer() + " - " + drone.getTypeName(), JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(title);

        JSeparator separator = new JSeparator();
        separator.setForeground(Color.GRAY);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        infoPanel.add(separator);

        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel detailCard = new JPanel();
        detailCard.setLayout(new GridLayout(0, 2, 10, 10));
        detailCard.setOpaque(false);
        detailCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        detailCard.add(createInfoLabel("Weight:", 18));
        detailCard.add(createInfoLabel(drone.getWeight() + "kg", 18));
        detailCard.add(createInfoLabel("Max Speed:", 18));
        detailCard.add(createInfoLabel(drone.getMaxSpeed() + " km/h", 18));
        detailCard.add(createInfoLabel("Battery Capacity:", 18));
        detailCard.add(createInfoLabel(drone.getBatteryCapacity() + "mAh", 18));
        detailCard.add(createInfoLabel("Control Range:", 18));
        detailCard.add(createInfoLabel(drone.getControlRange() + "m", 18));
        detailCard.add(createInfoLabel("Max Carriage:", 18));
        detailCard.add(createInfoLabel(drone.getMaxCarriage() + "kg", 18));

        infoPanel.add(detailCard);
        detailsPanel.add(infoPanel, BorderLayout.CENTER);

        mainPanel.add(detailsPanel, "DroneDetails");
        cardLayout.show(mainPanel, "DroneDetails");
    }

    private JLabel createInfoLabel(String text, int fontSize) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setForeground(UIManager.getColor("Label.foreground").brighter());
        return label;
    }
}
