/*
 * Copyright (C) 2017 Ordina
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.ordina.jtech.hackadrone;

import java.io.IOException;

import nl.ordina.jtech.hackadrone.controllers.ApController;
import nl.ordina.jtech.hackadrone.controllers.ControlsController;
import nl.ordina.jtech.hackadrone.io.AutoPilot;
import nl.ordina.jtech.hackadrone.io.Camera;
import nl.ordina.jtech.hackadrone.io.DeepLearning;
import nl.ordina.jtech.hackadrone.io.Device;
import nl.ordina.jtech.hackadrone.io.Heartbeat;
import nl.ordina.jtech.hackadrone.io.Recorder;
import nl.ordina.jtech.hackadrone.io.VideoFrame;
import nl.ordina.jtech.hackadrone.net.CommandConnection;
import nl.ordina.jtech.hackadrone.net.TransportConnection;
import nl.ordina.jtech.hackadrone.utils.ByteUtils;

/**
 * Class representing a CX10 drone.
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
     * The controls controller.
     */
    private ControlsController controls;

    /**
     * The heartbeat.
     */
    private Heartbeat heartbeat;

    /**
     * The camera handler.
     */
    private Camera camera;

    /**
     * DeepLearning handler.
     */
    private DeepLearning deepLearning;

    /**
     * The recorder handler.
     */
    private Recorder recorder;

    /**
     * The AutoPilot controller.
     */
    private ApController ap;

    /**
     * Video frame
     */
    private VideoFrame videoFrame;

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
            if (controls == null) {
                controls = new ControlsController(device, new CommandConnection(IO_HOST, IO_PORT));
            }

            controls.start();
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
        if (controls != null) {
            controls.interrupt();
            controls = null;
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
            camera = new Camera(DRONE_HOST, DRONE_PORT, CAMERA_HOST, CAMERA_PORT, createVideoFrame());
        }

        camera.start();
    }

    /**
     * Stops the camera.
     *
     * @throws DroneException if stopping the camera failed
     */
    @Override
    public void stopCamera() throws DroneException {
        camera.stop();
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
     * Starts the AutoPilot.
     *
     * @throws DroneException if starting the AutoPilot failed
     */
    @Override
    public void startAutoPilot() throws DroneException {
        try {
            if (ap == null) {
                ap = new ApController(new AutoPilot(), new CommandConnection(IO_HOST, IO_PORT));
            }

            ap.start();
        } catch (IOException | IllegalThreadStateException e) {
            throw new DroneException("Starting the AutoPilot of the " + NAME + " failed!");
        }
    }

    /**
     * Stops the AutoPilot.
     *
     * @throws DroneException if stopping the AutoPilot failed
     */
    @Override
    public void stopAutoPilot() throws DroneException {
        if (ap != null) {
            ap.interrupt();
            ap = null;
        } else {
            throw new DroneException("Stopping AutoPilot of the " + NAME + " failed!");
        }
    }

    @Override
    public void startDeepLearning() throws DroneException {
        if (deepLearning == null) {
            if (camera == null) {
                camera = new Camera(DRONE_HOST, DRONE_PORT, CAMERA_HOST, CAMERA_PORT, createVideoFrame());
                camera.start();
            }
            deepLearning = new DeepLearning();
            camera.setDeepLearning(deepLearning);
        } else {
            throw new DroneException("Starting the deepLearning of the " + NAME + " failed!");
        }
    }

    @Override
    public void stopDeepLearning() throws DroneException {
        if (deepLearning != null) {
            if (camera.getDeepLearning() != null) {
                camera.setDeepLearning(null);
            }
            deepLearning = null;
        } else {
            throw new DroneException("Stopping deepLearning of the " + NAME + " failed!");
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

    /**
     * Create a shared video frame.
     *
     * @return
     */
    private VideoFrame createVideoFrame() {
        if (videoFrame == null) {
            return (this.videoFrame = new VideoFrame());
        } else {
            return this.videoFrame;
        }
    }

}
