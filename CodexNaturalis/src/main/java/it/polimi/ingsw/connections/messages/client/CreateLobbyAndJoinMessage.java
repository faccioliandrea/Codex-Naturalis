package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.controller.server.ServerController;

public class CreateLobbyAndJoinMessage extends ClientToServerMessage {
    private int numOfPlayers;

    public CreateLobbyAndJoinMessage(String username, int numOfPlayers) {
        this.username = username;
        this.numOfPlayers = numOfPlayers;
    }

    @Override
    public void execute(ServerController controller) {
        controller.createLobby(this.username, this.numOfPlayers);
    }
}
