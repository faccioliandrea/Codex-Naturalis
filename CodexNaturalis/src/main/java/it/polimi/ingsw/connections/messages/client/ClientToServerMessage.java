package it.polimi.ingsw.connections.messages.client;

import it.polimi.ingsw.connections.messages.Message;
import it.polimi.ingsw.controller.server.ServerController;

public abstract class ClientToServerMessage implements Message {
    protected String username;

    public abstract void execute(ServerController controller);
}
