package it.polimi.ingsw.connections;

import java.io.IOException;

/**
 * Interface for Runnable classes that use streams 
 */
public interface StreamRunnable extends Runnable{
    /**
     * Close the stream
     * @throws IOException if an error occurs while closing the stream
     */
    void close() throws IOException;
}
