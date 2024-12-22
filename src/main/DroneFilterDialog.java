package src.main;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class DroneFilterDialog extends JDialog {
    //private DroneFilter filter;
    private boolean confirmed = false;

    // Filter components
    private JComboBox<String> carriageTypeCombo;
    private JTextField minWeightField;
    private JTextField maxWeightField;
    private JComboBox<String> droneTypeCombo;
    private JComboBox<String> manufacturerCombo;
    private JTextField minBatteryField;
    private JTextField maxBatteryField;

    public DroneFilterDialog(JFrame parent) {
        super(parent, "Drone Filter", true);
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create filter components
        mainPanel.add(createFilterPanel());
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(createButtonPanel());

        add(mainPanel, BorderLayout.CENTER);

        // Set dialog size and position
        setSize(400, 500);
        setLocationRelativeTo(getParent());
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Carriage Type
        gbc.gridx = 0;
        gbc.gridy = 0;
        filterPanel.add(new JLabel("Carriage Type:"), gbc);

        carriageTypeCombo = new JComboBox<>(new String[]{"Any", "ACT", "SEN", "NOT"});
        gbc.gridx = 1;
        filterPanel.add(carriageTypeCombo, gbc);

        // Weight Range
        gbc.gridx = 0;
        gbc.gridy = 1;
        filterPanel.add(new JLabel("Weight Range (kg):"), gbc);

        JPanel weightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        minWeightField = new JTextField(5);
        maxWeightField = new JTextField(5);
        weightPanel.add(minWeightField);
        weightPanel.add(new JLabel("-"));
        weightPanel.add(maxWeightField);

        gbc.gridx = 1;
        filterPanel.add(weightPanel, gbc);

        // Drone Type
        gbc.gridx = 0;
        gbc.gridy = 2;
        filterPanel.add(new JLabel("Drone Type:"), gbc);

        droneTypeCombo = new JComboBox<>(new String[]{"Any", "Commercial", "Military", "Recreational"});
        gbc.gridx = 1;
        filterPanel.add(droneTypeCombo, gbc);

        // Manufacturer
        gbc.gridx = 0;
        gbc.gridy = 3;
        filterPanel.add(new JLabel("Manufacturer:"), gbc);

        manufacturerCombo = new JComboBox<>(new String[]{"Any", "Altair Aerial", "Autel Robotics", "Blade"});
        gbc.gridx = 1;
        filterPanel.add(manufacturerCombo, gbc);

        // Battery Range
        gbc.gridx = 0;
        gbc.gridy = 4;
        filterPanel.add(new JLabel("Battery Range (%):"), gbc);

        JPanel batteryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        minBatteryField = new JTextField(5);
        maxBatteryField = new JTextField(5);
        batteryPanel.add(minBatteryField);
        batteryPanel.add(new JLabel("-"));
        batteryPanel.add(maxBatteryField);

        gbc.gridx = 1;
        filterPanel.add(batteryPanel, gbc);

        return filterPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton applyButton = new JButton("Apply");
        JButton clearButton = new JButton("Clear");
        JButton cancelButton = new JButton("Cancel");

        // Apply button action
        applyButton.addActionListener(e -> {
            confirmed = true;
            createFilterFromInputs();
            dispose();
        });

        // Clear button action
        clearButton.addActionListener(e -> {
            carriageTypeCombo.setSelectedIndex(0);
            minWeightField.setText("");
            maxWeightField.setText("");
            droneTypeCombo.setSelectedIndex(0);
            manufacturerCombo.setSelectedIndex(0);
            minBatteryField.setText("");
            maxBatteryField.setText("");
        });

        // Cancel button action
        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        buttonPanel.add(applyButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(cancelButton);

        return buttonPanel;
    }

    private void createFilterFromInputs() {
        //DroneFilter.Builder builder = new DroneFilter.Builder();

        // Add carriage type if not "Any"
        if (carriageTypeCombo.getSelectedIndex() > 0) {
            //builder.withCarriageType((String) carriageTypeCombo.getSelectedItem());
        }

        // Add weight range if provided
        try {
            Integer minWeight = minWeightField.getText().isEmpty() ? null :
                    Integer.parseInt(minWeightField.getText());
            Integer maxWeight = maxWeightField.getText().isEmpty() ? null :
                    Integer.parseInt(maxWeightField.getText());
            if (minWeight != null || maxWeight != null) {
                //builder.withWeightRange(minWeight, maxWeight);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid weight range values",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        // Add other filters...

        //filter = builder.build();
    }

    /*
    public DroneFilter getFilter() {
        return filter;
    }
     */

    public boolean isConfirmed() {
        return confirmed;
    }
}

