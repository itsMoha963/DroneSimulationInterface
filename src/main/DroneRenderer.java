package src.main;

import javax.swing.*;
import java.awt.*;

public class DroneRenderer extends JPanel implements ListCellRenderer<Drone> {
    private JLabel nameLabel;
    private JLabel carriageLabel;
    private JLabel weightLabel;
    private JLabel iconLabel;

    public DroneRenderer() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.LIGHT_GRAY);
        nameLabel = new JLabel();
        carriageLabel = new JLabel();
        weightLabel = new JLabel();

        JPanel textPanel = new JPanel(new GridLayout(3, 1));
        textPanel.add(nameLabel);
        textPanel.add(carriageLabel);
        textPanel.add(weightLabel);

        add(textPanel, BorderLayout.CENTER);

        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Drone> list, Drone drone, int index, boolean isSelected, boolean cellHasFocus) {
        nameLabel.setText("Name: " + drone.getName());
        carriageLabel.setText("Carriage: " + drone.getCarriageType());
        weightLabel.setText("Weight: " + drone.getWeight() + " kg");

        // Highlight selected cell
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        setOpaque(true); // Ensure background is painted
        return this;
    }
}