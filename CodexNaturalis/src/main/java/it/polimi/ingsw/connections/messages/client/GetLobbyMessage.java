package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.controller.server.ServerController;

public class GetLobbyMessage extends ClientToServerMessage {
    public GetLobbyMessage(String username) {
        this.username = username;
    }
    @Override
    public void execute(ServerController controller) {
        controller.getLobbies(this.username);
    }
}
