package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.controller.client.ClientController;

public class InvalidUsernameMessage extends ServerToClientMessage {
    public InvalidUsernameMessage() {}

    @Override
    public void execute(ClientController controller) {
        controller.invalidUsername();
    }
}
