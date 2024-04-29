package it.polimi.ingsw.connections.messages.client;
import it.polimi.ingsw.controller.server.ServerController;
import java.awt.*;

public class PlaceCardMessage extends ClientToServerMessage {
    private String cardID;
    private Point position;
    private boolean flipped;

    public PlaceCardMessage(String username, String cardID, Point position, boolean flipped) {
        this.username = username;
        this.cardID = cardID;
        this.position = position;
        this.flipped = flipped;
    }

    @Override
    public void execute(ServerController controller) {
    controller.placeCard(this.username, this.cardID, this.position, this.flipped);
    }
}
