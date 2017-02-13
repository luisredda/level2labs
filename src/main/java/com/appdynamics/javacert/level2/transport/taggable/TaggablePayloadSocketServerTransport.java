package com.appdynamics.javacert.level2.transport.taggable;

import com.appdynamics.javacert.level2.CommandPayload;
import com.appdynamics.javacert.level2.transport.SocketConstants;
import com.appdynamics.javacert.level2.transport.socket.SocketServerTransport;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by trader on 2/8/17.
 */
public class TaggablePayloadSocketServerTransport extends SocketServerTransport {

    public TaggablePayloadSocketServerTransport() {
        super();
    }

    protected CommandPayload readPayload(InputStream stream) {

        try {

            BufferedInputStream bis = new BufferedInputStream(stream);
            ObjectInputStream iStream = new ObjectInputStream(bis);
            if (bis.available() > 0) {
                Object in = iStream.readObject();
                Map<String, Serializable> wrappedPayload = (Map<String, Serializable>) in;
                return (CommandPayload) wrappedPayload.get(SocketConstants.PAYLOAD_KEY);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }
}
