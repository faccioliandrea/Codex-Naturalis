package it.polimi.ingsw.connections.messages.client;

import it.polimi.ingsw.connections.server.ConnectionBridge;

/**
 * Message that contains the index of the private goal chosen by the player
 */
public class ChoosePrivateGoalMessage extends ClientToServerMessage {
    final private int index;

    /**
     * Constructor
     * @param username the player's username
     * @param index the index of the private goal chosen by the player
     */
    public ChoosePrivateGoalMessage(String username, int index) {
        this.username = username;
        this.index = index;
    }

    /**
     * Execute a method on the server to choose a private goal
     * @param bridge the server's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.choosePrivateGoal(this.username, this.index);
    }
}
