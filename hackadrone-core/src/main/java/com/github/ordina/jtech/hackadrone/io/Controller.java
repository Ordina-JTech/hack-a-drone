package com.github.ordina.jtech.hackadrone.io;

import com.github.ordina.jtech.hackadrone.models.Command;
import com.github.ordina.jtech.hackadrone.net.CommandConnection;

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
                Thread.sleep(50);
            } catch (IOException e) {
                System.err.println("Unable to send command");;
            } catch (InterruptedException e) {
                System.err.println("Command interrupted");
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
