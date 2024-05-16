package it.polimi.ingsw.connections.messages.server;

import it.polimi.ingsw.connections.client.ConnectionBridge;

public class PlayerDisconnectedMessage extends ServerToClientMessage {
    private String username;
    private boolean gameStarted;

    public PlayerDisconnectedMessage(String username, boolean gameStarted) {
        this.username = username;
        this.gameStarted = gameStarted;
    }
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.playerDisconnected(this.username, this.gameStarted);
    }
}
