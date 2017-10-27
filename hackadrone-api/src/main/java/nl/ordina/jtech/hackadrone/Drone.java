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

import nl.ordina.jtech.hackadrone.io.Device;
import nl.ordina.jtech.hackadrone.net.Connection;

/**
 * Interface representing a drone.
 */
public interface Drone extends Connection {

    /**
     * Connects.
     *
     * @throws DroneException if the connection failed
     */
    @Override
    void connect() throws DroneException;

    /**
     * Disconnects.
     *
     * @throws DroneException if the disconnection failed
     */
    @Override
    void disconnect() throws DroneException;

    /**
     * Sends messages.
     *
     * @throws DroneException if sending the messages failed
     */
    void sendMessages() throws DroneException;

    /**
     * Starts the heartbeat.
     *
     * @throws DroneException if starting the heartbeat failed
     */
    void startHeartbeat() throws DroneException;

    /**
     * Stops the heartbeat.
     *
     * @throws DroneException if stopping the heartbeat failed
     */
    void stopHeartbeat() throws DroneException;

    /**
     * Starts the controls.
     *
     * @param device the device to use the controls from
     * @throws DroneException if starting the controls failed
     */
    void startControls(Device device) throws DroneException;

    /**
     * Stops the controls.
     *
     * @throws DroneException if stopping the controls failed
     */
    void stopControls() throws DroneException;

    /**
     * Starts the camera.
     *
     * @throws DroneException if starting the camera failed
     */
    void startCamera() throws DroneException;

    /**
     * Stops the camera.
     *
     * @throws DroneException if stopping the camera failed
     */
    void stopCamera() throws DroneException;

    /**
     * Starts the recorder.
     *
     * @throws DroneException if starting the recorder failed
     */
    void startRecorder() throws DroneException;

    /**
     * Stops the recorder.
     *
     * @throws DroneException if stopping the recorder failed
     */
    void stopRecorder() throws DroneException;

    /**
     * Starts the AutoPilot.
     *
     * @throws DroneException if starting the AutoPilot failed
     */
    void startAutoPilot() throws DroneException;

    /**
     * Stops the AutoPilot.
     *
     * @throws DroneException if stopping the AutoPilot failed
     */
    void stopAutoPilot() throws DroneException;

    /**
     * Starts DeepLearning.
     *
     * @throws DroneException if starting DeepLearning has failed
     */
    void startDeepLearning() throws DroneException;

    /**
     * Stops DeepLearning.
     *
     * @throws DroneException if stopping the DeepLearning has failed
     */
    void stopDeepLearning() throws DroneException;

    /**
     * Gets the name of the drone.
     *
     * @return the name of the drone
     */
    String getName();

}
