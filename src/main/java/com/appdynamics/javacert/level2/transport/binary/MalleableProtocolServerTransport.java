package com.appdynamics.javacert.level2.transport.binary;

import com.appdynamics.javacert.level2.CommandPayload;
import com.appdynamics.javacert.level2.transport.socket.SocketServerTransport;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * Created by trader on 2/8/17.
 */
public class MalleableProtocolServerTransport extends SocketServerTransport {

    public MalleableProtocolServerTransport() {

        super();
    }

    protected CommandPayload readPayload(InputStream stream) {

        try {

            BufferedInputStream bis = new BufferedInputStream(stream);
            int bytesAvail = bis.available();
            byte[] toRead = new byte[bytesAvail];
            bis.read(toRead);
            return new CommandPayload(toRead);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }
}
