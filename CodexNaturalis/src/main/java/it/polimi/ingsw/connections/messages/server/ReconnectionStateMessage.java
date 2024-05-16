package it.polimi.ingsw.connections.messages.server;

import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.data.GameStateInfo;

public class ReconnectionStateMessage extends ServerToClientMessage {
    private GameStateInfo gameStateInfo;

    public ReconnectionStateMessage(GameStateInfo gameStateInfo) {
        this.gameStateInfo = gameStateInfo;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.reconnectionState(gameStateInfo);
    }
}
