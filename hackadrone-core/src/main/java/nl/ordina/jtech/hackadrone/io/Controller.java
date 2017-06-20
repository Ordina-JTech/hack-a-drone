package nl.ordina.jtech.hackadrone.io;

import nl.ordina.jtech.hackadrone.net.CommandConnection;

/**
 * Class representing the controller for a drone.
 *
 * @author Nils Berlijn
 * @author Nanne Huiges
 * @version 1.0
 * @since 1.0
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
