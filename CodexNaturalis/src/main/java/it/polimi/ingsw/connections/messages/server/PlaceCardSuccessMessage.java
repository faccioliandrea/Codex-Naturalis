package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.data.CardInfo;

import java.awt.*;
import java.util.ArrayList;

public class PlaceCardSuccessMessage extends ServerToClientMessage {
    private int updatedPlayerPoints;
    private int updatedGoalPoints;

    private CardInfo placedCard;
    private ArrayList<Point> newAvailable;

    public PlaceCardSuccessMessage(int updatedPlayerPoints, int updatedGoalPoints, CardInfo placedCard, ArrayList<Point> newAvailable) {
        this.updatedPlayerPoints = updatedPlayerPoints;
        this.updatedGoalPoints = updatedGoalPoints;
        this.placedCard = placedCard;
        this.newAvailable = newAvailable;

    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.placeCardSuccess(this.updatedPlayerPoints, this.updatedGoalPoints, this.placedCard, this.newAvailable);
    }
}
