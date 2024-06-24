package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.connections.server.ConnectionBridge;
import java.awt.*;

public class PlaceCardMessage extends ClientToServerMessage {
    final private String cardID;
    final private Point position;
    final private boolean flipped;

    public PlaceCardMessage(String username, String cardID, Point position, boolean flipped) {
        this.username = username;
        this.cardID = cardID;
        this.position = position;
        this.flipped = flipped;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
    bridge.placeCard(this.username, this.cardID, this.position, this.flipped);
    }
}
