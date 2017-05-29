package nl.ordina.jtech.hackadrone.net;

import java.io.IOException;

/**
 * Interface representing a connection.
 *
 * @author Nils Berlijn
 * @version 1.0
 * @since 1.0
 */
public interface Connection {

    /**
     * Connects.
     *
     * @throws IOException if the connection failed
     */
    void connect() throws IOException;

    /**
     * Disconnects.
     *
     * @throws IOException if the disconnection failed
     */
    void disconnect() throws IOException;

}
