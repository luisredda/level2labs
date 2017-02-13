package com.appdynamics.javacert.level2;

/**
 * Created by trader on 2/7/17.
 */
public class Receiver implements MessageListener {

    private ServerTransport transport;

    private Receiver(String transportKey) {
        transport = TransportFactory.newServerTransport(transportKey);
        transport.registerListener(this);
    }

    public static void main(String[] args) {
        Receiver instance = new Receiver(args[0]);

        try {
            instance.transport.join();
        } catch (InterruptedException ie) {

        }
    }

    public void messageReceived(CommandPayload message) {

        for (String verb : message.getCommandVerbs()) {

            CommandHandler handler = CommandDispatcher.forCommand(verb);
            handler.handleCommand(message.getContextFor(verb));
            System.out.println("Got message command " + verb);
        }
    }
}
