package it.polimi.ingsw.connections.messages.server;

import it.polimi.ingsw.connections.client.ConnectionBridge;

/**
 * Message that notifies the client that a player has disconnected
 */
public class PlayerDisconnectedMessage extends ServerToClientMessage {
    final private String username;
    final private boolean gameStarted;

    /**
     * Constructor
     * @param username the username of the player that has disconnected
     * @param gameStarted true if the game has started
     */
    public PlayerDisconnectedMessage(String username, boolean gameStarted) {
        this.username = username;
        this.gameStarted = gameStarted;
    }

    /**
     * Execute a method on the client to notify that a player has disconnected
     * @param bridge the client's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.playerDisconnected(this.username, this.gameStarted);
    }
}
