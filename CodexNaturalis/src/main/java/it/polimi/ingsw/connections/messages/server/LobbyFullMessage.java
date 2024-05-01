package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;

public class LobbyFullMessage extends ServerToClientMessage {
    public LobbyFullMessage() {}

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.lobbyFull();
    }
}
