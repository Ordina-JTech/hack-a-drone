package com.github.ordina.jtech.hackadrone.net;

import java.io.IOException;

public interface Transport {

    void sendMessage(byte[] bytes, int responseSize) throws IOException;

}
