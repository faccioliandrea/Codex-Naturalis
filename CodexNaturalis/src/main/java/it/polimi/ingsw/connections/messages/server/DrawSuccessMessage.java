package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.controller.CardInfo;

import java.util.ArrayList;
import java.util.Collection;

public class DrawSuccessMessage extends ServerToClientMessage {
    private ArrayList<CardInfo> updatedHand;

    public DrawSuccessMessage(ArrayList<CardInfo> updatedHand) {
        this.updatedHand = updatedHand;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.drawSuccess(this.updatedHand);
    }
}
