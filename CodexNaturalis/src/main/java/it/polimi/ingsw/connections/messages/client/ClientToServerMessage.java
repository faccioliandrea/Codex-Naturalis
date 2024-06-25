package it.polimi.ingsw.connections.messages.client;

import it.polimi.ingsw.connections.messages.Message;
import it.polimi.ingsw.connections.server.ConnectionBridge;

/**
 * Abstract class for a message sent by the client to the server
 */
public abstract class ClientToServerMessage implements Message {
    /**
     * The player's username
     */
    protected String username;

    /**
     * Execute a method on the server
     * @param bridge the server's bridge
     */
    public abstract void execute(ConnectionBridge bridge);
}
