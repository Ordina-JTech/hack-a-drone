package com.github.nberlijn.hackadrone.cli;

import com.github.nberlijn.hackadrone.CX10WD;
import com.github.nberlijn.hackadrone.Drone;

import java.util.Scanner;

final class CLI {

    private final Drone cx10WD = new CX10WD();

    void launch() {
        System.out.println("Welcome to the " + cx10WD.getName() + " Command Line Interface.");

        help();
    }

    private void options() {
        Scanner scanner = new Scanner(System.in);

        switch (scanner.next()) {
            case "help":
                help();
                break;
            case "connect":
                connect();
                break;
            case "disconnect":
                disconnect();
                break;
            case "exit":
                exit();
                break;
        }
    }

    private void help() {
        System.out.println("The following commands are available:");
        System.out.println("- help");
        System.out.println("- connect");
        System.out.println("- disconnect");
        System.out.println("- exit");

        options();
    }

    private void connect() {
        try {
            System.out.println("Trying to establish a new connection...");
            cx10WD.connect();
            System.out.println("Connection successfully established with " + cx10WD.getName());
        } catch (Exception e) {
            System.out.println("Connection failed!");
        }

        options();
    }

    private void disconnect() {
        try {
            cx10WD.disconnect();
            System.out.println("Disconnection successful");
        } catch (Exception e) {
            System.out.println("Disconnection failed!");
        }

        options();
    }

    private void exit() {
        System.exit(0);
    }

}
