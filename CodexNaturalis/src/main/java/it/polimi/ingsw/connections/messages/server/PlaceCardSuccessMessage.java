package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.data.CardInfo;

public class PlaceCardSuccessMessage extends ServerToClientMessage {
    private int updatedPlayerPoints;
    private int updatedGoalPoints;

    private CardInfo placedCard;

    public PlaceCardSuccessMessage(int updatedPlayerPoints, int updatedGoalPoints, CardInfo placedCard) {
        this.updatedPlayerPoints = updatedPlayerPoints;
        this.updatedGoalPoints = updatedGoalPoints;
        this.placedCard = placedCard;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.placeCardSuccess(this.updatedPlayerPoints, this.updatedGoalPoints, this.placedCard);
    }
}
