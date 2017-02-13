package com.appdynamics.javacert.level2;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by trader on 2/7/17.
 */
public abstract class ServerTransport {

    private MessageListener listener;
    private ListenerThread thread;
    ExecutorService pool;


    public ServerTransport() {
        listener = null;
        pool = Executors.newCachedThreadPool();
    }

    public synchronized void registerListener(MessageListener listener) {
        if (this.listener == null) {
            this.listener = listener;
        } else {
            throw new IllegalStateException("Listener not null!");
        }
    }

    protected abstract List<CommandPayload> nextMessage();

    protected synchronized MessageListener getListener() {
        return listener;
    }

    protected void postConstructorInit() {
        thread = new ListenerThread();
        thread.start();
    }

    public void join() throws InterruptedException {
        thread.join();
    }

    protected ExecutorService getPool() {
        return pool;
    }

    private class ListenerThread extends Thread {

        ListenerThread() {
            super();
        }

        public void run() {
            while (true) {
                List<CommandPayload> msg = nextMessage();
                for (CommandPayload payload : msg) {
                    pool.submit(new Notifier(payload));
                }
            }
        }
    }

    private class Notifier implements Runnable {

        private CommandPayload payload;

        Notifier(CommandPayload payload) {
            this.payload = payload;
        }

        public void run() {
            MessageListener theListener = getListener();
            if (theListener != null) {
                theListener.messageReceived(payload);
            }
        }
    }
}
