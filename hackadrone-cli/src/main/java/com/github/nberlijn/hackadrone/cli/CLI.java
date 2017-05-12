package com.github.nberlijn.hackadrone.cli;

import com.github.nberlijn.hackadrone.api.CX10;
import com.github.nberlijn.hackadrone.core.CX10WD;
import com.github.nberlijn.hackadrone.exceptions.CX10Exception;
import com.github.nberlijn.hackadrone.utils.ANSI;

import java.util.Scanner;

final class CLI {

    private final CX10 cx10 = new CX10WD();

    public static void main(String[] args) {
        CLI cli = new CLI();
        cli.launch();
    }

    private void launch() {
        System.out.println(ANSI.BLUE + "Welcome to the " + cx10.getName() + " Command Line Interface." + ANSI.RESET);
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
            case "exit":
                exit();
                break;
            case "help":
                help();
                break;
        }
    }

    private void connect() {
        try {
            System.out.println(ANSI.YELLOW + "Trying to establish a new connection..." + ANSI.RESET);
            cx10.connect();
            cx10.sendMessages();
            System.out.println(ANSI.GREEN + "Connection successfully established" + ANSI.RESET);
        } catch (CX10Exception e) {
            System.out.println(ANSI.RED + "Connection failed!" + ANSI.RESET);
        }

        options();
    }

    private void disconnect() {
        try {
            System.out.println(ANSI.YELLOW + "Trying to disconnect..." + ANSI.RESET);
            cx10.disconnect();
            System.out.println(ANSI.GREEN + "Disconnection successful" + ANSI.RESET);
        } catch (CX10Exception e) {
            System.out.println(ANSI.RED + "Disconnection failed!" + ANSI.RESET);
        }

        options();
    }

    private void exit() {
        System.out.println(ANSI.YELLOW + "Exiting..." + ANSI.RESET);
        System.exit(0);
    }

    private void help() {
        System.out.println(ANSI.YELLOW + "The following commands are available:" + ANSI.RESET);
        System.out.println(ANSI.WHITE + "- connect" + ANSI.RESET);
        System.out.println(ANSI.WHITE + "- disconnect" + ANSI.RESET);
        System.out.println(ANSI.WHITE + "- help" + ANSI.RESET);
        System.out.println(ANSI.WHITE + "- exit" + ANSI.RESET);
        options();
    }

}
