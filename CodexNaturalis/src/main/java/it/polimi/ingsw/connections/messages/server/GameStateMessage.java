package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.data.GameStateInfo;

public class GameStateMessage extends ServerToClientMessage {
    final private GameStateInfo gameStateInfo;

    public GameStateMessage(GameStateInfo gameStateInfo) {
        this.gameStateInfo = gameStateInfo;
    }

    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.gameState(gameStateInfo);
    }
}
