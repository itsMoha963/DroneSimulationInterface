package gui.components;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * A generic error panel with a retry button.
 * This panel displays the error message and provides options to retry or exit the program.
 */
public class APIErrorPanel extends JPanel {

    /**
     * Creates a generic error panel with a retry and exit button.
     * Displays the given {@code errorMessage} and assigns the {@code listener} to the retry button.
     *
     * @param listener A {@link Consumer} that defines the action to be performed when the retry button is clicked.
     * @param errorMessage The error message to be displayed on the panel.
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