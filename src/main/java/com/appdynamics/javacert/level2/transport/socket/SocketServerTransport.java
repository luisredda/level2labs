package com.appdynamics.javacert.level2.transport.socket;

import com.appdynamics.javacert.level2.CommandPayload;
import com.appdynamics.javacert.level2.ServerTransport;
import com.appdynamics.javacert.level2.transport.SocketConstants;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by trader on 2/8/17.
 */
public abstract class SocketServerTransport extends ServerTransport {

    private ServerSocket sock;
    private LinkedList<CommandPayload> pendingPayloads;

    public SocketServerTransport() {

        super();

        pendingPayloads = new LinkedList<CommandPayload>();

        try {
            sock = new ServerSocket(SocketConstants.PORT);
            getPool().submit(new SocketListener());
            postConstructorInit();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    protected List<CommandPayload> nextMessage() {

        List<CommandPayload> result = new ArrayList<CommandPayload>();

        synchronized (pendingPayloads) {
            while (pendingPayloads.size() == 0) {
                try {
                    pendingPayloads.wait();
                } catch (InterruptedException ie) {
                    System.exit(1);
                }
            }

            result.addAll(pendingPayloads);
            pendingPayloads.clear();
            pendingPayloads.notifyAll();
        }

        return result;
    }

    protected abstract CommandPayload readPayload(InputStream stream);

    private class SocketListener implements Runnable {

        public void run() {

            while (true) {

                try {
                    Socket clientSocket = sock.accept();
                    getPool().submit(new MessageProcessor(clientSocket));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
    }

    private class MessageProcessor implements Runnable {

        private Socket clientSocket;

        MessageProcessor(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try {

                CommandPayload payload = readPayload(clientSocket.getInputStream());
                synchronized (pendingPayloads) {
                    pendingPayloads.add(payload);
                    pendingPayloads.notifyAll();
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
