package it.polimi.ingsw.connections;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

import it.polimi.ingsw.connections.messages.*;

/**
 * Runnable class that listens for incoming messages from the server 
 */
public class InputStreamRunnable implements StreamRunnable {
  private final ObjectInputStream inputStream;
  private boolean isStopped;
  final private BlockingQueue<Message> msgQueue;
  final private Consumer<String> callback;

  /**
   * Constructor
   * @param ois: ObjectInputStream to read from
   * @param msgQueue: BlockingQueue where to put the read messages
   * @param callback: Consumer to call when an exception is thrown
   */
  public InputStreamRunnable(ObjectInputStream ois, BlockingQueue<Message> msgQueue, Consumer<String> callback)
      throws IOException {
    this.inputStream = ois;
    this.isStopped = false;
    this.msgQueue = msgQueue;
    this.callback = callback;
  }

  /**
   * Constructor
   * @param is: InputStream to read from
   * @param msgQueue: BlockingQueue where to put the read messages
   * @param callback: Consumer to call when an exception is thrown
   */
  public InputStreamRunnable(InputStream is, BlockingQueue<Message> msgQueue, Consumer<String> callback)
      throws IOException {
    this.inputStream = new ObjectInputStream(is);
    this.isStopped = false;
    this.msgQueue = msgQueue;
    this.callback = callback;
  }

  /**
   * Getter for the ObjectInputStream
   * @return ObjectInputStream
   */
  public ObjectInputStream getInputStream() {
    return inputStream;
  }

  /**
   * Runnable method that listens for incoming messages from the server
   */
  @Override
  public void run() {
    try {
      while (!this.isStopped) {
        synchronized (inputStream) {
          Message msg = (Message) inputStream.readObject();
          if (!(msg instanceof PingMessage)) {
            msgQueue.put(msg);
          }
        }
      }
    } catch (IOException | InterruptedException | ClassNotFoundException e) {
      this.callback.accept(e.getMessage());
      return;
    }
  }

  /**
   * Close the input stream
   */
  public void close() throws IOException {
    this.isStopped = true;
    synchronized (inputStream) {
      inputStream.close();
    }
  }
}
