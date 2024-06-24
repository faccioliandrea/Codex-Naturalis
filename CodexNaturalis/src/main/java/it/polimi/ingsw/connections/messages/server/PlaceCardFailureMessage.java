package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;

public class PlaceCardFailureMessage extends ServerToClientMessage {

    public PlaceCardFailureMessage() {
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.placeCardFailure();
    }
}
