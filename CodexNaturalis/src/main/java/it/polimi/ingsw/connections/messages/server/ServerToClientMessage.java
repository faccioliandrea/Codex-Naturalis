package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.messages.Message;
import it.polimi.ingsw.connections.client.ConnectionBridge;


public abstract class ServerToClientMessage implements Message {
    public abstract void execute(ConnectionBridge bridge);
}
