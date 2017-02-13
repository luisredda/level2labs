package com.appdynamics.javacert.level2;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by trader on 2/8/17.
 */
public class CommandPayload extends HashMap<String, ArrayList<String>> implements Serializable {

    private static final int INITIAL_SIZE = 100;
    private static final int MIN_TO_ENSURE = 10;

    private static final int COMMAND = 1;
    private static final int UNKNOWN = 1000;

    public static final SortedMap<String, ArrayList<String>> TEST_DATA;

    static {
        SortedMap<String, ArrayList<String>> someData = new TreeMap<String, ArrayList<String>>();

        ArrayList<String> args = new ArrayList<String>();
        args.add("fred");
        args.add("smith");
        args.add("accounting");
        someData.put("addUser", args);

        args = new ArrayList<String>();
        args.add("100");
        someData.put("deleteUser", args);

        args = new ArrayList<String>();
        args.add("legal");
        someData.put("addDept", args);

        args = new ArrayList<String>();
        args.add("marketing");
        someData.put("deleteDept", args);

        args = new ArrayList<String>();
        args.add("Cisco");
        args.add("San Jose");
        args.add("California");
        args.add("United States");
        someData.put("addClient", args);

        args = new ArrayList<String>();
        args.add("NewRelic");
        someData.put("deleteClient", args);

        args = new ArrayList<String>();
        args.add("joe");
        args.add("johnson");
        args.add("e-10");
        args.add("accounting");
        someData.put("addEmployee", args);

        args = new ArrayList<String>();
        args.add("1243");
        args.add("e-8");
        someData.put("updateEmployeeLevel", args);

        TEST_DATA = Collections.unmodifiableSortedMap(someData);
    }

    public CommandPayload() {}

    public CommandPayload(byte[] sourceArray) {

        try {

            ByteBuffer buf = ByteBuffer.wrap(sourceArray);
            int len;
            byte[] stringBytes;
            while (buf.position() < buf.limit()) {
                int type = buf.getInt();
                switch (type) {
                    case COMMAND:
                        len = buf.getInt();
                        stringBytes = new byte[len];
                        buf.get(stringBytes);
                        String commandString = new String(stringBytes);
                        int argCount = buf.getInt();
                        ArrayList<String> args = new ArrayList<String>(argCount);
                        for (int i = 0; i < argCount; ++i) {
                            len = buf.getInt();
                            stringBytes = new byte[len];
                            buf.get(stringBytes);
                            args.add(new String(stringBytes));
                        }

                        addCommand(commandString, args);
                        break;

                    case UNKNOWN:
                        // Just grab an arbitrary string
                        len = buf.getInt();
                        byte[] unknownBytes = new byte[len];
                        buf.get(unknownBytes);
                        unknownField(unknownBytes);
                        break;
                    default:
                        throw new IllegalStateException("Corrupt stream, unknown type field: " + type);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
            System.exit(1);

        }
    }

    public void addCommand(String command, ArrayList<String> context) {
        put(command, context);
    }

    public Collection<String> getCommandVerbs() {
        return keySet();
    }

    public ArrayList<String> getContextFor(String command) {
        return get(command);
    }

    /**
     * Notification for an unknown field read.  Nothing is done with the data at this point
     * @return
     */
    public void unknownField(byte[] field) {
    }

    public byte[] commandToBytes() {
        return commandToBytes(null);
    }

    public byte[] commandToBytes(byte[] unknownFields) {

        ByteBuffer buf = ByteBuffer.allocate(INITIAL_SIZE);

        Collection<String> verbs = getCommandVerbs();
        buf = ensureCapacity(buf);

        for (String verb : verbs) {
            buf = encodeSingleCommand(buf, verb, getContextFor(verb));
        }

        if (unknownFields != null) {
            buf = ensureCapacity(buf);
            buf.putInt(UNKNOWN);
            buf = ensureCapacity(buf);
            buf.putInt(unknownFields.length);
            buf = ensureCapacity(buf, unknownFields.length);
            buf.put(unknownFields);
        }

        // Return an exact-sized array
        byte[] bufArray = buf.array();
        int realSize = buf.position();

        if (realSize < bufArray.length) {
            byte[] newArray = new byte[realSize];
            System.arraycopy(bufArray, 0, newArray, 0, newArray.length);
            bufArray = newArray;
        }

        return bufArray;
    }

    private ByteBuffer encodeSingleCommand(ByteBuffer buf, String verb, List<String> context) {
        buf = ensureCapacity(buf);
        buf.putInt(COMMAND);
        byte[] verbBytes = verb.getBytes();
        buf = ensureCapacity(buf);
        buf.putInt(verbBytes.length);
        buf = ensureCapacity(buf, verbBytes.length);
        buf.put(verbBytes);
        buf = ensureCapacity(buf);
        int nArgs = context.size();
        buf.putInt(nArgs);

        for (String arg : context) {
            buf = ensureCapacity(buf);
            byte[] argBytes = arg.getBytes();
            buf = ensureCapacity(buf);
            buf.putInt(argBytes.length);
            buf = ensureCapacity(buf, argBytes.length);
            buf.put(argBytes);
        }

        return buf;
    }

    private ByteBuffer ensureCapacity(ByteBuffer orig) {
        return ensureCapacity(orig, MIN_TO_ENSURE);
    }

    private ByteBuffer ensureCapacity(ByteBuffer orig, int increment) {

        ByteBuffer result;

        if (orig == null) {
            result =  ByteBuffer.allocate(Math.max(INITIAL_SIZE, increment));
        } else {
            if (orig.position() + increment > orig.limit()) {
                byte[] bytes = new byte[orig.position()];
                int len = orig.position();
                orig.rewind();
                orig.get(bytes, 0, len);
                ByteBuffer newArray = ByteBuffer.allocate(orig.limit() * 2);
                newArray.put(bytes);
                result = newArray;

            } else {
                result = orig;
            }
        }

        return result;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder("[CommandPayload: ");
        Iterator<String> verbIt = getCommandVerbs().iterator();
        while (verbIt.hasNext()) {
            String verb = verbIt.next();
            sb.append(verb);
            sb.append("=");
            sb.append(getContextFor(verb));
            if (verbIt.hasNext()) {
                sb.append(",");
            }
        }

        sb.append("]");
        return sb.toString();
    }

    public int hashCode() {
        int result = 5;
        for (String commandKey : getCommandVerbs()) {
            result = 31 * result + commandKey.hashCode();
            for (String arg : getContextFor(commandKey)) {
                result = 31 * result + arg.hashCode();
            }
        }

        return result;
    }

    public boolean equals(Object another) {

        boolean result = false;

        try {
            CommandPayload anotherPayload = (CommandPayload) another;
            Collection<String> myVerbs = getCommandVerbs();
            Collection<String> otherVerbs = anotherPayload.getCommandVerbs();
            if (myVerbs.equals(otherVerbs)) {
                boolean argsEqual = true;
                for (String verb : myVerbs) {
                    ArrayList<String> myArgs = getContextFor(verb);
                    ArrayList<String> anothersArgs = anotherPayload.getContextFor(verb);
                    if (!myArgs.equals(anothersArgs)) {
                        argsEqual = false;
                        break;
                    }
                }

                if (argsEqual) {
                    result = true;
                }
            }
        } catch (Exception cce) {
        }

        return result;
    }
}
