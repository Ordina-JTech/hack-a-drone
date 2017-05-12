package com.github.nberlijn.hackadrone.api;

import com.github.nberlijn.hackadrone.persistence.models.Command;

public interface Control {

    void start();

    void stop();

    boolean isAvailable();

}
