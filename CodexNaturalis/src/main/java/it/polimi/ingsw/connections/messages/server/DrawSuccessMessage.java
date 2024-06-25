package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.data.CardInfo;

import java.util.ArrayList;

/**
 * Message that contains the updated hand of the player that drew a card
 */
public class DrawSuccessMessage extends ServerToClientMessage {
    final private ArrayList<CardInfo> updatedHand;

    /**
     * Constructor
     * @param updatedHand the updated hand of the player that drew a card
     */
    public DrawSuccessMessage(ArrayList<CardInfo> updatedHand) {
        this.updatedHand = updatedHand;
    }

    /**
     * Execute a method on the client to update the hand of the player that drew a card
     * @param bridge the client's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.drawSuccess(this.updatedHand);
    }
}
