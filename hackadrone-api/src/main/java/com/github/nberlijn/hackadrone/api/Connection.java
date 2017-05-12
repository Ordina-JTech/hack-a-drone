package com.github.nberlijn.hackadrone.api;

import java.io.IOException;

public interface Connection {

    void connect() throws IOException;

    void disconnect() throws IOException;

}
