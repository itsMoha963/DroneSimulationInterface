package gui;

/**
 * This interface serves as a listener for when a TabbedPane gets activated or deactivated
 * by the user.
 */
public interface TabbedPaneActivationListener {
    /**
     * Gets called when User clicks on a Pane
     */
    void onActivate();

    /**
     * Gets called on the currently active Pane when the user switches to another
     */
    void onDeactivate();
}
