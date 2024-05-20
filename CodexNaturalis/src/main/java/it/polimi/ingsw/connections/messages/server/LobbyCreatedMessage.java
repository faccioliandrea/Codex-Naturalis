package it.polimi.ingsw.connections.messages.server;

import it.polimi.ingsw.connections.client.ConnectionBridge;

public class LobbyCreatedMessage extends ServerToClientMessage {
    private String lobbyId;

    public LobbyCreatedMessage(String lobbyId) {
        this.lobbyId = lobbyId;
    }
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.lobbyCreated(this.lobbyId);
    }
}
