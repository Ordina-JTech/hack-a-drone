package com.github.nberlijn.hackadrone;

import com.github.nberlijn.hackadrone.io.Device;
import com.github.nberlijn.hackadrone.net.Connection;

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

    String getName();

}
