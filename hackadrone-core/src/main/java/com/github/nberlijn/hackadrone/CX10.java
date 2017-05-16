package com.github.nberlijn.hackadrone;

import com.github.nberlijn.hackadrone.io.Device;
import com.github.nberlijn.hackadrone.net.CommandConnection;
import com.github.nberlijn.hackadrone.net.TransportConnection;
import com.github.nberlijn.hackadrone.threads.Controller;
import com.github.nberlijn.hackadrone.threads.Heartbeat;
import com.github.nberlijn.hackadrone.utils.ByteUtils;

import java.io.IOException;

public final class CX10 implements Drone {

    private static final String NAME = "CX-10WD-TX";
    private static final String HOST = "172.16.10.1";
    private static final int PORT = 8888;
    private static final int IO_PORT = 8895;
    private TransportConnection transportConnection;
    private Controller controller;
    private Heartbeat heartbeat;

    public void connect() throws DroneException {
        try {
            if (transportConnection == null) {
                transportConnection = new TransportConnection(HOST, PORT);
            }

            transportConnection.connect();
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

    public void sendMessages() throws DroneException {
        try {
            transportConnection.sendMessage(ByteUtils.loadMessageFromFile("message1.bin"), 106);
            transportConnection.sendMessage(ByteUtils.loadMessageFromFile("message2.bin"), 106);
            transportConnection.sendMessage(ByteUtils.loadMessageFromFile("message3.bin"), 170);
            transportConnection.sendMessage(ByteUtils.loadMessageFromFile("message4.bin"), 106);
            transportConnection.sendMessage(ByteUtils.loadMessageFromFile("message5.bin"), 106);
        } catch (IOException e) {
            throw new DroneException("Sending messages to the " + NAME + " failed!");
        }
    }

    public void startHeartbeat() throws DroneException {
        if (heartbeat == null) {
            heartbeat = new Heartbeat(HOST, PORT);
            heartbeat.start();
        } else {
            throw new DroneException("Starting the heartbeat of the " + NAME + " failed!");
        }
    }

    public void stopHeartbeat() throws DroneException {
        if (heartbeat != null) {
            heartbeat.interrupt();
            heartbeat = null;
        } else {
            throw new DroneException("Stopping the heartbeat of the " + NAME + " failed!");
        }
    }

    public void startControls(Device device) throws DroneException {
        try {
            if (controller == null) {
                controller = new Controller(device, new CommandConnection(HOST, IO_PORT));
            }

            controller.start();
        } catch (IOException | IllegalThreadStateException e) {
            throw new DroneException("Starting the controls of the " + NAME + " failed!");
        }
    }

    public void stopControls() throws DroneException {
        if (controller != null) {
            controller.interrupt();
            controller = null;
        } else {
            throw new DroneException("Stopping the controls of the " + NAME + " failed!");
        }
    }

    public String getName() {
        return NAME;
    }

}
