package com.github.nberlijn.hackadrone.net;

import java.io.IOException;

public interface Transport {

    void sendMessage(byte[] bytes, int responseSize) throws IOException;

}
