package it.polimi.ingsw.connections;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

import it.polimi.ingsw.connections.messages.Message;
import it.polimi.ingsw.connections.messages.*;

public class InputStreamRunnable implements StreamRunnable {
    private final ObjectInputStream inputStream;
    private boolean isStopped;
//    private IOException exception;
    private BlockingQueue<Message> msgQueue;
    private Consumer<String> callback;
    // TODO: last ping timestamp

    public InputStreamRunnable(ObjectInputStream ois, BlockingQueue<Message> msgQueue, Consumer<String> callback) throws IOException {
        this.inputStream = ois;
        this.isStopped = false;
        this.msgQueue = msgQueue;
        this.callback = callback;
    }

    public InputStreamRunnable(InputStream is, BlockingQueue<Message> msgQueue, Consumer<String> callback) throws IOException {
        this.inputStream = new ObjectInputStream(is);
        this.isStopped = false;
        this.msgQueue = msgQueue;
        this.callback = callback;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    @Override
    public void run() {
        try {
            while (!this.isStopped) {
                synchronized (inputStream) {
                    Message msg = (Message) inputStream.readObject();
                    if (! (msg instanceof PingMessage)) {
                        msgQueue.put(msg);
                    }
                }
            }
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            return;
        }
    }

    public void close() throws IOException {
        this.isStopped = true;
        synchronized (inputStream) {
            inputStream.close();
        }
    }
}
