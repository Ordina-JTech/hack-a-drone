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

package nl.ordina.jtech.hackadrone.io;

import nl.ordina.jtech.hackadrone.net.CommandConnection;

/**
 * Class representing the controller for a drone.
 */
public abstract class Controller extends Thread implements CommandListener {

    /**
     * The device to control the drone with.
     */
    protected final Device device;

    /**
     * The command connection with the drone.
     */
    protected final CommandConnection commandConnection;

    /**
     * The delay between commands in ms.
     */
    protected final int delay = 50;

    /**
     * A controller constructor.
     *
     * @param device the device to control the drone with
     * @param commandConnection the command connection with the drone
     */
    public Controller(Device device, CommandConnection commandConnection) {
        this.device = device;
        this.commandConnection = commandConnection;
    }

    /**
     * Interrupts the controller.
     */
    @Override
    public void interrupt() {
        device.setListener(null);
        device.stop();

        super.interrupt();
    }

    /**
     * Gets the delay between commands in ms.
     *
     * @return the delay between commands in ms
     */
    @Override
    public int getDelay() {
        return this.delay;
    }

}
