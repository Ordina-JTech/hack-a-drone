package nl.ordina.jtech.hackadrone.io;

/**
 * Interface representing a device.
 *
 * @author Nils Berlijn
 * @version 1.0
 * @since 1.0
 */
public interface Device extends Handler {

    /**
     * Sets the listener.
     *
     * @param commandListener the command listener to set
     */
    void setListener(CommandListener commandListener);

}
