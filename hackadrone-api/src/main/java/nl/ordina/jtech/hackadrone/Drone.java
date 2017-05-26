package nl.ordina.jtech.hackadrone;

import nl.ordina.jtech.hackadrone.io.Device;
import nl.ordina.jtech.hackadrone.net.Connection;

public interface Drone extends Connection {

    @Override
    void connect() throws DroneException;

    @Override
    void disconnect() throws DroneException;

    void sendMessages() throws DroneException;

    void startHeartbeat() throws DroneException;

    void stopHeartbeat() throws DroneException;

    void startControls(Device device) throws DroneException;

    void stopControls() throws DroneException;

    void startVideoStream() throws DroneException;

    void stopVideoStream() throws DroneException;

    void startVideoRecord() throws DroneException;

    void stopVideoRecord() throws DroneException;

    void startAi() throws DroneException;

    void stopAi() throws DroneException;

    String getName();

}
