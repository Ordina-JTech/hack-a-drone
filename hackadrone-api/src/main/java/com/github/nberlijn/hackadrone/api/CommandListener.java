package com.github.nberlijn.hackadrone.api;

import com.github.nberlijn.hackadrone.persistence.models.Command;

public interface CommandListener {

    void onCommandReceived(Command command);

}
