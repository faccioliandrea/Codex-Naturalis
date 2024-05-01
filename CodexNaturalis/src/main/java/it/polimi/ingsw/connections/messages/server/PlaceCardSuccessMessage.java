package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;

public class PlaceCardSuccessMessage extends ServerToClientMessage {
    private int updatedPlayerPoints;
    private int updatedGoalPoints;

    public PlaceCardSuccessMessage(int updatedPlayerPoints, int updatedGoalPoints) {
        this.updatedPlayerPoints = updatedPlayerPoints;
        this.updatedGoalPoints = updatedGoalPoints;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.placeCardSuccess(this.updatedPlayerPoints, this.updatedGoalPoints);
    }
}
