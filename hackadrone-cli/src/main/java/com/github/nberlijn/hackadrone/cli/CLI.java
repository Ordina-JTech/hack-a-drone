package com.github.nberlijn.hackadrone.cli;

import com.github.nberlijn.hackadrone.CX10WD;
import com.github.nberlijn.hackadrone.Drone;
import com.github.nberlijn.hackadrone.exceptions.DroneException;
import com.github.nberlijn.hackadrone.utils.ANSI;

import java.util.Scanner;

final class CLI {

    private final Drone cx10WD = new CX10WD();

    public static void main(String[] args) {
        CLI cli = new CLI();
        cli.launch();
    }

    private void launch() {
        System.out.println(ANSI.BLUE + "Welcome to the " + cx10WD.getName() + " Command Line Interface." + ANSI.RESET);

        options();
    }

    private void options() {
        Scanner scanner = new Scanner(System.in);

        switch (scanner.next()) {
            case "connect":
                connect();
                break;
            case "disconnect":
                disconnect();
                break;
            case "help":
                help();
                break;
            case "exit":
                exit();
                break;
        }
    }

    private void connect() {
        try {
            System.out.println(ANSI.YELLOW + "Trying to establish a new connection..." + ANSI.RESET);
            cx10WD.connect();
            System.out.println(ANSI.GREEN + "Connection successfully established" + ANSI.RESET);
        } catch (DroneException e) {
            System.out.println(ANSI.RED + "Connection failed!" + ANSI.RESET);
        }

        options();
    }

    private void disconnect() {
        try {
            System.out.println(ANSI.YELLOW + "Trying to disconnect..." + ANSI.RESET);
            cx10WD.disconnect();
            System.out.println(ANSI.GREEN + "Disconnection successful" + ANSI.RESET);
        } catch (DroneException e) {
            System.out.println(ANSI.RED + "Disconnection failed!" + ANSI.RESET);
        }

        options();
    }

    private void help() {
        System.out.println(ANSI.YELLOW + "The following commands are available:" + ANSI.RESET);
        System.out.println(ANSI.WHITE + "- connect" + ANSI.RESET);
        System.out.println(ANSI.WHITE + "- disconnect" + ANSI.RESET);
        System.out.println(ANSI.WHITE + "- help" + ANSI.RESET);
        System.out.println(ANSI.WHITE + "- exit" + ANSI.RESET);

        options();
    }

    private void exit() {
        System.out.println(ANSI.YELLOW + "Exiting..." + ANSI.RESET);
        System.exit(0);
    }

}
