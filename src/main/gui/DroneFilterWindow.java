package src.main.gui;

import src.main.utils.DefaultDroneFilter;

import javax.swing.*;
import java.awt.*;

public class DroneFilterWindow extends JDialog {
    private JComboBox<String> carriageTypeComboBox;
    private JSpinner minWeightSpinner;
    private JSpinner maxWeightSpinner;
    private JComboBox<String> droneTypeComboBox;

    private DroneWindow window;
    private DefaultDroneFilter currentFilter;

    public DroneFilterWindow(DroneWindow window, DefaultDroneFilter currentFilter) {
        setTitle("Drone Filter");
        this.window = window;
        this.currentFilter = currentFilter;
        init();
        pack();
    }

    private void init() {
        String[] carriageTypes = {"All Types", "NOT", "ACT", "SEN"};
        carriageTypeComboBox = new JComboBox<>(carriageTypes);

        // Initialize weight range filters
        SpinnerNumberModel minWeightModel = new SpinnerNumberModel(0.0, 0.0, 1000.0, 1);
        SpinnerNumberModel maxWeightModel = new SpinnerNumberModel(1000.0, 0.0, 1000.0, 1);

        minWeightSpinner = new JSpinner(minWeightModel);
        maxWeightSpinner = new JSpinner(maxWeightModel);

        JSpinner.NumberEditor minWeightEditor = new JSpinner.NumberEditor(minWeightSpinner, "##0");
        JSpinner.NumberEditor maxWeightEditor = new JSpinner.NumberEditor(maxWeightSpinner, "##0");

        minWeightSpinner.setEditor(minWeightEditor);
        maxWeightSpinner.setEditor(maxWeightEditor);

        // Add change listeners to be sure min <= max
        minWeightSpinner.addChangeListener(e -> {
            double minVal = (Double) minWeightSpinner.getValue();
            double maxVal = (Double) maxWeightSpinner.getValue();
            if (minVal > maxVal) {
                maxWeightSpinner.setValue(minVal);
            }
        });

        maxWeightSpinner.addChangeListener(e -> {
            double minVal = (Double) minWeightSpinner.getValue();
            double maxVal = (Double) maxWeightSpinner.getValue();
            if (maxVal < minVal) {
                minWeightSpinner.setValue(maxVal);
            }
        });


        String[] droneTypes = {"All", "Commercial", "Military", "Consumer", "Industrial"};
        droneTypeComboBox = new JComboBox<>(droneTypes);

        setLayout(new BorderLayout(10, 10));

        // Create main panel with GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add Type filter
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Carriage Type:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        mainPanel.add(carriageTypeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        mainPanel.add(new JLabel("Weight Range (g):"), gbc);

        JPanel weightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints weightGbc = new GridBagConstraints();
        weightGbc.insets = new Insets(0, 0, 0, 5);
        weightGbc.fill = GridBagConstraints.HORIZONTAL;
        weightGbc.weightx = 1.0;

        weightGbc.gridx = 0;
        weightPanel.add(new JLabel("Min:"), weightGbc);

        weightGbc.gridx = 1;
        weightPanel.add(minWeightSpinner, weightGbc);

        weightGbc.gridx = 2;
        weightPanel.add(new JLabel("Max:"), weightGbc);

        weightGbc.gridx = 3;
        weightPanel.add(maxWeightSpinner, weightGbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        mainPanel.add(weightPanel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton applyButton = new JButton("Apply");
        JButton cancelButton = new JButton("Cancel");
        JButton resetButton = new JButton("Reset");

        applyButton.addActionListener(e -> {
            createFilter();
            dispose();
        });

        cancelButton.addActionListener(e -> {

            dispose();
        });

        resetButton.addActionListener(e -> {
            resetFilter();
        });

        buttonPanel.add(applyButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(cancelButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        minWeightSpinner.setValue(Double.valueOf(currentFilter.getMinWeight()));
        maxWeightSpinner.setValue(Double.valueOf(currentFilter.getMaxWeight()));

        carriageTypeComboBox.setSelectedItem(currentFilter.getCarriageType());
    }

    public void resetFilter() {
        minWeightSpinner.setValue(0.0);
        maxWeightSpinner.setValue(1000.0);
        carriageTypeComboBox.setSelectedIndex(0);
        droneTypeComboBox.setSelectedIndex(0);
    }

    private void createFilter() {
        System.out.println(carriageTypeComboBox.getSelectedItem().toString());
        double min = (Double) minWeightSpinner.getValue();
        double max = (Double) maxWeightSpinner.getValue();

        DefaultDroneFilter filter = new DefaultDroneFilter(carriageTypeComboBox.getSelectedItem().toString(), (int) min, (int) max);

        window.setFilter(filter);
    }
}