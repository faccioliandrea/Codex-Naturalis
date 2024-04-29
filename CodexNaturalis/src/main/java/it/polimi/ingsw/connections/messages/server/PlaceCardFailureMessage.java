package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.controller.client.ClientController;

public class PlaceCardFailureMessage extends ServerToClientMessage {
    public PlaceCardFailureMessage() {}

    @Override
    public void execute(ClientController controller) {
        controller.placeCardFailure();
    }
}
