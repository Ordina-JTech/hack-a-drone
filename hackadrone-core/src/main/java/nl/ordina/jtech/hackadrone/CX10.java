package nl.ordina.jtech.hackadrone;

import nl.ordina.jtech.hackadrone.io.*;
import nl.ordina.jtech.hackadrone.net.CommandConnection;
import nl.ordina.jtech.hackadrone.net.TransportConnection;
import nl.ordina.jtech.hackadrone.utils.ByteUtils;

import java.io.IOException;

public final class CX10 implements Drone {

    private static final String NAME = "Cheerson CX-10WD-TX Mini FPV Drone";

    private static final String DRONE_HOST = "172.16.10.1";
    private static final int DRONE_PORT = 8888;

    private static final String IO_HOST = "172.16.10.1";
    private static final int IO_PORT = 8895;

    private static final String VIDEO_HOST = "127.0.0.1";
    private static final int VIDEO_PORT = 8889;

    private static final String RECORDER_HOST = "127.0.0.1";
    private static final int RECORDER_PORT = 8890;

    private TransportConnection transportConnection;
    private Controller controller;
    private Heartbeat heartbeat;
    private Handler camera;
    private Handler recorder;
    private Handler ai;

    @Override
    public void connect() throws DroneException {
        try {
            if (transportConnection == null) {
                transportConnection = new TransportConnection(DRONE_HOST, DRONE_PORT);
            }

            transportConnection.connect();
        } catch (IOException e) {
            throw new DroneException("Connection with the " + NAME + " failed!");
        }
    }

    @Override
    public void disconnect() throws DroneException {
        try {
            transportConnection.disconnect();
        } catch (IOException | NullPointerException e) {
            throw new DroneException("Disconnecting with the " + NAME + " failed!");
        }
    }

    @Override
    public void sendMessages() throws DroneException {
        try {
            transportConnection.sendMessage(ByteUtils.loadMessageFromFile("bin/message1.bin"), 106);
            transportConnection.sendMessage(ByteUtils.loadMessageFromFile("bin/message2.bin"), 106);
            transportConnection.sendMessage(ByteUtils.loadMessageFromFile("bin/message3.bin"), 170);
            transportConnection.sendMessage(ByteUtils.loadMessageFromFile("bin/message4.bin"), 106);
            transportConnection.sendMessage(ByteUtils.loadMessageFromFile("bin/message5.bin"), 106);
        } catch (IOException e) {
            throw new DroneException("Sending messages to the " + NAME + " failed!");
        }
    }

    @Override
    public void startHeartbeat() throws DroneException {
        if (heartbeat == null) {
            heartbeat = new Heartbeat(DRONE_HOST, DRONE_PORT);
            heartbeat.start();
        } else {
            throw new DroneException("Starting the heartbeat of the " + NAME + " failed!");
        }
    }

    @Override
    public void stopHeartbeat() throws DroneException {
        if (heartbeat != null) {
            heartbeat.interrupt();
            heartbeat = null;
        } else {
            throw new DroneException("Stopping the heartbeat of the " + NAME + " failed!");
        }
    }

    @Override
    public void startControls(Device device) throws DroneException {
        try {
            if (controller == null) {
                controller = new Controller(device, new CommandConnection(IO_HOST, IO_PORT));
            }

            controller.start();
        } catch (IOException | IllegalThreadStateException e) {
            throw new DroneException("Starting the controls of the " + NAME + " failed!");
        }
    }

    @Override
    public void stopControls() throws DroneException {
        if (controller != null) {
            controller.interrupt();
            controller = null;
        } else {
            throw new DroneException("Stopping the controls of the " + NAME + " failed!");
        }
    }

    @Override
    public void startCamera() throws DroneException {
        if (camera == null) {
            camera = new Camera(DRONE_HOST, DRONE_PORT, VIDEO_HOST, VIDEO_PORT);
            camera.start();
        } else {
            throw new DroneException("Starting the camera of the " + NAME + " failed!");
        }
    }

    @Override
    public void stopCamera() throws DroneException {
        if (camera != null) {
            camera.stop();
            camera = null;
        } else {
            throw new DroneException("Stopping the camera of the " + NAME + " failed!");
        }
    }

    @Override
    public void startRecorder() throws DroneException {
        if (recorder == null) {
            recorder = new Recorder(DRONE_HOST, DRONE_PORT, RECORDER_HOST, RECORDER_PORT);
            recorder.start();
        } else {
            throw new DroneException("Starting the recorder of the " + NAME + " failed!");
        }
    }

    @Override
    public void stopRecorder() throws DroneException {
        if (recorder != null) {
            recorder.stop();
            recorder = null;
        } else {
            throw new DroneException("Stopping the recorder of the " + NAME + " failed!");
        }
    }

    @Override
    public void startAi() throws DroneException {
        if (ai == null) {
            ai = new AI();
            ai.start();
        } else {
            throw new DroneException("Starting AI of the " + NAME + " failed!");
        }
    }

    @Override
    public void stopAi() throws DroneException {
        if (ai != null) {
            ai.stop();
            ai = null;
        } else {
            throw new DroneException("Stopping AI of the " + NAME + " failed!");
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

}
