package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;

public class JoinLobbySuccessMessage extends ServerToClientMessage {
    private boolean isLastPlayer;
    public JoinLobbySuccessMessage(boolean isLastPlayer) {
        this.isLastPlayer = isLastPlayer;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.joinLobbySuccess(isLastPlayer);
    }
}
