package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;

public class LobbyDoesNotExistMessage extends ServerToClientMessage {
    public LobbyDoesNotExistMessage() {}

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.lobbyDoesNotExists();
    }
}
