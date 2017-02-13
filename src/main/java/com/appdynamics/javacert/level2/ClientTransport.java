package com.appdynamics.javacert.level2;

import java.io.Serializable;
import java.util.List;

/**
 * Created by trader on 2/7/17.
 */
public abstract class ClientTransport {

    public ClientTransport() {
    }

    public abstract void sendMessage(CommandPayload message);
}
