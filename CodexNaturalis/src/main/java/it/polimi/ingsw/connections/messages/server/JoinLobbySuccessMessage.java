package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;

public class JoinLobbySuccessMessage extends ServerToClientMessage {
    public JoinLobbySuccessMessage() {}

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.joinLobbySuccess();
    }
}
