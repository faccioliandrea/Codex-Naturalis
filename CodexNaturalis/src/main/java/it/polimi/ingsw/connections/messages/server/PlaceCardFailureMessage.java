package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;

/**
 * Message that notifies the client that the card cannot be placed
 */
public class PlaceCardFailureMessage extends ServerToClientMessage {

    /**
     * Constructor
     */
    public PlaceCardFailureMessage() {}


    /**
     * Execute a method on the client to notify that the card cannot be placed
     * @param bridge the client's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.placeCardFailure();
    }
}
