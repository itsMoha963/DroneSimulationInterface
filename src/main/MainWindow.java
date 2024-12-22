package src.main;

import com.formdev.flatlaf.ui.FlatLineBorder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends JFrame {

    // Color Pallet: TODO: dont Hardcode Color Pallet
    public static final Color PLATINUM = new Color(216, 219, 226);
    public static final Color POWDER_BLUE = new Color(169, 188, 208);
    public static final Color MOONSTONE = new Color(88, 164, 176);
    public static final Color GUNMETAL = new Color(39, 43, 52);
    public static final Color EERIE_BLACK = new Color(27, 27, 30);

    private final JPanel contentPanel;

    public MainWindow() {
        setTitle("Drone Simulation Interface");
        setSize(900, 1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        try {
            setIconImage(ImageIO.read(new File("Icons/appicon.png")));
        } catch (IOException e) {
            System.err.println("Failed to load App Icon: " + e.getMessage());
        }

        createToolBar();

        contentPanel = new JPanel();
        contentPanel.setLayout(new FlowLayout());
        contentPanel.setBackground(new Color(27, 27, 30));

        ArrayList<Drone> drones = Main.FetchDrones();

        for (int i = 0; i < drones.size(); i++) {
            Drone tmpDrone = drones.get(i);
            contentPanel.add(createDronePanel(tmpDrone.getId(), tmpDrone.getCarriageType(), tmpDrone.getWeight()));
        }

        contentPanel.setVisible(true);
        add(contentPanel);
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
        toolBar.add(createToolBarButtonHelper("Refresh", "🔄"));
        toolBar.addSeparator();
        toolBar.add(new JLabel("Filter: "));
        toolBar.add(droneTypeComboBox);

        add(toolBar, BorderLayout.NORTH);
    }

    private JButton createToolBarButtonHelper(String tooltip, String text) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);
        button.setBackground(GUNMETAL);
        button.setForeground(PLATINUM);
        return button;
    }

    private void filterDronesByType(String type) {
        System.out.println("Filtering Drones by Type: " + type);

        // Not sure if we should refetch or work with the existing Drones.
        ArrayList<Drone> drones = Main.FetchDrones();

        contentPanel.removeAll();

        for (int i = 0; i < drones.size(); i++) {
            if ( type.equals(drones.get(i).getCarriageType()) || type.equals("All Types") ) {
                contentPanel.add(createDronePanel(drones.get(i).getId(), drones.get(i).getCarriageType(), drones.get(i).getWeight()));
            }
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createDronePanel(int id, String type, int weight) {
        JPanel dronePanel = new JPanel();

        dronePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dronePanel.setLayout(new GridLayout(3, 1));
        dronePanel.setBackground(GUNMETAL);

        JLabel droneIDLabel = new JLabel(id + "", SwingConstants.CENTER);
        droneIDLabel.setFont(new Font("Arial", Font.BOLD, 32));
        droneIDLabel.setForeground(PLATINUM);

        JLabel droneCarriageTypeLabel = new JLabel("Carriage Type: " + type, SwingConstants.CENTER);
        droneCarriageTypeLabel.setForeground(PLATINUM);
        droneCarriageTypeLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel droneCarriageWeightLabel = new JLabel("Carriage Weight: " + weight + "g", SwingConstants.CENTER);
        droneCarriageWeightLabel.setForeground(PLATINUM);
        droneCarriageWeightLabel.setFont(new Font("Arial", Font.BOLD, 12));

        dronePanel.add(droneIDLabel);
        dronePanel.add(droneCarriageTypeLabel);
        dronePanel.add(droneCarriageWeightLabel);

        dronePanel.setBorder( new FlatLineBorder( new Insets( 16, 16, 16, 16 ), null, 0, 32 ) );

        return dronePanel;
    }
}
