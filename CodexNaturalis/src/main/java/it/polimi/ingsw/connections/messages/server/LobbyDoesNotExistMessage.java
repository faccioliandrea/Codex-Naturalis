package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.controller.client.ClientController;

public class LobbyDoesNotExistMessage extends ServerToClientMessage {
    public LobbyDoesNotExistMessage() {}

    @Override
    public void execute(ClientController controller) {
        controller.lobbyDoesNotExist();
    }
}
