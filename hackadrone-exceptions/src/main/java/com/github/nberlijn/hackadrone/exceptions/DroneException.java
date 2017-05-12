package com.github.nberlijn.hackadrone.exceptions;

import java.io.IOException;

public class DroneException extends IOException {

    public DroneException() {
        super();
    }

    public DroneException(String message) {
        super(message);
    }

}
