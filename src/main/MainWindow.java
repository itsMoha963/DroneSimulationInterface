package src.main;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainWindow extends JFrame {

    private JList<String> droneList;


    public MainWindow() {
        setTitle("Drone Simulation Interface");
        setSize(900, 1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel panel = new JPanel();

        JButton fetchDrones = new JButton("Fetch Drones");
        fetchDrones.addActionListener(action -> fetchDrones());

        droneList = new JList<>();
        droneList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(droneList);
        panel.add(scrollPane, BorderLayout.CENTER);

        panel.add(fetchDrones);
        panel.setVisible(true);
        add(panel);
    }

    public void fetchDrones()
    {
        // Fetch this from the API
        List<String> droneData = List.of(
                "Drone 91: carriage type NOT (weight: 0g)",
                "Drone 92: carriage type ACT (weight: 28g)",
                "Drone 93: carriage type ACT (weight: 51g)",
                "Drone 94: carriage type SEN (weight: 52g)",
                "Drone 95: carriage type ACT (weight: 46g)",
                "Drone 96: carriage type NOT (weight: 0g)",
                "Drone 97: carriage type SEN (weight: 33g)",
                "Drone 98: carriage type NOT (weight: 0g)",
                "Drone 99: carriage type ACT (weight: 4g)",
                "Drone 100: carriage type ACT (weight: 9g)"
        );

        droneList.setListData(droneData.toArray(new String[0]));
    }
}
