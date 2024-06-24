package it.polimi.ingsw.connections.messages.server;

import it.polimi.ingsw.connections.client.ConnectionBridge;

public class OtherPlayerTurnMessage extends ServerToClientMessage{

    final private String currentPlayer;
    public OtherPlayerTurnMessage(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.OtherPlayerTurnMessage(currentPlayer);
    }

}
