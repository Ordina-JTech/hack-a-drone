package com.github.nberlijn.hackadrone.threads;

import com.github.nberlijn.hackadrone.io.CommandListener;
import com.github.nberlijn.hackadrone.io.Device;
import com.github.nberlijn.hackadrone.models.Command;
import com.github.nberlijn.hackadrone.net.CommandConnection;

import java.io.IOException;

public final class Controller extends Thread implements CommandListener {

    private final Device device;
    private final CommandConnection commandConnection;
    private Command command;

    public Controller(Device device, CommandConnection commandConnection) {
        this.device = device;
        this.commandConnection = commandConnection;
        command = new Command();
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
                hold();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void hold() {
        try {
            sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onCommandReceived(Command command) {
        if (command == null) {
            this.command = new Command();
        } else {
            this.command = command;
        }
    }

}
