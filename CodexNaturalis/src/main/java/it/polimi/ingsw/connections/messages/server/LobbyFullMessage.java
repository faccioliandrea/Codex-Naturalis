package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.controller.client.ClientController;

public class LobbyFullMessage extends ServerToClientMessage {
    public LobbyFullMessage() {}

    @Override
    public void execute(ClientController controller) {
        controller.lobbyFull();
    }
}
