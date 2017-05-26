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

    void startCamera() throws DroneException;

    void stopCamera() throws DroneException;

    void startRecorder() throws DroneException;

    void stopRecorder() throws DroneException;

    void startAi() throws DroneException;

    void stopAi() throws DroneException;

    String getName();

}
