package com.appdynamics.javacert.level2.transport.taggable;

import com.appdynamics.javacert.level2.CommandPayload;
import com.appdynamics.javacert.level2.transport.SocketConstants;
import com.appdynamics.javacert.level2.transport.socket.SocketClientTransport;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by trader on 2/7/17.
 */
public class TaggablePayloadSocketClientTransport extends SocketClientTransport {

    protected void doSendMessage(CommandPayload message, OutputStream stream) {
        try {
            Map<String, Serializable> thePayload = new HashMap<String, Serializable>();
            thePayload.put(SocketConstants.PAYLOAD_KEY, message);
            ObjectOutputStream oos = new ObjectOutputStream(stream);
            oos.writeObject(thePayload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
