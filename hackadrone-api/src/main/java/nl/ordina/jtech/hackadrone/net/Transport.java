package nl.ordina.jtech.hackadrone.net;

import java.io.IOException;

/**
 * Interface representing a transport.
 *
 * @author Nils Berlijn
 * @version 1.0
 * @since 1.0
 */
public interface Transport {

    /**
     * Sends a message.
     *
     * @param bytes the bytes to send
     * @param responseSize the size of the response bytes
     * @throws IOException if sending the message failed
     */
    void sendMessage(byte[] bytes, int responseSize) throws IOException;

}
