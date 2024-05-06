package it.polimi.ingsw.connections;

import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import it.polimi.ingsw.Client;
import it.polimi.ingsw.connections.messages.Message;
import it.polimi.ingsw.connections.messages.PingMessage;
import it.polimi.ingsw.connections.messages.client.ClientToServerMessage;
import it.polimi.ingsw.connections.messages.server.ServerToClientMessage;

public class OutputStreamRunnable implements StreamRunnable {
    private final ObjectOutputStream outputStream;
    private boolean isStopped;
    private Consumer<Exception> callback;
//    private IOException exception;

    final Object lock = new Object();
//    private TimerTask task = new TimerTask() {
//        public void run() {
//            synchronized (lock) {
//                lock.notifyAll();
//            }
//        }
//    };
//    Timer timer = new Timer();


    public OutputStreamRunnable(ObjectOutputStream oos, Consumer<Exception> callback) throws IOException {
        this.outputStream = oos;
        this.isStopped = false;
        this.callback = callback;
    }

    public OutputStreamRunnable(OutputStream os, Consumer<Exception> callback) throws IOException {
        this.outputStream = new ObjectOutputStream(os);
        this.isStopped = false;
        this.callback = callback;
    }


    public void run() {
        try {
            while (!this.isStopped) {
                synchronized (lock) {
                    lock.wait(3000);
                }
                ping();
            }
        } catch (IOException | InterruptedException e) {
            this.callback.accept(e);
            return;
        }
    }

    public void sendMessage(Message msg) throws IOException {
        synchronized (outputStream) {
            outputStream.writeObject(msg);
        }
    }

    public void close() throws IOException {
        this.isStopped = true;
        synchronized (outputStream) {
            outputStream.close();
        }
    }

    private void ping() throws IOException {
        sendMessage(new PingMessage() );
    }
}
