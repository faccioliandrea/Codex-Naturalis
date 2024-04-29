package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.controller.client.ClientController;

public abstract class ServerToClientMessage {
    public abstract void execute(ClientController controller);
}
