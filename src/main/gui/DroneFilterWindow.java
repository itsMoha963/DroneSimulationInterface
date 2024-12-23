package src.main.gui;

import javax.swing.*;
import java.awt.*;

public class DroneFilterWindow extends JDialog {
    public DroneFilterWindow() {
        super();
        createWindow();
    }

    public void createWindow() {
        setLayout(new BorderLayout());
        setTitle("Filter");

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        contentPanel.add(createCarriageTypeComboBox());
        contentPanel.add(createWeightRangePanel());

        add(contentPanel, BorderLayout.CENTER);
        setSize(300, 300);
        setLocationRelativeTo(getParent());
    }

    public JPanel createCarriageTypeComboBox() {
        JPanel carriageTypeComboBoxPanel = new JPanel();
        JComboBox<String> droneTypeComboBox = new JComboBox<>(new String[]{
                "All Types",
                "NOT",
                "ACT",
                "SEN"
        });

        carriageTypeComboBoxPanel.add(droneTypeComboBox);
        return carriageTypeComboBoxPanel;
    }

    public JPanel createWeightRangePanel() {
        JPanel weightRangePanel = new JPanel();
        JLabel weightRangeLabel = new JLabel("Weight Range: ");

        JTextField minWeightTextField = new JFormattedTextField();
        JTextField maxWeightTextField = new JFormattedTextField();

        weightRangePanel.add(weightRangeLabel);
        weightRangePanel.add(minWeightTextField);
        weightRangePanel.add(new JLabel(" - "));
        weightRangePanel.add(maxWeightTextField);
        return weightRangePanel;
    }
}
