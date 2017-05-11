package com.github.nberlijn.hackadrone;

import com.github.nberlijn.hackadrone.net.TransportConnection;
import com.github.nberlijn.hackadrone.utils.ByteUtils;

import java.io.IOException;

public final class CX10WD implements Drone {

    private static final String NAME = "CX-10WD-TX";
    private static final String HOST = "172.16.10.1";
    private static final int PORT = 8888;
    private TransportConnection transportConnection;

    public void connect() throws IOException {
        transportConnection = new TransportConnection(HOST, PORT);
        transportConnection.connect();
        transportConnection.sendMessage(ByteUtils.loadMessageFromFile("message1.bin"), 106);
        transportConnection.sendMessage(ByteUtils.loadMessageFromFile("message2.bin"), 106);
        transportConnection.sendMessage(ByteUtils.loadMessageFromFile("message3.bin"), 170);
        transportConnection.sendMessage(ByteUtils.loadMessageFromFile("message4.bin"), 106);
        transportConnection.sendMessage(ByteUtils.loadMessageFromFile("message5.bin"), 106);
    }

    public void disconnect() throws IOException {
        if (transportConnection != null) {
            transportConnection.disconnect();
        }
    }

    public String getName() {
        return NAME;
    }

}
