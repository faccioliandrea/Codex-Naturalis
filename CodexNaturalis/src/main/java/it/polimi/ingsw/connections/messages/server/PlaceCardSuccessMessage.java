package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.data.PlaceCardSuccessInfo;
import it.polimi.ingsw.model.enumeration.CardSymbol;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class PlaceCardSuccessMessage extends ServerToClientMessage {
    private PlaceCardSuccessInfo placeCardSuccessInfo;

    public PlaceCardSuccessMessage(PlaceCardSuccessInfo placeCardSuccessInfo) {
        this.placeCardSuccessInfo = placeCardSuccessInfo;

    }

    @Override
    public void execute(ConnectionBridge bridge) {


        bridge.placeCardSuccess(this.placeCardSuccessInfo);
    }
}
