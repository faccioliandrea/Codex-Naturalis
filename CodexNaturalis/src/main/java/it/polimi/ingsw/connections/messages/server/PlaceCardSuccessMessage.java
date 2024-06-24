package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.data.PlaceCardSuccessInfo;

public class PlaceCardSuccessMessage extends ServerToClientMessage {
    final private PlaceCardSuccessInfo placeCardSuccessInfo;

    public PlaceCardSuccessMessage(PlaceCardSuccessInfo placeCardSuccessInfo) {
        this.placeCardSuccessInfo = placeCardSuccessInfo;

    }

    @Override
    public void execute(ConnectionBridge bridge) {


        bridge.placeCardSuccess(this.placeCardSuccessInfo);
    }
}
