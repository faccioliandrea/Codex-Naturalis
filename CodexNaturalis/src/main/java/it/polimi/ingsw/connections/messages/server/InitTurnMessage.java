package it.polimi.ingsw.connections.messages.server;

import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.data.TurnInfo;

public class InitTurnMessage extends ServerToClientMessage {
    final private TurnInfo turnInfo;

    public InitTurnMessage(TurnInfo turnInfo) {
        this.turnInfo = turnInfo;
    }

    @Override
    public void execute(ConnectionBridge bridge) {

        bridge.initTurn(turnInfo);
    }
}
