package com.github.nberlijn.hackadrone.cli;

public final class App {

    public static void main(String[] args) {
        launch();
    }

    private static void launch() {
        CLI cli = new CLI();
        cli.launch();
    }

}
