package com.appdynamics.javacert.level2;


import com.appdynamics.javacert.level2.transport.binary.MalleableProtocolClientTransport;
import com.appdynamics.javacert.level2.transport.binary.MalleableProtocolServerTransport;
import com.appdynamics.javacert.level2.transport.taggable.TaggablePayloadSocketClientTransport;
import com.appdynamics.javacert.level2.transport.taggable.TaggablePayloadSocketServerTransport;

/**
 * Created by trader on 2/7/17.
 */
public class TransportFactory {

    public static ClientTransport newClientTransport(String key) {
        if (key.equals("proto")) {
            return new MalleableProtocolClientTransport();
        } else {
            return new TaggablePayloadSocketClientTransport();
        }
    }

    public static ServerTransport newServerTransport(String key) {
        if (key.equals("proto")) {
            return new MalleableProtocolServerTransport();
        } else {
            return new TaggablePayloadSocketServerTransport();
        }
    }
}
