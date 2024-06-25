package it.polimi.ingsw.connections.messages.server;

import it.polimi.ingsw.connections.client.ConnectionBridge;

/**
 * Message that contains the id of the lobby that has been created
 */
public class LobbyCreatedMessage extends ServerToClientMessage {
    final private String lobbyId;

    /**
     * Constructor
     * @param lobbyId id of the lobby
     */
    public LobbyCreatedMessage(String lobbyId) {
        this.lobbyId = lobbyId;
    }

    /**
     * Execute a method on the client to notify that a lobby has been created
     * @param bridge the client's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.lobbyCreated(this.lobbyId);
    }
}
