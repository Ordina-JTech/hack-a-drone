package nl.ordina.jtech.hackadrone.gui;

/**
 * Interface representing a click event.
 *
 * @author Nils Berlijn
 * @version 1.0
 * @since 1.0
 */
public interface ClickEvent {

    /**
     * Handles the connect button.
     */
    void onConnectClicked();

    /**
     * Handles the controls button.
     */
    void onControlsClicked();

    /**
     * Handles the camera button.
     */
    void onCameraClicked();

    /**
     * Handles the recorder button.
     */
    void onRecorderClicked();

    /**
     * Handles AI button.
     */
    void onAiClicked();

}
