package nl.ordina.jtech.hackadrone.io;

import nl.ordina.jtech.hackadrone.models.Command;

public final class AI implements Device {

    private CommandListener commandListener;
    private Command command = new Command();

    @Override
    public void start() {
        command.setTakeOff(true);
        commandListener.onCommandReceived(command);

        for (int i = 0; i < 25; i++) {
            command.setThrottle(-127);
            commandListener.onCommandReceived(command);
        }
    }

    @Override
    public void stop() {
        commandListener = null;
        command = new Command();
    }

    @Override
    public void setListener(CommandListener commandListener) {
        this.commandListener = commandListener;
    }

}
