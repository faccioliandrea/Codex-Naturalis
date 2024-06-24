package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.data.CardInfo;

import java.util.ArrayList;

public class DrawSuccessMessage extends ServerToClientMessage {
    final private ArrayList<CardInfo> updatedHand;

    public DrawSuccessMessage(ArrayList<CardInfo> updatedHand) {
        this.updatedHand = updatedHand;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.drawSuccess(this.updatedHand);
    }
}
