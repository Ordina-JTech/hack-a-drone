package nl.ordina.jtech.hackadrone.io;

import nl.ordina.jtech.hackadrone.models.Command;
import nl.ordina.jtech.hackadrone.net.CommandConnection;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Class representing the controller for a drone.
 *
 * @author Nils Berlijn
 * @author Nanne Huiges
 * @version 1.0
 * @since 1.0
 */
public final class Controller extends Thread implements CommandListener {

    /**
     * The device to control the drone with.
     */
    private final Device device;

    /**
     * The command connection with the drone.
     */
    private final CommandConnection commandConnection;

    /**
     * The list with commands.
     */
    private LinkedList<Command> commandList = new LinkedList<>();

    /**
     * The delay between commands in ms.
     */
    private int delay = 50;

    /**
     * A controller constructor
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
     * Starts running the controller.
     */
    @Override
    public void run() {
        device.setListener(this);
        device.start();

        Command defaultCommand = new Command();

        while (!isInterrupted()) {
            try {
                if (commandList.isEmpty()) {
                    commandConnection.sendCommand(defaultCommand);
                } else {
                    commandConnection.sendCommand(commandList.remove());
                }

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
        if (command != null) {
            this.commandList.add(command);
        }
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
