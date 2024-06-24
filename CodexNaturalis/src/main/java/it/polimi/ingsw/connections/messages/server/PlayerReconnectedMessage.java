package it.polimi.ingsw.connections.messages.server;

import it.polimi.ingsw.connections.client.ConnectionBridge;

public class PlayerReconnectedMessage extends ServerToClientMessage {
    final private String username;

    public PlayerReconnectedMessage(String username) {
        this.username = username;
    }
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.playerReconnected(this.username);
    }
}
