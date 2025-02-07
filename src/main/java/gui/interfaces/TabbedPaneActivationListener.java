package gui.interfaces;

/**
 * This interface serves as a listener for tracking when a tab in a TabbedPane is activated or deactivated by the user.
*/
public interface TabbedPaneActivationListener {
    /**
     * Gets called when User clicks on a Pane.
     */
    void onActivate();

    /**
     * Gets called on the currently active Pane when the user switches to another.
     */
    void onDeactivate();
}
