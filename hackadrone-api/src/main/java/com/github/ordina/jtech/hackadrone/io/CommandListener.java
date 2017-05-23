package com.github.ordina.jtech.hackadrone.io;

import com.github.ordina.jtech.hackadrone.models.Command;

public interface CommandListener {

    void onCommandReceived(Command command);

}
