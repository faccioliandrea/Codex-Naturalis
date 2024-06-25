package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;

/**
 * Message that contains the result of the join lobby request
 */
public class JoinLobbySuccessMessage extends ServerToClientMessage {
    final private boolean isLastPlayer;

    /**
     * Constructor
     * @param isLastPlayer true if the player is the last one to join the lobby
     */
    public JoinLobbySuccessMessage(boolean isLastPlayer) {
        this.isLastPlayer = isLastPlayer;
    }

    /**
     * Execute a method on the client to handle the result of the join lobby request
     * @param bridge the client's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.joinLobbySuccess(isLastPlayer);
    }
}
