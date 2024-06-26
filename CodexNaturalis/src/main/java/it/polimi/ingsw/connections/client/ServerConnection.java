package it.polimi.ingsw.connections.client;

import it.polimi.ingsw.connections.ConnectionStatus;

import java.io.IOException;

/**
 * Interface for the connection to the server 
 */
public interface  ServerConnection {
    /**
     * Called from child threads when an exception is thrown
     * @param e: Exception message
     */
    void threadExceptionCallback(String e) throws IOException;
    
    /**
     * Close the connection
     */
    void close() throws IOException;
    
    /**
     * Get the status of the connection
     * @return ConnectionStatus
     */
    ConnectionStatus getStatus() throws IOException;
}
