package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;

public class PlayerJoinedMessage extends ServerToClientMessage {
    private String username;

    public PlayerJoinedMessage(String username) {
        this.username = username;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.playerJoinedLobby(this.username);
    }
}
