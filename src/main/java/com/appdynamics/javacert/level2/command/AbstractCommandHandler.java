package com.appdynamics.javacert.level2.command;

import com.appdynamics.javacert.level2.CommandHandler;

import java.io.Serializable;
import java.util.List;

/**
 * Created by trader on 2/9/17.
 */
public abstract class AbstractCommandHandler implements CommandHandler {

    public void handleCommand(List<String> args) {
        // Faked
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {

        }
    }
}
