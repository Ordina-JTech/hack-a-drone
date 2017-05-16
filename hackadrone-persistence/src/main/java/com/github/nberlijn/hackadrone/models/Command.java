package com.github.nberlijn.hackadrone.models;

public class Command {

    private int pitch;
    private int yaw;
    private int roll;
    private int throttle;
    private boolean takeOff;
    private boolean land;

    public Command() {
        this(0, 0, 0, 0);
    }

    private Command(int pitch, int yaw, int roll, int throttle) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.throttle = throttle;
        this.roll = roll;
    }

    public int getPitch() {
        return pitch;
    }

    public void setPitch(int pitch) {
        if (pitch < -128) {
            pitch = -128;
        } else if (pitch > 127) {
            pitch = 127;
        }

        this.pitch = pitch;
    }

    public int getYaw() {
        return yaw;
    }

    public void setYaw(int yaw) {
        if (yaw < -128) {
            yaw = -128;
        } else if (yaw > 127) {
            yaw = 127;
        }

        this.yaw = yaw;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        if (roll < -128) {
            roll = 128;
        } else if (roll > 127) {
            roll = 127;
        }

        this.roll = roll;
    }

    public int getThrottle() {
        return throttle;
    }

    public void setThrottle(int throttle) {
        if (throttle < -128) {
            throttle = -128;
        } else if (throttle > 127) {
            throttle = 127;
        }

        this.throttle = throttle;
    }

    public boolean isTakeOff() {
        return takeOff;
    }

    public void setTakeOff(boolean takeOff) {
        this.takeOff = takeOff;
    }

    public boolean isLand() {
        return land;
    }

    public void setLand(boolean land) {
        this.land = land;
    }

    @Override
    public String toString() {
        return "Command{" +
                "pitch=" + pitch +
                ", yaw=" + yaw +
                ", roll=" + roll +
                ", throttle=" + throttle +
                ", takeOff=" + takeOff +
                ", land=" + land +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Command command = (Command) object;

        return pitch == command.pitch && yaw == command.yaw && roll == command.roll && throttle == command.throttle && takeOff == command.takeOff && land == command.land;
    }

    @Override
    public int hashCode() {
        int result = pitch;

        result = 31 * result + yaw;
        result = 31 * result + roll;
        result = 31 * result + throttle;
        result = 31 * result + (takeOff ? 1 : 0);
        result = 31 * result + (land ? 1 : 0);

        return result;
    }

}
