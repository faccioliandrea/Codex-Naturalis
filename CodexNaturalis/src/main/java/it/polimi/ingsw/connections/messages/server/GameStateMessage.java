package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.data.CardInfo;
import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.data.GameStateInfo;

import java.util.ArrayList;

public class GameStateMessage extends ServerToClientMessage {
    private GameStateInfo gameStateInfo;

    public GameStateMessage(GameStateInfo gameStateInfo) {
        this.gameStateInfo = gameStateInfo;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.gameState(gameStateInfo);
    }
}
