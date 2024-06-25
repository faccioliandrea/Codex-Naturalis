package it.polimi.ingsw.connections.messages.server;

import it.polimi.ingsw.connections.client.ConnectionBridge;

/**
 * Message that notifies the client that no other player is connected
 */
public class PlayerReconnectedMessage extends ServerToClientMessage {
    final private String username;

    /**
     * Constructor
     * @param username the username of the player that has reconnected
     */
    public PlayerReconnectedMessage(String username) {
        this.username = username;
    }

    /**
     * Execute a method on the client to notify that a player has reconnected
     * @param bridge the client's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.playerReconnected(this.username);
    }
}
