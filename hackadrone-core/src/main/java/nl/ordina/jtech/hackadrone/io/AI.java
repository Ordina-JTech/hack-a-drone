package nl.ordina.jtech.hackadrone.io;

import nl.ordina.jtech.hackadrone.models.Command;

/**
 * Class representing the artificial intelligence (AI) for a drone.
 *
 * The AI is part of a device and can be inserted into a controller.
 * The AI uses a command listener to handle commands and uses the command model for the available commands.
 * The controller uses the command listener to handle the triggered commands.
 *
 * @see Controller for more information about the working flow of the controller
 * @see CommandListener for the interface that is used to handle commands
 * @see Command for a more detailed explanation about using the commands
 *
 * @author Nils Berlijn
 * @version 1.0
 * @since 1.0
 */
public final class AI implements Device {

    /**
     * The command listener.
     */
    private CommandListener commandListener;

    /**
     * The command.
     */
    private Command command = new Command();

    /**
     * Starts the AI.
     *
     * TODO: Build an interesting and cool AI.
     *
     * A simple example is added to get started.
     * This example will simply take off the drone from the ground and will directly land the drone back to the ground.
     *
     * @see Command for a more detailed explanation about using the commands
     */
    @Override
    public void start() {
        command.setTakeOff(true);
        commandListener.onCommandReceived(command);

        for (int i = 0; i < 25; i++) {
            command.setThrottle(-127);
            commandListener.onCommandReceived(command);
        }
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
        command = new Command();
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

}
