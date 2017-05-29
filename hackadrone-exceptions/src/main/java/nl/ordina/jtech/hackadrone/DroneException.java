package nl.ordina.jtech.hackadrone;

import java.io.IOException;

/**
 * Class representing a drone exception.
 *
 * @author Nils Berlijn
 * @version 1.0
 * @since 1.0
 */
public final class DroneException extends IOException {

    /**
     * A drone exception constructor.
     *
     * @param message the message that will be given by the exception
     */
    DroneException(String message) {
        super(message);
    }

}
