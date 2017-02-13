package com.appdynamics.javacert.level2;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by trader on 2/10/17.
 */
public class CommandPayloadTest {

    @Test
    public void testDegeneratePayloads() throws Exception {
        CommandPayload p1 = new CommandPayload();
        CommandPayload p2 = new CommandPayload();

        assertTrue(p1.hashCode() == p2.hashCode());
        assertTrue(p1.equals(p2));
    }

    @Test
    public void testSimpleEquality() throws Exception {
        String firstCommand = CommandPayload.TEST_DATA.firstKey();
        ArrayList<String> context = CommandPayload.TEST_DATA.get(firstCommand);
        CommandPayload payload1 = new CommandPayload();
        payload1.addCommand(firstCommand, context);
        CommandPayload payload2 = new CommandPayload();
        payload2.addCommand(firstCommand, context);

        assertTrue(payload1.hashCode() == payload2.hashCode());
        assertTrue(payload1.equals(payload2));
    }

    @Test
    public void testCompoundEquality() throws Exception {

        CommandPayload p1 = new CommandPayload();
        CommandPayload p2 = new CommandPayload();

        for (String command : CommandPayload.TEST_DATA.keySet()) {
            ArrayList<String> context = CommandPayload.TEST_DATA.get(command);
            p1.addCommand(command, context);
            p2.addCommand(command, context);
        }

        assertTrue(p1.hashCode() == p1.hashCode());
        assertTrue(p2.equals(p2));
    }

    @Test
    public void testEncodeDecodeSimplePayload() throws Exception {

        CommandPayload payload = new CommandPayload();

        String firstCommand = CommandPayload.TEST_DATA.firstKey();
        ArrayList<String> context = CommandPayload.TEST_DATA.get(firstCommand);
        payload.addCommand(firstCommand, context);

        byte[] payloadBytes = payload.commandToBytes();
        assertNotNull(payloadBytes);
        CommandPayload payloadFromBytes = new CommandPayload(payloadBytes);

        assertTrue(payload.hashCode() == payloadFromBytes.hashCode());
        assertTrue(payload.equals(payloadFromBytes));
    }

    @Test
    public void testEncodeDecodeSimplePayloadWithExtra() throws Exception {

        CommandPayload payload = new CommandPayload();
        String extra = "EXTRA_STRING";

        String firstCommand = CommandPayload.TEST_DATA.firstKey();
        ArrayList<String> context = CommandPayload.TEST_DATA.get(firstCommand);
        payload.addCommand(firstCommand, context);

        byte[] payloadBytes = payload.commandToBytes(extra.getBytes());
        assertNotNull(payloadBytes);
        PayloadWithExtra payloadFromBytes = new PayloadWithExtra(payloadBytes);

        assertTrue(payload.hashCode() == payloadFromBytes.hashCode());
        assertNotNull(payloadFromBytes.extraString);
        assertTrue(payloadFromBytes.extraString.equals(extra));
        assertTrue(payload.equals(payloadFromBytes));
    }

    @Test
    public void testEncodeDecodeCompoundPayload() throws Exception {

        CommandPayload payload = new CommandPayload();

        for (String command : CommandPayload.TEST_DATA.keySet()) {
            ArrayList<String> context = CommandPayload.TEST_DATA.get(command);
            payload.addCommand(command, context);
        }

        byte[] payloadBytes = payload.commandToBytes();
        assertNotNull(payloadBytes);
        CommandPayload payloadFromBytes = new CommandPayload(payloadBytes);

        assertTrue(payload.hashCode() == payloadFromBytes.hashCode());
        assertTrue(payload.equals(payloadFromBytes));
    }

    @Test
    public void testEncodeDecodeCompoundPayloadWithExtra() throws Exception {

        CommandPayload payload = new CommandPayload();
        String extra = "EXTRA_STRING";

        for (String command : CommandPayload.TEST_DATA.keySet()) {
            ArrayList<String> context = CommandPayload.TEST_DATA.get(command);
            payload.addCommand(command, context);
        }

        byte[] payloadBytes = payload.commandToBytes(extra.getBytes());
        assertNotNull(payloadBytes);
        PayloadWithExtra payloadFromBytes = new PayloadWithExtra(payloadBytes);

        assertTrue(payload.hashCode() == payloadFromBytes.hashCode());
        assertNotNull(payloadFromBytes.extraString);
        assertTrue(payloadFromBytes.extraString.equals(extra));
        assertTrue(payload.equals(payloadFromBytes));
    }

    private static class PayloadWithExtra extends CommandPayload {

        public PayloadWithExtra(byte[] rawData) {
            super(rawData);
        }

        String extraString;
        public void unknownField(byte[] unknown) {
            extraString = new String(unknown);
        }
    }
}
