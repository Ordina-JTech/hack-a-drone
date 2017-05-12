package com.github.nberlijn.hackadrone;

import java.io.IOException;

public interface Connection {

    void connect() throws IOException;

    void disconnect() throws IOException;

}
