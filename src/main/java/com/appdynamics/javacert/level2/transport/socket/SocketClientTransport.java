package com.appdynamics.javacert.level2.transport.socket;

import com.appdynamics.javacert.level2.ClientTransport;
import com.appdynamics.javacert.level2.CommandPayload;
import com.appdynamics.javacert.level2.transport.SocketConstants;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by trader on 2/7/17.
 */
public abstract class SocketClientTransport extends ClientTransport {

    public void sendMessage(CommandPayload message) {
        try {
            Socket sock = new Socket("localhost", SocketConstants.PORT);
            Map<String, Serializable> thePayload = new HashMap<String, Serializable>();
            thePayload.put(SocketConstants.PAYLOAD_KEY, message);
            OutputStream oos = sock.getOutputStream();
            doSendMessage(message, oos);
            oos.flush();
            oos.close();
            sock.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void doSendMessage(CommandPayload message, OutputStream stream);
}
