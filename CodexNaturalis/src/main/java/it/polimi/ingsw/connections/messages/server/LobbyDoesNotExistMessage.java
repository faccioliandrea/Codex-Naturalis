package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;

/**
 * Message that notifies the client that the lobby does not exist
 */
public class LobbyDoesNotExistMessage extends ServerToClientMessage {
    /**
     * Constructor
     */
    public LobbyDoesNotExistMessage() {}

    /**
     * Execute a method on the client to notify that the lobby does not exist
     * @param bridge the client's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.lobbyDoesNotExists();
    }
}
