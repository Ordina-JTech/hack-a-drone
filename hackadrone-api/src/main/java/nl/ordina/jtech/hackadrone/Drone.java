package nl.ordina.jtech.hackadrone;

import nl.ordina.jtech.hackadrone.io.Device;
import nl.ordina.jtech.hackadrone.net.Connection;

/**
 * Interface representing a drone.
 *
 * @author Nils Berlijn
 * @version 1.0
 * @since 1.0
 */
public interface Drone extends Connection {

    /**
     * Connects.
     *
     * @throws DroneException if the connection failed
     */
    @Override
    void connect() throws DroneException;

    /**
     * Disconnects.
     *
     * @throws DroneException if the disconnection failed
     */
    @Override
    void disconnect() throws DroneException;

    /**
     * Sends messages.
     *
     * @throws DroneException if sending the messages failed
     */
    void sendMessages() throws DroneException;

    /**
     * Starts the heartbeat.
     *
     * @throws DroneException if starting the heartbeat failed
     */
    void startHeartbeat() throws DroneException;

    /**
     * Stops the heartbeat.
     *
     * @throws DroneException if stopping the heartbeat failed
     */
    void stopHeartbeat() throws DroneException;

    /**
     * Starts the controls.
     *
     * @param device the device to use the controls from
     * @throws DroneException if starting the controls failed
     */
    void startControls(Device device) throws DroneException;

    /**
     * Stops the controls.
     *
     * @throws DroneException if stopping the controls failed
     */
    void stopControls() throws DroneException;

    /**
     * Starts the camera.
     *
     * @throws DroneException if starting the camera failed
     */
    void startCamera() throws DroneException;

    /**
     * Stops the camera.
     *
     * @throws DroneException if stopping the camera failed
     */
    void stopCamera() throws DroneException;

    /**
     * Starts the recorder.
     *
     * @throws DroneException if starting the recorder failed
     */
    void startRecorder() throws DroneException;

    /**
     * Stops the recorder.
     *
     * @throws DroneException if stopping the recorder failed
     */
    void stopRecorder() throws DroneException;

    /**
     * Starts the AI.
     *
     * @throws DroneException if starting the AI failed
     */
    void startAi() throws DroneException;

    /**
     * Stops the AI.
     *
     * @throws DroneException if stopping the AI failed
     */
    void stopAi() throws DroneException;

    /**
     * Gets the name of the drone.
     *
     * @return the name of the drone
     */
    String getName();

}
