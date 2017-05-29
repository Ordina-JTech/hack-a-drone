package nl.ordina.jtech.hackadrone.models;

/**
 * Class representing a command model.
 *
 * @author Nils Berlijn
 * @version 1.0
 * @since 1.0
 */
public final class Command {

    /**
     * The pitch number.
     */
    private int pitch;

    /**
     * The yaw number.
     */
    private int yaw;

    /**
     * The roll number.
     */
    private int roll;

    /**
     * The throttle number.
     */
    private int throttle;

    /**
     * The take off status.
     */
    private boolean takeOff;

    /**
     * The land status.
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
     * @param pitch the pitch number
     * @param yaw the yaw number
     * @param roll the roll number
     * @param throttle the throttle number
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
     * @return the pitch number
     */
    public int getPitch() {
        return pitch;
    }

    /**
     * Sets the pitch number.
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
     * @return the yaw number
     */
    public int getYaw() {
        return yaw;
    }

    /**
     * Sets the yaw number.
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
     * @return the roll number
     */
    public int getRoll() {
        return roll;
    }

    /**
     * Sets the roll number.
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
     * @return the throttle
     */
    public int getThrottle() {
        return throttle;
    }

    /**
     * Sets the throttle.
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
     * @return the take off status
     */
    public boolean isTakeOff() {
        return takeOff;
    }

    /**
     * Sets the take off status.
     *
     * @param takeOff the take off status
     */
    public void setTakeOff(boolean takeOff) {
        this.takeOff = takeOff;
    }

    /**
     * Gets the land status.
     *
     * @return the land status
     */
    public boolean isLand() {
        return land;
    }

    /**
     * Sets the land status.
     *
     * @param land the land status
     */
    public void setLand(boolean land) {
        this.land = land;
    }

}
