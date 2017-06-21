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

import nl.ordina.jtech.hackadrone.controllers.AiController;
import nl.ordina.jtech.hackadrone.models.Command;

/**
 * Class representing the artificial intelligence (AI) for a drone.
 *
 * The AI is part of a device and can be inserted into an AI controller.
 * The AI uses a command listener to handle commands and uses the command model for the available commands.
 * The controller uses the command listener to handle the triggered commands.
 *
 * @see AiController for more information about the working flow of the AI controller
 * @see CommandListener for the interface that is used to handle commands
 * @see Command for a more detailed explanation about the available commands
 *
 * @author Nils Berlijn
 * @author Nanne Huiges
 * @version 1.0
 * @since 1.0
 */
public final class AI implements Device {

    /**
     * The command listener.
     */
    private CommandListener commandListener;

    /**
     * Starts the AI.
     *
     * TODO: Build an interesting and cool AI.
     *
     * A simple example is added to get started.
     * This example will simply first take off the drone from the ground.
     * Then the drone will turning around for a bit (about 180 degrees).
     * Finally the drone goes backwards for a bit and land back to the ground.
     * Between the commands it waits a bit doing 'nothing'.
     *
     * @see AI#pitch(int, int) for a more detailed explanation about using the pitch command
     * @see AI#yaw(int, int) for a more detailed explanation about using the yaw command
     * @see AI#roll(int, int) for a more detailed explanation about using the roll command
     * @see AI#throttle(int, int) for a more detailed explanation about using the throttle command
     * @see AI#takeOff() for a more detailed explanation about using the take off command
     * @see AI#land() for a more detailed explanation about using the land command
     * @see AI#chill(int) for a more detailed explanation about using the chill command
     */
    @Override
    public void start() {
        // Take off
        chill(250);
        takeOff();

        // Move forward
        chill(500);
        pitch(100, 1000);

        // Brake
        chill(250);
        pitch(-50, 250);

        // Turn around
        chill(1000);
        yaw(127, 2000);

        // Move backward
        chill(500);
        pitch(-100, 500);

        // Brake
        chill(250);
        pitch(-50, 250);

        // Land
        chill(250);
        land();
    }

    /**
     * Stops the AI.
     *
     * The command listener will be set to null.
     * Also the command will be reinitialised.
     */
    @Override
    public void stop() {
        commandListener = null;
    }

    /**
     * Sets the listener.
     *
     * @param commandListener the command listener to set
     */
    @Override
    public void setListener(CommandListener commandListener) {
        this.commandListener = commandListener;
    }

    /**
     * Sends enough commands to the command listener for the amount set of seconds.
     *
     * @param command the command to send
     * @param ms how many ms to send the command
     */
    private void runCommandFor(Command command, int ms) {
        int commandRepeat = ms / commandListener.getDelay();

        for (int i = 0; i < commandRepeat; i++) {
            commandListener.onCommandReceived(command);
        }
    }

    /**
     * The pitch number is used to move the drone forward or backward.
     * A plus value will let the drone move forwards and a minus value will let the drone move backwards.
     *
     * Send a positive value up to 127 to go forward.
     * Send a negative value down to -128 to go backwards.
     *
     * @param pitch the amount (speed) of the move
     * @param ms how long to keep moving
     */
    private void pitch(int pitch, int ms) {
        Command pitchCommand = new Command();
        pitchCommand.setPitch(pitch);
        runCommandFor(pitchCommand, ms);
    }

    /**
     * The yaw number is used to turn the drone to the left or the right.
     * A plus value will let the drone turn to the right and a minus value will let the drone turn to the left.
     *
     * Send a positive value up to 127 to turn right.
     * Send a negative value down to -128 to turn left.
     *
     * @param yaw the amount (speed) of the turn
     * @param ms how long to keep turning
     */
    private void yaw(int yaw, int ms) {
        Command yawCommand = new Command();
        yawCommand.setYaw(yaw);
        runCommandFor(yawCommand, ms);
    }

    /**
     * The roll number is used to move the drone to the left or the right.
     * A plus value will let the drone move to the right and a minus value will let the drone move to the left.
     *
     * Send a positive value up to 127 to go right.
     * Send a negative value down to -128 to go left.
     *
     * @param roll the amount (speed) of the move
     * @param ms how long to keep moving
     */
    private void roll(int roll, int ms) {
        Command rollCommand = new Command();
        rollCommand.setRoll(roll);
        runCommandFor(rollCommand, ms);
    }

    /**
     * The throttle number is used to move the drone up or down.
     * A plus value will let the drone move up and a minus value will let the drone move down.
     *
     * Send a positive value up to 127 to go up.
     * Send a negative value down to -128 to go down.
     *
     * @param throttle the amount (speed) of the move
     * @param ms how long to keep moving
     */
    private void throttle(int throttle, int ms) {
        Command throttleCommand = new Command();
        throttleCommand.setThrottle(throttle);
        runCommandFor(throttleCommand, ms);
    }

    /**
     * The take off is used to take off the drone from the ground.
     * A true value will let the drone take off from the ground and a false value will not let the drone take off from the ground.
     * It takes 5 ms to be sure to take off.
     */
    private void takeOff() {
        Command takeOffCommand = new Command();
        takeOffCommand.setTakeOff(true);
        runCommandFor(takeOffCommand, 500);
    }

    /**
     * The land is used to let the drone land on the ground.
     * A true value will let the drone land on the ground and a false value will not let the drone land on the ground.
     * It takes 5 ms to be sure to land.
     */
    private void land() {
        Command landCommand = new Command();
        landCommand.setLand(true);
        runCommandFor(landCommand, 10000);
    }

    /**
     * Do nothing for a while.
     * This is useful to let the drone level out.
     *
     * @param ms how long to do nothing
     */
    private void chill(int ms) {
        Command chillCommand = new Command();
        runCommandFor(chillCommand, ms);
    }

}
