package com.github.nberlijn.hackadrone.core;

import com.github.nberlijn.hackadrone.api.CX10;
import com.github.nberlijn.hackadrone.core.net.TransportConnection;
import com.github.nberlijn.hackadrone.exceptions.CX10Exception;
import com.github.nberlijn.hackadrone.utils.ByteUtils;

import java.io.IOException;

public final class CX10WD implements CX10 {

    private static final String NAME = "CX-10WD-TX";
    private static final String HOST = "172.16.10.1";
    private static final int PORT = 8888;
    private TransportConnection transportConnection;

    public void connect() throws CX10Exception {
        if (transportConnection == null) {
            transportConnection = new TransportConnection(HOST, PORT);
        }

        try {
            transportConnection.connect();
        } catch (IOException e) {
            throw new CX10Exception("Connection with the " + NAME + " failed!");
        }
    }

    public void disconnect() throws CX10Exception {
        try {
            transportConnection.disconnect();
        } catch (IOException | NullPointerException e) {
            throw new CX10Exception("Disconnecting with the " + NAME + " failed!");
        }
    }

    public void sendMessages() throws CX10Exception {
        try {
            transportConnection.sendMessage(ByteUtils.loadMessageFromFile("message1.bin"), 106);
            transportConnection.sendMessage(ByteUtils.loadMessageFromFile("message2.bin"), 106);
            transportConnection.sendMessage(ByteUtils.loadMessageFromFile("message3.bin"), 170);
            transportConnection.sendMessage(ByteUtils.loadMessageFromFile("message4.bin"), 106);
            transportConnection.sendMessage(ByteUtils.loadMessageFromFile("message5.bin"), 106);
        } catch (IOException e) {
            throw new CX10Exception("Sending messages to the " + NAME + " failed!");
        }
    }

    public String getName() {
        return NAME;
    }

}
