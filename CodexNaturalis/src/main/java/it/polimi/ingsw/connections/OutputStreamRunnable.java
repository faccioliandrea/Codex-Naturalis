package it.polimi.ingsw.connections;

import java.io.*;
import java.util.function.Consumer;

import it.polimi.ingsw.connections.messages.Message;
import it.polimi.ingsw.connections.messages.PingMessage;

/**
 * Runnable class that sends messages to the server
 */
public class OutputStreamRunnable implements StreamRunnable {
    private final ObjectOutputStream outputStream;
    private boolean isStopped;
    final private Consumer<String> callback;

    final Object lock = new Object();

    /**
     * Constructor
     * @param oos: ObjectOutputStream to write to
     * @param callback: Consumer to call when an exception is thrown
     */ 
    public OutputStreamRunnable(ObjectOutputStream oos, Consumer<String> callback) throws IOException {
        this.outputStream = oos;
        this.isStopped = false;
        this.callback = callback;
    }

    /**
     * Constructor
     * @param os: OutputStream to write to
     * @param callback: Consumer to call when an exception is thrown
     */ 
    public OutputStreamRunnable(OutputStream os, Consumer<String> callback) throws IOException {
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
            this.callback.accept(e.getMessage());
            return;
        }
    }

    /**
     * Send a message to the server
     * @param msg: Message to send
     * @throws IOException if an error occurs while sending the message
     */
    public void sendMessage(Message msg) throws IOException {
        synchronized (outputStream) {
            outputStream.writeObject(msg);
        }
    }

    /**
     * Close the output stream
     * @throws IOException if an error occurs while closing the stream
     */ 
    public void close() throws IOException {
        this.isStopped = true;
        synchronized (outputStream) {
            outputStream.close();
        }
    }

    /**
     * Send a ping message to the server
     * @throws IOException if an error occurs while sending the message
     */
    private void ping() throws IOException {
        sendMessage(new PingMessage() );
    }
}
