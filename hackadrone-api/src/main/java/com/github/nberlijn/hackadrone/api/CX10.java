package com.github.nberlijn.hackadrone.api;

import com.github.nberlijn.hackadrone.exceptions.CX10Exception;

public interface CX10 extends Drone, Connection {

    @Override
    void connect() throws CX10Exception;

    @Override
    void disconnect() throws CX10Exception;

    void sendMessages() throws CX10Exception;

}
