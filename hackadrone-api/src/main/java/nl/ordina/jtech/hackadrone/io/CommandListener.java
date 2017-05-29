package nl.ordina.jtech.hackadrone.io;

import nl.ordina.jtech.hackadrone.models.Command;

/**
 * Interface representing a command listener.
 *
 * @author Nils Berlijn
 * @version 1.0
 * @since 1.0
 */
public interface CommandListener {

    /**
     * Handles the received command.
     *
     * @param command the command to handle
     */
    void onCommandReceived(Command command);

}
