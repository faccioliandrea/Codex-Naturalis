package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.connections.server.ConnectionBridge;

/**
 * Message that contains the index of the gold card drawn by the player
 */
public class DrawGoldCardMessage extends ClientToServerMessage {
    final private int index;

    /**
     * Constructor
     * @param username the player's username
     * @param index the index of the gold card drawn by the player
     */
    public DrawGoldCardMessage(String username, int index) {
        this.username = username;
        this.index = index;
    }

    /**
     * Execute a method on the server to draw a gold card
     * @param bridge the server's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.drawGold(this.username, this.index);
    }
}
