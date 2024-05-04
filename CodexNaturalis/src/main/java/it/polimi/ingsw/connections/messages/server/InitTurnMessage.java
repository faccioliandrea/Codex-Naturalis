package it.polimi.ingsw.connections.messages.server;

import java.awt.*;
import java.util.ArrayList;

import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.data.TurnInfo;

public class InitTurnMessage extends ServerToClientMessage {
    private TurnInfo turnInfo;

    public InitTurnMessage(TurnInfo turnInfo) {
        this.turnInfo = turnInfo;
    }

    @Override
    public void execute(ConnectionBridge bridge) {

        bridge.initTurn(turnInfo);
    }
}
