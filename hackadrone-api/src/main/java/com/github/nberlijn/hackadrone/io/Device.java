package com.github.nberlijn.hackadrone.io;

public interface Device {

    void start();

    void stop();

    void setListener(CommandListener controlListener);

}
