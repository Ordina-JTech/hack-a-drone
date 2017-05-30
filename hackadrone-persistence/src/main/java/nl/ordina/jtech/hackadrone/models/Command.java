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

package nl.ordina.jtech.hackadrone.models;

/**
 * Class representing a command model.
 *
 * This command model contains commands that are used to control a drone with.
 * The commands pitch, yaw, roll, throttle, take off and land are available to use.
 *
 * @author Nils Berlijn
 * @version 1.0
 * @since 1.0
 */
public final class Command {

    /**
     * The pitch number is used to move the drone forward or backward.
     */
    private int pitch;

    /**
     * The yaw number is used to turn the drone to the left or the right.
     */
    private int yaw;

    /**
     * The roll number is used to move the drone to the left or the right.
     */
    private int roll;

    /**
     * The throttle number is used to move the drone up or down.
     */
    private int throttle;

    /**
     * The take off status is used to take off the drone from the ground.
     */
    private boolean takeOff;

    /**
     * The land status is used to let the drone land on the ground.
     */
    private boolean land;

    /**
     * A command constructor.
     */
    public Command() {
        this(0, 0, 0, 0);
    }

    /**
     * A command constructor.
     *
     * @param pitch the pitch number is used to move the drone forward or backward
     * @param yaw the yaw number is used to turn the drone to the left or the right
     * @param roll the roll number is used to move the drone to the left or the right
     * @param throttle the throttle number is used to move the drone up or down
     */
    private Command(int pitch, int yaw, int roll, int throttle) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.throttle = throttle;
        this.roll = roll;
    }

    /**
     * Gets the pitch number.
     *
     * The pitch number is used to move the drone forward or backward.
     * A plus value will let the drone move forwards and a minus value will let the drone move backwards.
     *
     * @return the pitch number
     */
    public int getPitch() {
        return pitch;
    }

    /**
     * Sets the pitch number.
     *
     * The pitch number is used to move the drone forward or backward.
     * A plus value will let the drone move forwards and a minus value will let the drone move backwards.
     *
     * @param pitch the pitch number
     */
    public void setPitch(int pitch) {
        if (pitch < -128) {
            pitch = -128;
        } else if (pitch > 127) {
            pitch = 127;
        }

        this.pitch = pitch;
    }

    /**
     * Gets the yaw number.
     *
     * The yaw number is used to turn the drone to the left or the right.
     * A plus value will let the drone turn to the right and a minus value will let the drone turn to the left.
     *
     * @return the yaw number
     */
    public int getYaw() {
        return yaw;
    }

    /**
     * Sets the yaw number.
     *
     * The yaw number is used to turn the drone to the left or the right.
     * A plus value will let the drone turn to the right and a minus value will let the drone turn to the left.
     *
     * @param yaw sets the yaw number
     */
    public void setYaw(int yaw) {
        if (yaw < -128) {
            yaw = -128;
        } else if (yaw > 127) {
            yaw = 127;
        }

        this.yaw = yaw;
    }

    /**
     * Gets the roll number.
     *
     * The roll number is used to move the drone to the left or the right.
     * A plus value will let the drone move to the right and a minus value will let the drone move to the left.
     *
     * @return the roll number
     */
    public int getRoll() {
        return roll;
    }

    /**
     * Sets the roll number.
     *
     * The roll number is used to move the drone to the left or the right.
     * A plus value will let the drone move to the right and a minus value will let the drone move to the left.
     *
     * @param roll the roll number
     */
    public void setRoll(int roll) {
        if (roll < -128) {
            roll = 128;
        } else if (roll > 127) {
            roll = 127;
        }

        this.roll = roll;
    }

    /**
     * Gets the throttle.
     *
     * The throttle number is used to move the drone up or down.
     * A plus value will let the drone move up and a minus value will let the drone move down.
     *
     * @return the throttle
     */
    public int getThrottle() {
        return throttle;
    }

    /**
     * Sets the throttle.
     *
     * The throttle number is used to move the drone up or down.
     * A plus value will let the drone move up and a minus value will let the drone move down.
     *
     * @param throttle the throttle
     */
    public void setThrottle(int throttle) {
        if (throttle < -128) {
            throttle = -128;
        } else if (throttle > 127) {
            throttle = 127;
        }

        this.throttle = throttle;
    }

    /**
     * Gets the take off status.
     *
     * The take off status is used to take off the drone from the ground.
     * A true value will let the drone take off from the ground and a false value will not let the drone take off from the ground.
     *
     * @return the take off status
     */
    public boolean isTakeOff() {
        return takeOff;
    }

    /**
     * Sets the take off status.
     *
     * The take off status is used to take off the drone from the ground.
     * A true value will let the drone take off from the ground and a false value will not let the drone take off from the ground.
     *
     * @param takeOff the take off status
     */
    public void setTakeOff(boolean takeOff) {
        this.takeOff = takeOff;
    }

    /**
     * Gets the land status.
     *
     * The land status is used to let the drone land on the ground.
     * A true value will let the drone land on the ground and a false value will not let the drone land on the ground.
     *
     * @return the land status
     */
    public boolean isLand() {
        return land;
    }

    /**
     * Sets the land status.
     *
     * The land status is used to let the drone land on the ground.
     * A true value will let the drone land on the ground and a false value will not let the drone land on the ground.
     *
     * @param land the land status
     */
    public void setLand(boolean land) {
        this.land = land;
    }

}
