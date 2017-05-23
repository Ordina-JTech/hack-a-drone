package nl.ordina.jtech.hackadrone.io;

import nl.ordina.jtech.hackadrone.models.Command;

public interface CommandListener {

    void onCommandReceived(Command command);

}
