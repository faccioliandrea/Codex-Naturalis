package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.controller.client.ClientController;

public class GameStartedMessage extends ServerToClientMessage {
    public GameStartedMessage() {}

    @Override
    public void execute(ClientController controller) {
        controller.gameStarted();
    }
}
