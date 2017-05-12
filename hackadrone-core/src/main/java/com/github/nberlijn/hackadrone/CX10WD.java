package com.github.nberlijn.hackadrone;

import com.github.nberlijn.hackadrone.exceptions.DroneException;
import com.github.nberlijn.hackadrone.net.TransportConnection;
import com.github.nberlijn.hackadrone.utils.ByteUtils;

import java.io.IOException;

public final class CX10WD implements Drone {

    private static final String NAME = "CX-10WD-TX";
    private static final String HOST = "172.16.10.1";
    private static final int PORT = 8888;
    private TransportConnection transportConnection;

    public void connect() throws DroneException {
        transportConnection = new TransportConnection(HOST, PORT);

        try {
            transportConnection.connect();
            transportConnection.sendMessage(ByteUtils.loadMessageFromFile("message1.bin"), 106);
            transportConnection.sendMessage(ByteUtils.loadMessageFromFile("message2.bin"), 106);
            transportConnection.sendMessage(ByteUtils.loadMessageFromFile("message3.bin"), 170);
            transportConnection.sendMessage(ByteUtils.loadMessageFromFile("message4.bin"), 106);
            transportConnection.sendMessage(ByteUtils.loadMessageFromFile("message5.bin"), 106);
        } catch (IOException e) {
            throw new DroneException("Connection with the " + NAME + " failed!");
        }
    }

    public void disconnect() throws DroneException {
        try {
            transportConnection.disconnect();
        } catch (IOException | NullPointerException e) {
            throw new DroneException("Disconnecting with the " + NAME + " failed!");
        }
    }

    public String getName() {
        return NAME;
    }

}
