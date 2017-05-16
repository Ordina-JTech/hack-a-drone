package com.github.nberlijn.hackadrone.threads;

import com.github.nberlijn.hackadrone.io.CommandListener;
import com.github.nberlijn.hackadrone.io.Device;
import com.github.nberlijn.hackadrone.models.Command;
import com.github.nberlijn.hackadrone.net.CommandConnection;

import java.io.IOException;

public final class Controller extends Thread implements CommandListener {

    private final Device device;
    private final CommandConnection commandConnection;
    private Command command = new Command();

    public Controller(Device device, CommandConnection commandConnection) {
        this.device = device;
        this.commandConnection = commandConnection;
    }

    @Override
    public void interrupt() {
        device.setListener(null);
        device.stop();
        super.interrupt();
    }

    @Override
    public void run() {
        device.setListener(this);
        device.start();

        while (!isInterrupted()) {
            try {
                commandConnection.sendCommand(command);
                Thread.sleep(5000);
            } catch (IOException | InterruptedException ignored) {

            }
        }
    }

    @Override
    public void onCommandReceived(Command command) {
        if (command == null) {
            this.command = new Command();
        } else {
            this.command = command;
        }
    }

}
