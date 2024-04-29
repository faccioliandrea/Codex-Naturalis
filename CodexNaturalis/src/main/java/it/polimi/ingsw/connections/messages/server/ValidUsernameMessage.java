package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.controller.client.ClientController;

public class ValidUsernameMessage extends ServerToClientMessage {
    public ValidUsernameMessage() {}

    @Override
    public void execute(ClientController controller) {}
}
