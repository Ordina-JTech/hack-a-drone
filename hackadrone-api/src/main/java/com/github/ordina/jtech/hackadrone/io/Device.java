package com.github.ordina.jtech.hackadrone.io;

public interface Device {

    void start();

    void stop();

    void setListener(CommandListener commandListener);

}
