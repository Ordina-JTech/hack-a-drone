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

package nl.ordina.jtech.hackadrone.controllers;

import nl.ordina.jtech.hackadrone.io.Controller;
import nl.ordina.jtech.hackadrone.io.Device;
import nl.ordina.jtech.hackadrone.models.Command;
import nl.ordina.jtech.hackadrone.net.CommandConnection;

import java.io.IOException;

/**
 * Class representing the controls controller for a drone.
 *
 * @author Nils Berlijn
 * @version 1.0
 * @since 1.0
 */
public final class ControlsController extends Controller {

    /**
     * The command.
     */
    private Command command = new Command();

    /**
     * A controls controller constructor.
     *
     * @param device the device to control the drone with
     * @param commandConnection the command connection with the drone
     */
    public ControlsController(Device device, CommandConnection commandConnection) {
        super(device, commandConnection);
    }

    /**
     * Starts running the controls controller.
     */
    @Override
    public void run() {
        device.setListener(this);
        device.start();

        while (!isInterrupted()) {
            try {
                commandConnection.sendCommand(command);

                Thread.sleep(this.delay);
            } catch (IOException e) {
                System.err.println("Unable to send command");
            } catch (InterruptedException e) {
                System.err.println("Command interrupted");
            }
        }
    }

    /**
     * Handles the received command.
     *
     * @param command the command to handle
     */
    @Override
    public void onCommandReceived(Command command) {
        if (command == null) {
            this.command = new Command();
        } else {
            this.command = command;
        }
    }

}
