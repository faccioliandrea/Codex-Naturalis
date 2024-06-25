package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.connections.server.ConnectionBridge;
import java.awt.*;

/**
 * Message that contains the cardID, the position and the flip status of the card that the player wants to place
 */
public class PlaceCardMessage extends ClientToServerMessage {
    final private String cardID;
    final private Point position;
    final private boolean flipped;

    /**
     * Constructor
     * @param username the player's username
     * @param cardID the ID of the card that the player wants to place
     * @param position the position where the player wants to place the card
     * @param flipped the flip status of the card
     */
    public PlaceCardMessage(String username, String cardID, Point position, boolean flipped) {
        this.username = username;
        this.cardID = cardID;
        this.position = position;
        this.flipped = flipped;
    }

    /**
     * Execute a method on the server to place a card
     * @param bridge the server's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
    bridge.placeCard(this.username, this.cardID, this.position, this.flipped);
    }
}
