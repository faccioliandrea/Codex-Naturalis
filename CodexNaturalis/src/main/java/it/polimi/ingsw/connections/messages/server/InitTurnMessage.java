package it.polimi.ingsw.connections.messages.server;

import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.data.TurnInfo;

/**
 * Message that contains the data needed to initialize a turn
 */
public class InitTurnMessage extends ServerToClientMessage {
    final private TurnInfo turnInfo;

    /**
     * Constructor
     * @param turnInfo the data needed to initialize a turn
     */
    public InitTurnMessage(TurnInfo turnInfo) {
        this.turnInfo = turnInfo;
    }

    /**
     * Execute a method on the client to initialize a turn
     * @param bridge the client's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {

        bridge.initTurn(turnInfo);
    }
}
