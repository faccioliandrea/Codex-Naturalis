package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.connections.server.ConnectionBridge;


public class JoinLobbyMessage extends ClientToServerMessage {
    final private String lobbyID;

    public JoinLobbyMessage(String username, String lobbyID) {
        this.username = username;
        this.lobbyID = lobbyID;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.addPlayerToLobby(this.username, this.lobbyID);
    }
}
