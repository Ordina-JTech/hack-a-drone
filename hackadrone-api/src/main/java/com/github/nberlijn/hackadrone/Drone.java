package com.github.nberlijn.hackadrone;

import com.github.nberlijn.hackadrone.exceptions.DroneException;

public interface Drone extends Connection {

    @Override
    void connect() throws DroneException;

    @Override
    void disconnect() throws DroneException;

    String getName();

}
