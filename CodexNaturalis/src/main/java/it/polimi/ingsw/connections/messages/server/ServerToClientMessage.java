package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.messages.Message;
import it.polimi.ingsw.connections.client.ConnectionBridge;

/**
 * Message that notifies the client of a server event
 */
public abstract class ServerToClientMessage implements Message {
    /**
     * Execute a method on the client to notify of a server event
     * @param bridge the client's bridge
     */
    public abstract void execute(ConnectionBridge bridge);
}
