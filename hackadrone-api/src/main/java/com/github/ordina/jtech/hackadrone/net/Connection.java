package com.github.ordina.jtech.hackadrone.net;

import java.io.IOException;

public interface Connection {

    void connect() throws IOException;

    void disconnect() throws IOException;

}
