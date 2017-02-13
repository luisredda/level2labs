package com.appdynamics.javacert.level2;

import java.util.Iterator;

/**
 * Created by trader on 2/7/17.
 */
public class Sender {

    private String transportKey;

    private Sender(String transportKey) {
        this.transportKey = transportKey;
    }

    public static void main(String[] args) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ie) {

        }

        Sender instance = new Sender(args[0]);
        instance.runLoop();
    }

    private void runLoop() {

        while (true) {
            sendOneMessage();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {

            }
        }
    }

    private void sendOneMessage() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {

        }

        ClientTransport t = TransportFactory.newClientTransport(transportKey);

        CommandPayload message = getRandomMessage();
        t.sendMessage(message);
        for (String verb : message.getCommandVerbs()) {
            System.out.println("Sent command " + verb);
        }
    }

    private CommandPayload getRandomMessage() {
        int dataSize = CommandPayload.TEST_DATA.size();
        int randomCommandIndex = (int) (Math.random() * dataSize);
        int i = 0;
        Iterator<String> commandVerbs = CommandPayload.TEST_DATA.keySet().iterator();
        while (i++ < randomCommandIndex && commandVerbs.hasNext()) {
            commandVerbs.next();
        }

        String key = commandVerbs.next();
        CommandPayload result = new CommandPayload();
        result.addCommand(key, CommandPayload.TEST_DATA.get(key));
        return result;
    }
}
