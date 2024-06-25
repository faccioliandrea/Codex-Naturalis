package it.polimi.ingsw.connections.messages.server;

import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.data.GameStateInfo;

/**
 * Message that notifies the client that the game state has been restored
 */
public class ReconnectionStateMessage extends ServerToClientMessage {
    final private GameStateInfo gameStateInfo;

    /**
     * Constructor
     * @param gameStateInfo the information about the game state
     */
    public ReconnectionStateMessage(GameStateInfo gameStateInfo) {
        this.gameStateInfo = gameStateInfo;
    }

    /**
     * Execute a method on the client to notify that the game state has been restored
     * @param bridge the client's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.reconnectionState(gameStateInfo);
    }
}
