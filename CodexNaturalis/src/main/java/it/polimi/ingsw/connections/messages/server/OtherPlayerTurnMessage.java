package it.polimi.ingsw.connections.messages.server;

import it.polimi.ingsw.connections.client.ConnectionBridge;

/**
 * Message that notifies the client that it is the turn of another player
 */
public class OtherPlayerTurnMessage extends ServerToClientMessage{
    final private String currentPlayer;

    /**
     * Constructor
     * @param currentPlayer the username of the player that has to play
     */
    public OtherPlayerTurnMessage(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Execute a method on the client to notify that it is the turn of another player
     * @param bridge the client's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.OtherPlayerTurnMessage(currentPlayer);
    }

}
