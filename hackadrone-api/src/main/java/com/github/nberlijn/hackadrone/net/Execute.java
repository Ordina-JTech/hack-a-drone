package com.github.nberlijn.hackadrone.net;

import com.github.nberlijn.hackadrone.models.Command;

import java.io.IOException;

public interface Execute {

    void sendCommand(Command command) throws IOException;

}
