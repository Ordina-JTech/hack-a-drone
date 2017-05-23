package com.github.ordina.jtech.hackadrone.net;

import com.github.ordina.jtech.hackadrone.models.Command;

import java.io.IOException;

public interface Execute {

    void sendCommand(Command command) throws IOException;

}
