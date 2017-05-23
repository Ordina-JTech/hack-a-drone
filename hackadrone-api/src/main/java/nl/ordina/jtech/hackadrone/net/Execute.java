package nl.ordina.jtech.hackadrone.net;

import nl.ordina.jtech.hackadrone.models.Command;

import java.io.IOException;

public interface Execute {

    void sendCommand(Command command) throws IOException;

}
