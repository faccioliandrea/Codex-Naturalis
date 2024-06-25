package it.polimi.ingsw.connections.messages.client;

import it.polimi.ingsw.connections.server.ConnectionBridge;

/**
 * Message that contains the side of the starter card chosen by the player
 */
public class ChooseStarterCardSideMessage extends ClientToServerMessage {
    final private boolean flipped;

    /**
     * Constructor
     * @param username the player's username
     * @param flipped the side of the starter card chosen by the player
     */
    public ChooseStarterCardSideMessage(String username, boolean flipped) {
        this.username = username;
        this.flipped = flipped;
    }

    /**
     * Execute a method on the server to choose the side of the starter card
     * @param bridge the server's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.chooseStarterCardSide(this.username, this.flipped);
    }
}
