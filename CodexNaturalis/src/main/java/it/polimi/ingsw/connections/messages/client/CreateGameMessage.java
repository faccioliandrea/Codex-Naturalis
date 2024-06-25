package it.polimi.ingsw.connections.messages.client;

import it.polimi.ingsw.connections.server.ConnectionBridge;

/**
 * Message that contains the username of the player that wants to create a game
 */
public class CreateGameMessage extends ClientToServerMessage {
    /**
     * Constructor
     * @param username the player's username
     */
    public CreateGameMessage(String username) {
        this.username = username;
    }

    /**
     * Execute a method on the server to create a game
     * @param bridge the server's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.createGame(username);
    }
}
