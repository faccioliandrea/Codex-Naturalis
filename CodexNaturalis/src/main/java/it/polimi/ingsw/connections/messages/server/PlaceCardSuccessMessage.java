package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.data.PlaceCardSuccessInfo;

/**
 * Message that notifies the client that the card has been placed successfully
 */
public class PlaceCardSuccessMessage extends ServerToClientMessage {
    final private PlaceCardSuccessInfo placeCardSuccessInfo;

    /**
     * Constructor
     * @param placeCardSuccessInfo the information about the card placement
     */
    public PlaceCardSuccessMessage(PlaceCardSuccessInfo placeCardSuccessInfo) {
        this.placeCardSuccessInfo = placeCardSuccessInfo;
    }

    /**
     * Execute a method on the client to notify that the card has been placed successfully
     * @param bridge the client's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.placeCardSuccess(this.placeCardSuccessInfo);
    }
}
