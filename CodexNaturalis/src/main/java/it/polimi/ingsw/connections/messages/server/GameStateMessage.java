package it.polimi.ingsw.connections.messages.server;
import it.polimi.ingsw.connections.client.ConnectionBridge;
import it.polimi.ingsw.connections.data.GameStateInfo;

/**
 * Message that contains the updated game state
 */
public class GameStateMessage extends ServerToClientMessage {
    final private GameStateInfo gameStateInfo;

    /**
     * Constructor
     * @param gameStateInfo the updated game state
     */
    public GameStateMessage(GameStateInfo gameStateInfo) {
        this.gameStateInfo = gameStateInfo;
    }

    /**
     * Execute a method on the client to update the game state
     * @param bridge the client's bridge
     */
    @Override
    public void execute(ConnectionBridge bridge) {
        bridge.gameState(gameStateInfo);
    }
}
