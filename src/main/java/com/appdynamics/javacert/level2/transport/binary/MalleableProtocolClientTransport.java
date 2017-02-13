package com.appdynamics.javacert.level2.transport.binary;

import com.appdynamics.javacert.level2.ClientTransport;
import com.appdynamics.javacert.level2.CommandPayload;
import com.appdynamics.javacert.level2.transport.SocketConstants;

import java.io.BufferedOutputStream;
import java.net.Socket;

/**
 * Created by trader on 2/7/17.
 */
public class MalleableProtocolClientTransport extends ClientTransport {

    public void sendMessage(CommandPayload message) {
        try {
            Socket sock = new Socket("localhost", SocketConstants.PORT);
            BufferedOutputStream oos = new BufferedOutputStream(sock.getOutputStream());
            oos.write(message.commandToBytes());
            oos.flush();
            oos.close();
            sock.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
