package gui;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * A generic error panel with a retry button.
 */
public class APIErrorPanel extends JPanel {

    /**
     * Create a generic error panel that can be reused for all views.
     * Assigns {@code listener} to the retry button.
     * Displays {code errorMessage} on the screen.
     * @param listener
     * @param errorMessage
     */
    public APIErrorPanel(Consumer<Void> listener, String errorMessage) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton retryButton = new JButton("Retry");
        retryButton.addActionListener(e -> listener.accept(null));

        JButton exitProgramButton = new JButton("Exit Program");
        exitProgramButton.addActionListener(e -> System.exit(0));

        JLabel label = new JLabel(errorMessage);
        label.setFont(label.getFont().deriveFont(35f));
        label.setForeground(Color.RED);
        label.setAlignmentX(CENTER_ALIGNMENT);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.add(retryButton);
        buttonPanel.add(exitProgramButton);

        add(label);
        add(buttonPanel);
    }
}
