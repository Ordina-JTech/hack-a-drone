package nl.ordina.jtech.hackadrone;

import nl.ordina.jtech.hackadrone.io.*;
import nl.ordina.jtech.hackadrone.net.CommandConnection;
import nl.ordina.jtech.hackadrone.net.TransportConnection;
import nl.ordina.jtech.hackadrone.utils.ByteUtils;

import java.io.IOException;

/**
 * Class representing a CX10 drone.
 *
 * @author Nils Berlijn
 * @version 1.0
 * @since 1.0
 */
public final class CX10 implements Drone {

    /**
     * The name of the drone.
     */
    private static final String NAME = "Cheerson CX-10WD-TX Mini FPV Drone";

    /**
     * The host of the drone.
     */
    private static final String DRONE_HOST = "172.16.10.1";

    /**
     * The port of the drone.
     */
    private static final int DRONE_PORT = 8888;

    /**
     * The host of the IO.
     */
    private static final String IO_HOST = "172.16.10.1";

    /**
     * The port of the IO.
     */
    private static final int IO_PORT = 8895;

    /**
     * The host of the camera.
     */
    private static final String CAMERA_HOST = "127.0.0.1";

    /**
     * The port of the camera.
     */
    private static final int CAMERA_PORT = 8889;

    /**
     * The host of the recorder.
     */
    private static final String RECORDER_HOST = "127.0.0.1";

    /**
     * The port of the recorder.
     */
    private static final int RECORDER_PORT = 8890;

    /**
     * The transport connection with the drone.
     */
    private TransportConnection transportConnection;

    /**
     * The controller.
     */
    private Controller controller;

    /**
     * The heartbeat.
     */
    private Heartbeat heartbeat;

    /**
     * The camera handler.
     */
    private Handler camera;

    /**
     * The recorder handler.
     */
    private Handler recorder;

    /**
     * The AI controller.
     */
    private Controller ai;

    /**
     * Connects.
     *
     * @throws DroneException if the connection failed
     */
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

    /**
     * Disconnects.
     *
     * @throws DroneException if the disconnection failed
     */
    @Override
    public void disconnect() throws DroneException {
        try {
            transportConnection.disconnect();
        } catch (IOException | NullPointerException e) {
            throw new DroneException("Disconnecting with the " + NAME + " failed!");
        }
    }

    /**
     * Sends messages.
     *
     * @throws DroneException if sending the messages failed
     */
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

    /**
     * Starts the heartbeat.
     *
     * @throws DroneException if starting the heartbeat failed
     */
    @Override
    public void startHeartbeat() throws DroneException {
        if (heartbeat == null) {
            heartbeat = new Heartbeat(DRONE_HOST, DRONE_PORT);
            heartbeat.start();
        } else {
            throw new DroneException("Starting the heartbeat of the " + NAME + " failed!");
        }
    }

    /**
     * Stops the heartbeat.
     *
     * @throws DroneException if stopping the heartbeat failed
     */
    @Override
    public void stopHeartbeat() throws DroneException {
        if (heartbeat != null) {
            heartbeat.interrupt();
            heartbeat = null;
        } else {
            throw new DroneException("Stopping the heartbeat of the " + NAME + " failed!");
        }
    }

    /**
     * Starts the controls.
     *
     * @param device the device to use the controls from
     * @throws DroneException if starting the controls failed
     */
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

    /**
     * Stops the controls.
     *
     * @throws DroneException if stopping the controls failed
     */
    @Override
    public void stopControls() throws DroneException {
        if (controller != null) {
            controller.interrupt();
            controller = null;
        } else {
            throw new DroneException("Stopping the controls of the " + NAME + " failed!");
        }
    }

    /**
     * Starts the camera.
     *
     * @throws DroneException if starting the camera failed
     */
    @Override
    public void startCamera() throws DroneException {
        if (camera == null) {
            camera = new Camera(DRONE_HOST, DRONE_PORT, CAMERA_HOST, CAMERA_PORT);
            camera.start();
        } else {
            throw new DroneException("Starting the camera of the " + NAME + " failed!");
        }
    }

    /**
     * Stops the camera.
     *
     * @throws DroneException if stopping the camera failed
     */
    @Override
    public void stopCamera() throws DroneException {
        if (camera != null) {
            camera.stop();
            camera = null;
        } else {
            throw new DroneException("Stopping the camera of the " + NAME + " failed!");
        }
    }

    /**
     * Starts the recorder.
     *
     * @throws DroneException if starting the recorder failed
     */
    @Override
    public void startRecorder() throws DroneException {
        if (recorder == null) {
            recorder = new Recorder(DRONE_HOST, DRONE_PORT, RECORDER_HOST, RECORDER_PORT);
            recorder.start();
        } else {
            throw new DroneException("Starting the recorder of the " + NAME + " failed!");
        }
    }

    /**
     * Stops the recorder.
     *
     * @throws DroneException if stopping the recorder failed
     */
    @Override
    public void stopRecorder() throws DroneException {
        if (recorder != null) {
            recorder.stop();
            recorder = null;
        } else {
            throw new DroneException("Stopping the recorder of the " + NAME + " failed!");
        }
    }

    /**
     * Starts the AI.
     *
     * @throws DroneException if starting the AI failed
     */
    @Override
    public void startAi() throws DroneException {
        try {
            if (ai == null) {
                ai = new Controller(new AI(), new CommandConnection(IO_HOST, IO_PORT));
            }

            ai.start();
        } catch (IOException | IllegalThreadStateException e) {
            throw new DroneException("Starting the AI of the " + NAME + " failed!");
        }
    }

    /**
     * Stops the AI.
     *
     * @throws DroneException if stopping the AI failed
     */
    @Override
    public void stopAi() throws DroneException {
        if (ai != null) {
            ai.interrupt();
            ai = null;
        } else {
            throw new DroneException("Stopping AI of the " + NAME + " failed!");
        }
    }

    /**
     * Gets the name of the drone.
     *
     * @return the name of the drone
     */
    @Override
    public String getName() {
        return NAME;
    }

}
