package com.github.ordina.jtech.hackadrone;

import com.github.ordina.jtech.hackadrone.io.Device;
import com.github.ordina.jtech.hackadrone.net.Connection;

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

    String getName();

}
