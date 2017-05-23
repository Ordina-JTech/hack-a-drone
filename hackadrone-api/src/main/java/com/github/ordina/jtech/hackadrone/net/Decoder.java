package com.github.ordina.jtech.hackadrone.net;

import java.io.IOException;

public interface Decoder extends Connection {

    byte[] read() throws IOException;

}
