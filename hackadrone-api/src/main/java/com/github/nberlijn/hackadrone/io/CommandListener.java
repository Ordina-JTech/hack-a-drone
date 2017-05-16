package com.github.nberlijn.hackadrone.io;

import com.github.nberlijn.hackadrone.models.Command;

public interface CommandListener {

    void onCommandReceived(Command command);

}
