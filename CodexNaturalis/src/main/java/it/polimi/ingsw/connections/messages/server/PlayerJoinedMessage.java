package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.controller.client.ClientController;

public class PlayerJoinedMessage extends ServerToClientMessage {
    private String username;

    public PlayerJoinedMessage(String username) {
        this.username = username;
    }

    @Override
    public void execute(ClientController controller) {
        controller.playerJoined(this.username);
    }
}
