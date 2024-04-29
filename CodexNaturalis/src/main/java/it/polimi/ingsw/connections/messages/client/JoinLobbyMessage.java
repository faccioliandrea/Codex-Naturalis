package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.controller.server.ServerController;

public class JoinLobbyMessage extends ClientToServerMessage {
    private String lobbyID;

    public JoinLobbyMessage(String username, String lobbyID) {
        this.username = username;
        this.lobbyID = lobbyID;
    }

    @Override
    public void execute(ServerController controller) {
        controller.addPlayerToLobby(this.username, this.lobbyID);
    }
}
