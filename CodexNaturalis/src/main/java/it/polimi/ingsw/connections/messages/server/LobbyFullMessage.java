package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;

/**
 * Message that notifies the client that the lobby is full
 */
public class LobbyFullMessage extends ServerToClientMessage {
    /**
     * Constructor
     */
    public LobbyFullMessage() {}

    /**
     * Execute a method on the client to notify that the lobby is full
     * @param bridge the client's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.lobbyFull();
    }
}
