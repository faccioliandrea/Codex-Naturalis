package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.connections.server.ConnectionBridge;

/**
 * Message that contains the index of the resource card chosen by the player
 */
public class DrawResourceCardMessage extends ClientToServerMessage {
    final private int index;

    /**
     * Constructor
     * @param username the player's username
     * @param cardIndex the index of the resource card chosen by the player
     */
    public DrawResourceCardMessage(String username, int cardIndex) {
        this.username = username;
        this.index = cardIndex;
    }

    /**
     * Execute a method on the server to draw a resource card
     * @param bridge the server's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.drawResource(this.username, this.index);
    }
}
