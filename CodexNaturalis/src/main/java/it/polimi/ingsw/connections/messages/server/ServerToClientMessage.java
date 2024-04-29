package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.messages.Message;
import it.polimi.ingsw.controller.client.ClientController;

public abstract class ServerToClientMessage implements Message {
    public abstract void execute(ClientController controller);
}
